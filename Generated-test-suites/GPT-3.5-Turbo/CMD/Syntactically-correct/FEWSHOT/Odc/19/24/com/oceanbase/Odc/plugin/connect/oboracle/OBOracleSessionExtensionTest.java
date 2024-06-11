package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        sessionExtension.killQuery(connection, connectionId);
        
        verify(connection, times(1)).prepareStatement("KILL QUERY 12345");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        sessionExtension.switchSchema(connection, schemaName);
        
        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA='testSchema'");
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                .thenReturn("schema_name");
        
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        
        assertEquals("schema_name", currentSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class)))
                .thenThrow(new SQLException());

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123456789L);
        
        String connectionId = sessionExtension.getConnectionId(connection);
        
        assertEquals("123456789", connectionId);
    }
}