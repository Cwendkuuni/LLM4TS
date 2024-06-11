package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.reentrantlock.ReentrantLock;

/**
 * Test class for {@link SingleConnectionDataSource}
 */
public class SingleConnectionDataSourceTest {
    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final long TIMEOUT_MILLIS = 10 * 1000;
    private Connection connection;
    private Connection getConnectionSpy;
    private EventPublisher eventPublisherSpy;
    private SingleConnectionDataSource singleConnectionDataSource;
    private ReentrantLock lockSpy;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws Exception {
        Class.forName(DRIVER_CLASS);
        getConnectionSpy = mock(Connection.class);
        eventPublisherSpy = mock(EventPublisher.class);
        lockSpy = new ReentrantLock();
        initializationOfSingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(getConnectionSpy.isClosed()).thenReturn(false);
        when(getConnectionSpy.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        singleConnectionDataSource.connection = getConnectionSpy;
        singleConnectionDataSource.lock = lockSpy;

        Connection connection = singleConnectionDataSource.getConnection();
        assertSame(getConnectionSpy, connection);

        when(getConnectionSpy.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException to be thrown.");
        } catch (SQLException e) {
            // expected
        }

        when(getConnectionSpy.isClosed()).thenReturn(true);
        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException to be thrown.");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        String invalidUsername = "testUsernameInvalid";
        String invalidPassword = "testPasswordInvalid";

        when(getConnectionSpy.isClosed()).thenReturn(false);
        when(getConnectionSpy.isValid(TIMEOUT_MILLIS)).thenReturn(true);
        singleConnectionDataSource.connection = getConnectionSpy;
        singleConnectionDataSource.lock = lockSpy;

        try {
            singleConnectionDataSource.getConnection(invalidUsername, invalidPassword);
            fail("Expected SQLException to be thrown.");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.connection = getConnectionSpy;
        singleConnectionDataSource.lock = lockSpy;

        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.connection);

        Connection newConnection = newConnectionFromDriver(USERNAME, PASSWORD);
        singleConnectionDataSource.connection = newConnection;
        singleConnectionDataSource.lock = lockSpy;

        onConnectionReset(newConnection);
        verify(eventPublisherSpy).publishEvent(new ConnectionResetEvent(newConnection));
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertTrue(singleConnectionDataSource.initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.connection = getConnectionSpy;
        singleConnectionDataSource.lock = lockSpy;

        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);
        boolean autoCommit = true;

        singleConnectionDataSource.prepareConnection(connection);
        assertTrue(connection.getAutoCommit() == autoCommit);
    }

    @Test
    public void testTryLock() throws InterruptedException {
        assertTrue(singleConnectionDataSource.lock.tryLock(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));

        try {
            singleConnectionDataSource.lock.tryLock(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            fail("Expected InterruptedException to be thrown.");
        } catch (InterruptedException e) {
            // expected
        }

        boolean caughtInterruptedException = false;
        try {
            singleConnectionDataSource.lock.tryLock();
            Thread.interrupted();
        } catch (InterruptedException e) {
            caughtInterruptedException = true;
        }
        assertTrue(caughtInterruptedException);
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);

        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.initConnection(connection);
        verify(connectionInitializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);

        singleConnectionDataSource.onConnectionCreated(connection);
        verify(log).debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);

        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisherSpy).publishEvent(new ConnectionResetEvent(connection));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);

        Connection newConnection = singleConnectionDataSource.innerCreateConnection();
        assertSame(connection, newConnection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Exception {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);
        Connection getConnectionSpy = mock(Connection.class);
        ReentrantLock lock = new ReentrantLock();

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);

        Object result = handler.invoke(handler, new Object[0][], new Object[0][], new Object[0][], new int[0], new Object[0][], new Object[0][], new Object[0][], new Throwable[0][], new int[0]);
        assertNull(result);
    }

    private void initializationOfSingleConnectionDataSource() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setAutoReconnect(true);
        singleConnectionDataSource.setEventPublisher(eventPublisherSpy);
        singleConnectionDataSource.initializerList = new ArrayList<>();
        singleConnectionDataSource.lock = lockSpy;
    }

    private Connection newConnectionFromDriver(String username, String password) throws SQLException {
        return DriverManager.getConnection(URL, username, password);
    }

    private void onConnectionReset(Connection connection) {
        if (eventPublisherSpy != null) {
            try {
                singleConnectionDataSource.onConnectionReset(connection);
            } catch (Exception e) {
                log.warn("Failed to publish event", e);
            }
        }
    }
}