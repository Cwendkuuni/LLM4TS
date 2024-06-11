package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {
    
    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        
        sessionExtension.killQuery(connection, "12345");
        
        // Verify that the execute method was called with the correct query
        verify(connection, times(1)).execute("KILL QUERY 12345");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        
        sessionExtension.switchSchema(connection, "test_schema");
        
        // Verify that the setCatalog method was called with the correct schema name
        verify(connection, times(1)).setCatalog("test_schema");
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        // Mock the queryForObject method to return "test_schema"
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        
        assertEquals("test_schema", currentSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        
        // Mock the queryForObject method to return "12345"
        String querySql = "select connection_id()";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn("12345");
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(connection);
        
        assertEquals("12345", connectionId);
    }
    
    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(54321L);
        
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(oceanBaseConnection);
        
        assertEquals("54321", connectionId); // expected to be converted to String
    }
}