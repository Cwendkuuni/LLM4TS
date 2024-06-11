package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);

        // Add verification for the execution of "KILL QUERY"
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        when(extension.getCurrentSchema(connection)).thenReturn("default_schema");

        extension.switchSchema(connection, schemaName);

        // Add verification for the setting of the new schema
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // Add mock data for the query result
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");

        assertEquals("test_schema", extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);

        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        // Add mock data for the query result
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("6789");

        assertEquals("6789", extension.getConnectionId(connection));

        // Add additional test case for when the connection is an OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String serverThreadId = "9876";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(serverThreadId);
        assertNotEquals("1234", extension.getConnectionId(oceanBaseConnection));
        assertEquals(serverThreadId, extension.getConnectionId(oceanBaseConnection));
    }
}