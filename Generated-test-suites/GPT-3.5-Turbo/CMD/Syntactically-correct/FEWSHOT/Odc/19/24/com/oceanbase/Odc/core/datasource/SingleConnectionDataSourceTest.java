package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection originalConnection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNotEquals(originalConnection, dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyOccupied() {
        Connection connection = datasource.getConnection();
        datasource.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setEventPublisher(mock(EventPublisher.class));
        dataSource.onConnectionReset(mock(Connection.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        Connection target = mock(Connection.class);
        Lock lock = mock(Lock.class);
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(target, lock);
        Method method = target.getClass().getMethod("close");
        assertNull(handler.invoke(mock(Object.class), method, null));
    }
}