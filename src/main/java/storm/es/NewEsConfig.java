/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package storm.es;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 0.11
 */
public class NewEsConfig implements Serializable {

    private final String clusterName;
    private final String[] nodes;
    private final Map<String, String> additionalConfiguration;

    /**
     * EsConfig Constructor to be used in EsIndexBolt, EsPercolateBolt and EsStateFactory
     *
     * @param clusterName Elasticsearch cluster name
     * @param nodes       Elasticsearch addresses in host:port pattern string array
     * @throws IllegalArgumentException if nodes are empty
     * @throws NullPointerException     on any of the fields being null
     */
    public NewEsConfig(String clusterName, String[] nodes) {
        this(clusterName, nodes, Collections.<String, String>emptyMap());
    }

    /**
     * EsConfig Constructor to be used in EsIndexBolt, EsPercolateBolt and EsStateFactory
     *
     * @param clusterName             Elasticsearch cluster name
     * @param nodes                   Elasticsearch addresses in host:port pattern string array
     * @param additionalConfiguration Additional Elasticsearch configuration
     * @throws IllegalArgumentException if nodes are empty
     * @throws NullPointerException     on any of the fields being null
     */
    public NewEsConfig(String clusterName, String[] nodes, Map<String, String> additionalConfiguration) {
        this.clusterName = clusterName;
        this.nodes = nodes;
        this.additionalConfiguration = new HashMap(additionalConfiguration);
    }

    TransportAddress[] getTransportAddresses() {
        String[] ns = nodes;
        TransportAddress[] addressArr = new TransportAddress[ns.length];
        for (int i = 0; i < ns.length; i++) {
            try {
                addressArr[i] = new InetSocketTransportAddress(InetAddress.getByName(ns[i]), 9300);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        return addressArr;
    }

    Settings toBasicSettings() {
        return Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put("transport.tcp.compress", true)
                .build();
    }
}
