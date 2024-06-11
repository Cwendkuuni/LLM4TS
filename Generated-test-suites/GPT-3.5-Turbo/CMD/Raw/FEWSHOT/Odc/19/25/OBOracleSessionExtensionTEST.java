package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {
    
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    
    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Verify that the execute method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        // Mocking getCurrentSchema method to return null
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(null);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Verify that the update method is called with the correct SQL query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=test_schema");
    }
    
    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "test_schema";
        // Mocking queryForObject method to return the expected schema
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, currentSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "456";
        // Mocking queryForObject method to return the expected connection id
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn(expectedConnectionId);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(expectedConnectionId, connectionId);
        
        // Mocking queryForObject method to throw an exception and checking the alternate flow
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(789L);
        
        connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("789", connectionId);
    }

}