package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(mockConnection, "123");
        verify(mockConnection, times(1)).prepareStatement("KILL QUERY 123").execute();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("default");
        obMySQLSessionExtension.switchSchema(mockConnection, "newSchema");
        verify(mockConnection, times(1)).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("testSchema");
        String result = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("testSchema", result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(mockConnection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(mockConnection.unwrap(OceanBaseConnection.class)).thenReturn(obConnection);
        when(obConnection.getServerThreadId()).thenReturn(456L);
        String result = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("456", result);
    }
}