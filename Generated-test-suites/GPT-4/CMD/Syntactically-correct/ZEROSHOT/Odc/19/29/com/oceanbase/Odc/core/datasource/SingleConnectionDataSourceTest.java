
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = spy(new SingleConnectionDataSource());
    }

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        Connection actualConnection = dataSource.getConnection();
        verify(dataSource, times(1)).getConnection();
        assertEquals(connection, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenClosed() throws Exception {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
        dataSource.getConnection("invalidusername", "invalidpassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        dataSource.resetConnection();
        verify(dataSource, times(1)).resetConnection();
    }

    @Test
    public void testClose() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.connection = connection;
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPreparationOfConnection() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.autoCommit = true;
        when(connection.getAutoCommit()).thenReturn(false);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetConnectionProxyWhenTryLockIsFalse() throws Exception {
        Connection connection = mock(Connection.class);
        when(dataSource.tryLock()).thenReturn(false);
        dataSource.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws Exception {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @After
    public void tearDown() {
        dataSource = null;
    }
}
