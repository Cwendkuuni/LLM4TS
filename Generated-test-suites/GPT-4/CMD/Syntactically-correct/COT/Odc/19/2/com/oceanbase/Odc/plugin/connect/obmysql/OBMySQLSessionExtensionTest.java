package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import static org.mockito.Mockito.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {
    OBMySQLSessionExtension obMySQL = new OBMySQLSessionExtension();
    
    // Mock the connection
    Connection connection = mock(Connection.class);
    
    // Method 1: Test: killQuery(Connection connection, String connectionId)
    @Test
    public void testKillQuery() {
        String connectionId = "TestConnection";
        obMySQL.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    // Method 2: Test: switchSchema(Connection connection, String schemaName) 
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TestSchema";
        when(obMySQL.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQL.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(schemaName);
    }
    
    // Method 3: Test: getCurrentSchema(Connection connection)
    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(obMySQL.getCurrentSchema(connection)).thenReturn(querySql);
        assertEquals(querySql, obMySQL.getCurrentSchema(connection));
    }

    // Method 4: Test: getConnectionId(Connection connection)
    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(obMySQL.getConnectionId(connection)).thenReturn(querySql);
        assertEquals(querySql, obMySQL.getConnectionId(connection));
    }
}