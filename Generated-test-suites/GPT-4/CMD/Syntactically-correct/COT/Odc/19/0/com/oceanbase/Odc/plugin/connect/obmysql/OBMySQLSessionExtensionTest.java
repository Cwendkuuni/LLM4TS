package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;


public class OBMySQLSessionExtensionTest {
   
    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private Connection connection = mock(Connection.class);
  
    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, "1");
        verify(connection).execute("KILL QUERY 1");
    }
  
    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("schema1");
        obMySQLSessionExtension.switchSchema(connection, "schema2");
        verify(connection).setCatalog("schema2");
    }
  
    @Test
    public void testGetCurrentSchema() {
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(result, "SELECT DATABASE()");
    }
    
    @Test
    public void testGetConnectionId() {
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(result, "select connection_id()");
    }
    
    // Testing the edge cases:
    @Test(expected = NullPointerException.class)
    public void testNullConnection() {
        obMySQLSessionExtension.killQuery(null, "1");
    }
  
    @Test(expected = SQLException.class)
    public void testSQLExceptionInSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenThrow(SQLException.class);
        obMySQLSessionExtension.switchSchema(connection, "schema2");
    }
  
    @Test
    public void testOceanBaseConnection(){
        Connection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        String result = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(result, "1");
    }
  
    @Test(expected = NullPointerException.class)
    public void testNullConnectionId() {
        String connectionId = null;
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }
}