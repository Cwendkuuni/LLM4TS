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
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        verify(connection, times(1)).prepareStatement("KILL QUERY " + connectionId).execute();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.switchSchema(connection, schemaName);

        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(expectedSchema);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String expectedConnectionId = "321";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException());

        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(321);

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }

    @Test
    public void testGetConnectionIdWithoutOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "0";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new SQLException());

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }

}