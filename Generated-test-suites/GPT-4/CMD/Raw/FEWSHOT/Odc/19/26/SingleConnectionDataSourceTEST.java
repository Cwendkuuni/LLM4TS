
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.connection = connection;
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection result = dataSource.getConnection();

        assertNotNull(result);
        assertTrue(dataSource.tryLock());
        verify(connection, times(1)).isClosed();
        verify(connection, times(1)).isValid(anyInt());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenClosed() throws SQLException {
        dataSource.connection = connection;
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsername() throws SQLException {
        dataSource.setUsername("user1");
        dataSource.setPassword("password1");

        dataSource.getConnection("user2", "password2");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.connection = connection;

        dataSource.resetConnection();

        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
        verify(connection, times(1)).close();
    }

    @Test
    public void testClose() {
        dataSource.connection = connection;

        dataSource.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        when(connection.getAutoCommit()).thenReturn(false);
        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test(expected = ConflictException.class)
    public void testTryLock() {
        dataSource.lock = mock(Lock.class);
        when(dataSource.lock.tryLock(anyLong(), any())).thenReturn(false);

        dataSource.tryLock();
    }
}
