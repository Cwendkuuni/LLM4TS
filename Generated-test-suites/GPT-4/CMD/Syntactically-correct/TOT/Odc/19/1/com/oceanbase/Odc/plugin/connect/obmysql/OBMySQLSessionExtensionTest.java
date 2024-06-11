package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension ob = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String connectionId = "12345";
        ob.killQuery(mockConnection, connectionId);
    }

    @Test
    public void testSwitchSchema_sameSchemaDoesNothing() throws SQLException {
        OBMySQLSessionExtension ob = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String schemaName = "myschema";
        when(ob.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        
        ob.switchSchema(mockConnection, schemaName);
        
        verify(mockConnection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_differentSchemaSwitches() throws SQLException {
        OBMySQLSessionExtension ob = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String schemaName = "myschema";
        String otherSchema = "otherschema";
        when(ob.getCurrentSchema(mockConnection)).thenReturn(otherSchema);
        
        ob.switchSchema(mockConnection, schemaName);
        
        verify(mockConnection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension ob = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        ob.getCurrentSchema(mockConnection);
    }
    
    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension ob = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        ob.getConnectionId(mockConnection);
    }
}