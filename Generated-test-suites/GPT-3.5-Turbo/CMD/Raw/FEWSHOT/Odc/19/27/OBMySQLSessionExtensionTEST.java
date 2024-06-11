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
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // Verify that the query to kill the connection was executed successfully
        // You can add further verification based on your requirements
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "test_schema";
        
        // Mock getCurrentSchema method to return a different schema name
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("other_schema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the schema was switched successfully
        // You can add further verification based on your requirements
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        // Mock queryForObject method to return a specific schema name
        when(obMySQLSessionExtension.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        // Mock queryForObject method to return a specific connection ID
        when(obMySQLSessionExtension.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn("54321");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        assertNotNull(connectionId);
        assertEquals("54321", connectionId);
    }
}