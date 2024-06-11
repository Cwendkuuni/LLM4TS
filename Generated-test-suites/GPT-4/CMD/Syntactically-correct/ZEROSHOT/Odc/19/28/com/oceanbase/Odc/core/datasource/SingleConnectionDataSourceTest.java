
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection mockConnection;

    @Before
    public void setup() throws Exception {
        mockConnection = mock(Connection.class);
        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return mockConnection;
            }
        };
    }

    @After
    public void teardown() throws SQLException {
        if (mockConnection != null) {
            verify(mockConnection, atLeastOnce()).close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetClosedConnection() throws Exception {
        when(mockConnection.isClosed()).thenReturn(true);

        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithCredentials() throws Exception {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        dataSource.setUsername("username");
        dataSource.setPassword("password");
        assertNotNull(dataSource.getConnection("username", "password"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithWrongCredentials() throws Exception {
        dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws Exception {
        dataSource.getConnection();
        dataSource.close();
        verify(mockConnection).close();
    }

    @Test
    public void testIsAutoCommit() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }
}
