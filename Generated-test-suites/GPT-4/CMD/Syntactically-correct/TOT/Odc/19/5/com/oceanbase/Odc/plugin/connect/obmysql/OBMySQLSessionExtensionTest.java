package com.oceanbase.odc.plugin.connect.obmysql;

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

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension; 
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension sessionExtension;
    Connection connection;
    
    @Before
    public void setUp() {
        sessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        sessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).createStatement(any(String.class));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "newSchema";
        when(connection.getCatalog()).thenReturn("oldSchema");
        sessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(connection.getCatalog()).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "12345";
        when(connection.getServerThreadId()).thenReturn(expectedConnectionId);
        String actualConnectionId = sessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
