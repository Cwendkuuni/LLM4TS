package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.test.util.TestConnection;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @date 2021-11-08 16:20
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final long TIMEOUT_MILLIS = 10 * 1000;
    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;
    private Lock lock;

    @Before
    public void setup() {
        this.dataSource = new SingleConnectionDataSource();
        this.connection = mock(Connection.class);
        this.connectionInitializer = mock(ConnectionInitializer.class);
        this.eventPublisher = mock(EventPublisher.class);
        this.lock = new ReentrantLock();
        this.dataSource.addInitializer(this.connectionInitializer);
    }

    @Test
    public void testGetConnection_nullAutoCommit_returnsConnectionWithDefaultAutoCommit() throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(false);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);

        Connection result = this.dataSource.getConnection();
        assertFalse(result.getAutoCommit());
    }

    @Test
    public void testGetConnection_validConnection_returnsConnectionProxy() throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(null);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);

        Connection result = this.dataSource.getConnection();
        assertSame(this.connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_invalidConnection_throwsSQLException() throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(null);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(false);

        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_invalidUsernameOrPassword_throwsSQLException() throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(null);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);

        this.dataSource.setUsername(USERNAME);
        this.dataSource.setPassword(PASSWORD);

        this.dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection_nullConnection_doesNotThrowException() throws SQLException {
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testResetConnection_validConnection_closesConnectionAndInitializesANewOne()
            throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(null);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);

        Connection newConnection = newTestConnection();
        Connection newConnectionProxy = this.dataSource.getConnectionProxy(newConnection);

        this.dataSource.resetConnection();
        Connection result = this.dataSource.getConnection();
        assertSame(newConnectionProxy, result);
    }

    @Test(expected = ConflictException.class)
    public void testResetConnection_anotherThreadOccupiesConnection_throwsConflictException()
            throws SQLException {
        when(this.connection.getAutoCommit()).thenReturn(null);
        when(this.connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);

        this.dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> eventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(this.eventPublisher).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();

        this.dataSource.getConnection(); // Another thread acquires the connection

        this.dataSource.resetConnection();
    }

    @Test
    public void testAddInitializer_addsConnectionInitializerToList() {
        this.dataSource.addInitializer(this.connectionInitializer);
        List<ConnectionInitializer> initializers = this.dataSource.getInitializerList();
        assertEquals(1, initializers.size());
        assertTrue(initializers.contains(this.connectionInitializer));
    }

    @Test
    public void testClose_releasesConnection() throws SQLException {
        when(this.connection.isClosed()).thenReturn(false);

        this.dataSource.getConnection();
        this.dataSource.close();

        Connection result = this.dataSource.getConnection();
        assertNull(result);
    }

    @Test
    public void testPrepareConnection_setsAutoCommit() throws SQLException {
        Connection connection = newTestConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);

        this.dataSource.prepareConnection(connectionProxy);

        boolean autoCommit = connectionProxy.getAutoCommit();
        assertTrue(autoCommit);
    }

    @Test
    public void testTryLock_acquiresLockIfAvailable() throws InterruptedException {
        assertTrue(this.lock.tryLock(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testTryLock_doesNotAcquireLockIfUnavailable() throws InterruptedException {
        assertFalse(this.lock.tryLock(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnection_anotherThreadOccupiesLock_throwsConflictException()
            throws SQLException, InterruptedException {
        this.lock.lock();

        Thread otherThread = new Thread(() -> {
            this.dataSource.getConnection();
        });

        otherThread.start();
        otherThread.join();

        this.dataSource.getConnection();
    }

    @Test
    public void testInitConnection_initializesConnection() throws SQLException {
        Connection connection = newTestConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);

        this.dataSource.initConnection(connectionProxy);

        verify(this.connectionInitializer).init(connectionProxy);
    }

    @Test
    public void testOnConnectionCreated_logsMessage() throws SQLException {
        Connection connection = newTestConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);

        this.dataSource.onConnectionCreated(connectionProxy);

        verify(this.log).debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset_publishesEvent() throws SQLException {
        Connection connection = newTestConnection();
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);

        this.dataSource.onConnectionReset(connectionProxy);

        ArgumentCaptor<ConnectionResetEvent> eventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(this.eventPublisher).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();
        assertSame(connectionProxy, event.getConnection());
    }

    @Test
    public void testInnerCreateConnection_initializesConnection() throws SQLException {
        Connection connection = newTestConnection();

        Connection result = this.dataSource.innerCreateConnection();

        assertSame(connection, result);
    }

    private Connection newTestConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        when(connection.getAutoCommit()).thenReturn(null);
        when(connection.close()).thenReturn(null);
        when(newConnectionFromDriver(USERNAME, PASSWORD)).thenReturn(connection);
        return connection;
    }
}