package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connectionMock;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
        
        verify(connectionMock, times(1)).createStatement();
        verify(connectionMock.createStatement(), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        
        when(obMySQLSessionExtension.getCurrentSchema(connectionMock)).thenReturn("original_schema");
        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
        
        verify(connectionMock, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String expectedSchema = "test_schema";
        when(obMySQLSessionExtension.getCurrentSchema(connectionMock)).thenReturn(expectedSchema);
        
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connectionMock);
        
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "54321";
        when(obMySQLSessionExtension.getConnectionId(connectionMock)).thenReturn(expectedConnectionId);
        
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connectionMock);
        
        assertEquals(expectedConnectionId, actualConnectionId);
    }

}