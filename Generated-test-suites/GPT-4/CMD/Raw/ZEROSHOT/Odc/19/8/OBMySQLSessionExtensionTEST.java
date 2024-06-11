
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/*
 * Copyright (c) 2023 OceanBase Corporation.
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
public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension extension;
    private Connection connection;
    private OceanBaseConnection obConnection;

    @Before
    public void setup() {
        extension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
        obConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "connectionId";
        extension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schemaName";
        when(connection.getCatalog()).thenReturn(schemaName);
        extension.switchSchema(connection, schemaName);
        verify(connection, times(1)).getCatalog();
        verify(connection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "schema";
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(schemaName);
        String actualSchema = extension.getCurrentSchema(connection);
        assertEquals(schemaName, actualSchema);
    }

    @Test
    public void testSwitchSchemaWhenSchemasAreDifferent() throws SQLException {
        String schemaName = "schemaName";
        when(connection.getCatalog()).thenReturn(schemaName + "diff");
        extension.switchSchema(connection, schemaName);
        verify(connection, times(1)).getCatalog();
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "connectionId";
        when(connection.queryForObject("SELECT connection_id()", String.class)).thenReturn(connectionId);
        String actualConId = extension.getConnectionId(connection);
        assertEquals(connectionId, actualConId);
    }

    @Test
    public void testGetConnectionIdWhenExceptionIsThrown() {
        String connectionId = "connectionId";
        when(connection.queryForObject("SELECT connection_id()", String.class)).thenThrow(RuntimeException.class);
        when(obConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        String actualConId = extension.getConnectionId(obConnection);
        assertEquals(connectionId, actualConId);
    }
}
