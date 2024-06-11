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
package com.oceanbase.odc.service.session.factory.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;

    @Before
    public void setUp() {
        // Initialize your class for testing
        config = mock(ConnectionConfig.class);
        factory = new OBConsoleDataSourceFactory(config, null);
    }

    @Test
    public void testGetUsername() {
        String expected = "testUser";
        String generatedUsername = OBConsoleDataSourceFactory.getUsername(config);
        assertEquals(expected, generatedUsername);
    }

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:oceanbase://127.0.0.1:3306/testSchema";
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals(expected, jdbcUrl);

    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("maxAllowedPacket", "64000000");
        expectedParams.put("allowMultiQueries", "true");
        // add all the other expected parameters here

        Map<String, String> actualParams = OBConsoleDataSourceFactory.getJdbcParams(config);

        assertTrue(expectedParams.equals(actualParams));
    }

    @Test(expected = IllegalArgumentException.class) 
    public void testResetPort_NegativeValue() {
        factory.resetPort((port) -> -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_PositiveValue() {
        factory.resetPort((port) -> 8306);
        // assert the update action here, depends on your implementation
    }

    // Add remaining test cases for other methods,
    // including edge cases, and error scenarios in same format.
}