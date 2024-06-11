
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private String connectionId;
    private String schemaName;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
        connectionId = "1001";
        schemaName = "OceanBase";
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("OldSchema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(anyString(), eq(String.class));
    }

    @Test
    public void testGetConnectionId() {
        obOracleSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject(anyString(), eq(String.class));
    }

    @Test
    public void testGetConnectionIdException() {
        when(obOracleSessionExtension.getConnectionId(connection)).thenThrow(new SQLException());
        assertNull(obOracleSessionExtension.getConnectionId(connection));
        verify(connection, times(1)).getServerThreadId();
    }
}
