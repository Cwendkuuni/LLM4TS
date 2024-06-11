
/**
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
package com.oceanbase.odc.test.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    
    @Before
    public void setUp() {
        //TODO: Construct ConnectionConfig object for testing purpose
        //this.connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testGetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        // Replace expectedUserName with your expected value
        String expectedUsername = "username";
        assertEquals(expectedUsername, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        // Replace expectedPassword with your expected value
        String expectedPassword = "password";
        assertEquals(expectedPassword, OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        // Replace expectedJdbcUrl with your expected value
        String expectedJdbcUrl = "jdbcurl";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.deepCopy());
    }

    // Similar tests can be added for other reset methods
}
