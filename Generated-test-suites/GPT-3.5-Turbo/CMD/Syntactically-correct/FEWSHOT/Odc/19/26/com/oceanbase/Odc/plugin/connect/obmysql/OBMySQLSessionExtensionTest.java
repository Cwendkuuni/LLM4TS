package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        String connectionId = "12345";
        sessionExtension.killQuery(connection, connectionId);

        verify(connection, times(1)).prepareStatement("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn("test_schema");

        String currentSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId_success() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn("7890");

        String connectionId = sessionExtension.getConnectionId(connection);

        assertEquals("7890", connectionId);
    }

    @Test
    public void testGetConnectionId_fail() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new RuntimeException());

        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1234);

        String connectionId = sessionExtension.getConnectionId(connection);

        assertEquals("1234", connectionId);
    }
}