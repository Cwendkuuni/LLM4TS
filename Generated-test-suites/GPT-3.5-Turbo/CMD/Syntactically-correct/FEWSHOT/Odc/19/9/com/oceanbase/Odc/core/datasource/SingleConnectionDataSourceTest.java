package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testAutoReconnect() {
        assertEquals(true, dataSource.isAutoReconnect());
    }

    @Test
    public void testGetAutoCommit() {
        dataSource.setAutoCommit(true);
        assertEquals(true, dataSource.getAutoCommit());
    }

    @Test
    public void testSetAutoCommit() {
        dataSource.setAutoCommit(false);
        assertEquals(false, dataSource.getAutoCommit());
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "user";
        String password = "password";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        String username = "invalidUser";
        String password = "invalidPassword";
        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();
        assertNotNull(dataSource.getConnectionProxy(connection, lock));
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
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class},
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock));
        Field field = proxy.getClass().getDeclaredField("lock");
        field.setAccessible(true);
        Lock actualLock = (Lock) field.get(proxy);
        assertEquals(lock, actualLock);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithTimeout() {
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();
        when(lock.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(false);
        dataSource.getConnectionProxy(connection, lock);
    }
}