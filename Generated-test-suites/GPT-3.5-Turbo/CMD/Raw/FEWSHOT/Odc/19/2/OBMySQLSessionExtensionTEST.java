package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {
    
    private Connection mockConnection;

    @Before
    public void setUp() {
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(mockConnection, "123");
        
        verify(mockConnection, times(1)).prepareStatement(eq("KILL QUERY 123"));
        verify(mockConnection, times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(obMySQLSessionExtension.getCurrentSchema(eq(mockConnection))).thenReturn("public");
        
        obMySQLSessionExtension.switchSchema(mockConnection, "test");
        
        verify(mockConnection, times(1)).setSchema(eq("test"));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(eq(mockConnection)).queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn("test");
        
        assertEquals("test", obMySQLSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        
        when(JdbcOperationsUtil.getJdbcOperations(eq(mockConnection)).queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn("12345");
        
        assertEquals("12345", obMySQLSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockOceanBaseConnection.getServerThreadId()).thenReturn(54321);
        
        assertNull(obMySQLSessionExtension.getConnectionId(mockOceanBaseConnection));
    }
}