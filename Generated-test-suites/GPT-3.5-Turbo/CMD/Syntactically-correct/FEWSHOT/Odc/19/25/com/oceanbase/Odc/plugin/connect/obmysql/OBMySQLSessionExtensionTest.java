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

        // Verify that the execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        
        obMySQLSessionExtension.switchSchema(connection, "schema2");

        // Verify that setCatalog method is called with the new schema name
        verify(connection, times(1)).setCatalog("schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("1");
        
        assertEquals("1", obMySQLSessionExtension.getConnectionId(connection));
        
        // Check if connection instance is an OceanBaseConnection
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(obConnection.getServerThreadId()).thenReturn(123);
        assertEquals("123", obMySQLSessionExtension.getConnectionId(obConnection));
        
        // Test when an exception is thrown
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }
}