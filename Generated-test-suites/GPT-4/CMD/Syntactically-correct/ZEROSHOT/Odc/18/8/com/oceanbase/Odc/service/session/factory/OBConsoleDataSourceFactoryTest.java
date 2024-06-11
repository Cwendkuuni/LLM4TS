
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

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.DialectType;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("host");
        connectionConfig.setPort(1234);
        connectionConfig.setDefaultSchema("defaultSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
        obTenantEndpoint.setProxyHost("proxyHost");
        obTenantEndpoint.setProxyPort(5678);
        connectionConfig.setEndpoint(obTenantEndpoint);
        
        Map<String, String> jdbcUrlParameters = new HashMap<>();
        jdbcUrlParameters.put("param1", "value1");
        connectionConfig.setJdbcUrlParameters(jdbcUrlParameters);
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactoryCopy = (OBConsoleDataSourceFactory) obConsoleDataSourceFactory.deepCopy();
        assertNotNull(obConsoleDataSourceFactoryCopy);
    }

    // similar tests for other methods...
}
