package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        
        // Verify that the execute method is called with the correct query
        // You can use a mocking framework like Mockito to verify this
        
        // Add assertion here if needed after verifying the behavior
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        
        // Verify that the setCatalog method is called with the correct schemaName
        // You can use a mocking framework like Mockito to verify this
        
        // Add assertion here if needed after verifying the behavior
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String currentSchema = "test_schema";
        
        // Mock the queryForObject method
        // when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getCurrentSchema(connection);
        
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        
        // Mock the queryForObject method
        // when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String result = extension.getConnectionId(connection);
        
        assertEquals(connectionId, result);
    }
}