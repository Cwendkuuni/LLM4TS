package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "1234";
        
        // Test normal case
        sessionExtension.killQuery(connection, connectionId);
        
        // Add more test cases if needed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "test_schema";
        
        // Test normal case
        sessionExtension.switchSchema(connection, schemaName);
        
        // Add more test cases if needed
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        
        // Test normal case
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
        
        // Add more test cases if needed
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        
        // Test normal case
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        
        // Test case when exception is thrown
        // Add more test cases if needed
    }
}