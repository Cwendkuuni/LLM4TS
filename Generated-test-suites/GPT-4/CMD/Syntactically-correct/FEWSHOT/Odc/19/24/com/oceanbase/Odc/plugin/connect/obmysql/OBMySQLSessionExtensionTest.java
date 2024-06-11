package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    @Mock
    private Connection connection;

    private OBMySQLSessionExtension extension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        extension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1001");
        verify(connection, times(1)).execute("KILL QUERY 1001");
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("testSchema");
        extension.switchSchema(connection, "testSchema");
        verify(connection, times(0)).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("oldSchema");
        extension.switchSchema(connection, "newSchema");
        verify(connection, times(1)).setCatalog("newSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        extension.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject("SELECT DATABASE()", String.class);
    }

    @Test
    public void testGetConnectionId() {
        extension.getConnectionId(connection);
        verify(connection, times(1)).queryForObject("select connection_id()", String.class);
    }
}