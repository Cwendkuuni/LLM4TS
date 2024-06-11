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
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
    Connection mockConnection = mock(Connection.class);

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(mockConnection, "123");
        
        // Check that the 'execute' method was called with "KILL QUERY 123"
        verify(mockJdbcOperations, times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(sessionExtension.getCurrentSchema(mockConnection)).thenReturn("oldSchema");

        sessionExtension.switchSchema(mockConnection, "newSchema");

        // Check that the 'update' method was called with the correct SQL
        verify(mockJdbcOperations, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=\"newSchema\"");
    }

    @Test
    public void testGetCurrentSchema() {
        sessionExtension.getCurrentSchema(mockConnection);

        // Check that the 'queryForObject' method was called with the right SQL query
        verify(mockJdbcOperations, times(1)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        sessionExtension.getConnectionId(mockConnection);

        // Check that the 'queryForObject' method was called with the right SQL query
        verify(mockJdbcOperations, times(1)).queryForObject("select userenv('sessionid') from dual", String.class);
    }
}