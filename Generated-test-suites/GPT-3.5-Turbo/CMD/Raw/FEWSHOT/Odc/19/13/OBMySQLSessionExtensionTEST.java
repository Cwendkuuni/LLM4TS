package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "123";

        sessionExtension.killQuery(connection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "test_schema";

        when(sessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");

        sessionExtension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String expectedConnectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String expectedConnectionId = "456";

        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedConnectionId));

        String actualConnectionId = sessionExtension.getConnectionId(oceanBaseConnection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}