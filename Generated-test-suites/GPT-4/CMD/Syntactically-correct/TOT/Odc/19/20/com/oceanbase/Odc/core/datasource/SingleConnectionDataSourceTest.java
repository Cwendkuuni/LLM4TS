package com.oceanbase.odc.core.datasource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource scds;
    private Connection mockConnection;
  
    @Before
    public void setUp() {
        scds = new SingleConnectionDataSource();
        mockConnection = Mockito.mock(Connection.class);
        dataSource.setConnection(mockConnection);
    }

    @Test
    public void testGetConnection_FirstTime() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ConnectionClosed() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnection_WithUsernamePassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        Connection conn = dataSource.getConnection("user", "pass");
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WrongUsernamePassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        dataSource.getConnection("wronguser", "wrongpass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        Connection conn1 = dataSource.getConnection();
        dataSource.resetConnection();
        Connection conn2 = dataSource.getConnection();
        assertNotSame(conn1, conn2);
    }

    @Test
    public void testClose() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        singleDataSource.close();
        verify(mockConnection, times(1)).close();
    }
}