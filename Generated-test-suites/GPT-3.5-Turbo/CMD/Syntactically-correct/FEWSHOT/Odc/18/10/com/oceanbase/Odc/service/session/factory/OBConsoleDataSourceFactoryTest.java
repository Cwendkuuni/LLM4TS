/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("key1", "value1");
        jdbcParams.put("key2", "value2");
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_db?key1=value1&key2=value2";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();

        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource)dataSource;
        assertEquals("localhost", singleConnectionDataSource.getUrl());
        // Add more assertions here based on the actual properties set in the data source
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();

        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();

        assertNotNull(clonedFactory);
        assertTrue(clonedFactory instanceof OBConsoleDataSourceFactory);
        OBConsoleDataSourceFactory obClonedFactory = (OBConsoleDataSourceFactory)clonedFactory;
        assertEquals("localhost", obClonedFactory.getHost());
        // Add more assertions here based on the actual properties of the cloned factory
    }

    // Add more test cases as needed for other methods in OBConsoleDataSourceFactory

}