package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

@SuppressWarnings("unchecked")
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerListMock;
    private Lock lockMock;
    private ConnectionInitializer initializerMock;

    @Before
    public void setUp() {
        this.connectionMock = mock(Connection.class);
        this.eventPublisherMock = mock(EventPublisher.class);
        this.initializerListMock = mock(List.class);
        this.initializerMock = mock(ConnectionInitializer.class);
        this.lockMock = mock(ReentrantLock.class);

        this.dataSource = new SingleConnectionDataSource(true);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
        this.dataSource.setInitializerList(this.initializerListMock);
        this.dataSource.setLock(this.lockMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.initializerListMock.isEmpty()).thenReturn(false);
        when(this.initializerListMock.get(0)).thenReturn(this.initializerMock);
        when(this.initializerMock.init(this.connectionMock)).thenReturn(null);

        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertSame(this.connectionMock, connection);

        verify(this.initializerMock).init(this.connectionMock);
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);

        assertThrows(SQLException.class, () -> this.dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testGetConnectionWithNullInitializerList() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.initializerListMock.isEmpty()).thenReturn(true);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertSame(this.connectionMock, connection);

        verify(this.initializerListMock, never()).get(0);
    }

    @Test
    public void testGetConnectionWithEmptyInitializerList() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.initializerListMock.isEmpty()).thenReturn(true);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertSame(this.connectionMock, connection);

        verify(this.initializerListMock, never()).get(0);
    }

    @Test
    public void testGetConnectionWithNullLock() throws SQLException {
        assertThrows(NullPointerException.class, () -> new SingleConnectionDataSource(true, null));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(false);

        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testResetConnectionWithClosedConnection() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(true);

        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testResetConnectionWithExceptionDuringConnectionCreation() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.connectionMock.isClosed()).thenReturn(false);
        when(this.connectionMock.close()).thenThrow(new SQLException("Test exception"));

        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.initializerListMock.isEmpty()).thenReturn(true);

        this.dataSource.addInitializer(this.initializerMock);
        verify(this.initializerListMock).add(this.initializerMock);
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullInitializer() {
        this.dataSource.addInitializer(null);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());

        verify(this.connectionMock).close();
    }

    @Test
    public void testCloseTwice() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        this.dataSource.close();
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());

        verify(this.connectionMock, times(1)).close();
    }

    @Test
    public void testCloseWithConnectionThatIsAlreadyClosed() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        this.dataSource.close();
        this.dataSource.getConnection();
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());

        verify(this.connectionMock, times(1)).close();
    }

    @Test
    public void testCloseWithNullConnection() {
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());

        verify(this.connectionMock, never()).close();
    }

    @Test
    public void testCloseWithConnectionThatIsNotClosed() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        this.dataSource.close();
        Connection connection2 = this.dataSource.getConnection();

        verify(this.connectionMock, times(1)).close();
        assertSame(connection2, connection);
    }

    @Test
    public void testPublishEvent() throws SQLException {
        ConnectionResetEvent event = new ConnectionResetEvent(this.connectionMock);

        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);

        this.dataSource.publishEvent(event);
        verify(this.eventPublisherMock).publishEvent(event);
    }

    @Test
    public void testPublishEventWithNullEventPublisher() {
        ConnectionResetEvent event = new ConnectionResetEvent(this.connectionMock);

        this.dataSource.setEventPublisher(null);

        this.dataSource.publishEvent(event);
        verify(this.eventPublisherMock, never()).publishEvent(event);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);

        Connection connection = this.dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertSame(this.connectionMock, connection);
    }

    @Test
    public void testInnerCreateConnectionWithExceptionDuringConnectionCreation() throws SQLException {
        when(this.lockMock.tryLock(10, TimeUnit.SECONDS)).thenReturn(true);
        when(this.connectionMock.close()).thenThrow(new SQLException("Test exception"));

        assertThrows(SQLException.class, () -> this.dataSource.innerCreateConnection());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);

        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testGetConnectionProxyWithNullConnection() {
        Connection connection = null;

        assertNull(this.dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testTryLock() throws InterruptedException {
        ArgumentCaptor<Long> timeoutArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> timeUnitArgumentCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        this.dataSource.tryLock();
        verify(this.lockMock).tryLock(timeoutArgumentCaptor.capture(), timeUnitArgumentCaptor.capture());

        long timeout = timeoutArgumentCaptor.getValue();
        TimeUnit timeUnit = timeUnitArgumentCaptor.getValue();

        assertTrue(timeUnit.toMillis(timeout) >= 10000);
    }

    @Test
    public void testTryLockWithNullLock() {
        assertThrows(NullPointerException.class, () -> this.dataSource.tryLock());
    }

}