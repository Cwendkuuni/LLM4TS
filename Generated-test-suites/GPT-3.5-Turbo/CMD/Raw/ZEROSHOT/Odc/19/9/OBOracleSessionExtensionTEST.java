package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "123");
        // Verify that the execute method is called with the expected query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        
        when(extension.getCurrentSchema(connection)).thenReturn("schema1");
        
        extension.switchSchema(connection, "schema1");
        // Verify that the update method is not called if the schemas are equal
        verify(JdbcOperationsUtil.getJdbcOperations(connection), never()).update(anyString());
        
        extension.switchSchema(connection, "schema2");
        // Verify that the update method is called with the expected query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA='schema2'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("schema1");
        
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("schema1", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), eq(String.class))).thenReturn("123");
        
        String connectionId = extension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }
}