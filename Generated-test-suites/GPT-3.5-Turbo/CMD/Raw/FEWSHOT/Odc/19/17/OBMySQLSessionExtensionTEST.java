package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        obMySQLSessionExtension.killQuery(connection, "1");
        
        verify(connection, times(1)).createStatement();
        verify(connection.createStatement(), times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        obMySQLSessionExtension.switchSchema(connection, "test_schema");
        
        verify(connection, times(1)).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithoutException() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("123");
        
        assertEquals("123", obMySQLSessionExtension.getConnectionId(connection));
    }
}