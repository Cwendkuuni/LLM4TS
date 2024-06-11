package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
  
    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        extension.killQuery(connection, "123");
        
        // Add assertions
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        extension.switchSchema(connection, "test_schema");
        
        // Add assertions
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        String currentSchema = extension.getCurrentSchema(connection);
        
        // Add assertions
    }
    
    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        
        String connectionId = extension.getConnectionId(connection);
        
        // Add assertions
    }
    
}