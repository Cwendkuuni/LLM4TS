package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for SingleConnectionDataSource.
 *
 * @author yh263208
 * @date 2023-03-05 10:10
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private List<Throwable> exceptions;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setAutoReconnect(true);
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);
        exceptions = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                exceptions.add(e);
            }
            connection = null;
        }
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (SQLException e) {
                exceptions.add(e);
            }
            dataSource = null;
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());
        assertTrue(connection.isValid(TIME_OUT_MILLIS));
    }

    @Test
    public void testGetConnection_InvalidUsernameOrPassword() throws Exception {
        assertThrows(SQLException.class, () -> dataSource.getConnection("wrong_username", "wrong_password"));
    }

    @Test
    public void testResetConnection() throws Exception {
        connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(connection);
        connection = dataSource.getConnection();
        assertTrue(connection.isValid(TIME_OUT_MILLIS));
    }

    @Test
    public void testAddInitializer() throws Exception {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        try (Connection connection = dataSource.getConnection()) {
            Mockito.verify(initializer).init(connection);
        }
    }

    @Test
    public void testClose() throws Exception {
        connection = dataSource.getConnection();
        dataSource.close();
        assertNull(connection);
        connection = dataSource.getConnection();
        assertTrue(connection.isValid(TIME_OUT_MILLIS));
    }

    @Test
    public void testPrepareConnection() throws Exception {
        connection = dataSource.getConnection();
        dataSource.prepareConnection(connection);
        Boolean autoCommit = dataSource.getAutoCommit();
        assertTrue(autoCommit != null);
        connection.setAutoCommit(false);
        dataSource.prepareConnection(connection);
        assertSame(false, connection.getAutoCommit());
    }

    @Test
    public void testTryLock_Success() throws Exception {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLock_Failed() throws Exception {
        dataSource.setLock(new ReentrantLock());
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        connection = dataSource.getConnection();
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertTrue(proxyConnection.isValid(TIME_OUT_MILLIS));
        assertSame(connection, proxyConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.initConnection(connection);
        Mockito.verify(connection, Mockito.times(dataSource.getInitializerList().size()))
                .createStatement();
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionCreated(connection);
        Mockito.verify(eventPublisher).publishEvent(Mockito.any(ConnectionEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnection_ConnectionFailed() throws Exception {
        Mockito.doThrow(new SQLException("Connection failed"))
                .when(dataSource).newConnectionFromDriver(USERNAME, PASSWORD);
        dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnection_ConnectionOccupied() throws Exception {
        dataSource.setLock(new ReentrantLock());
        dataSource.getConnection();
        dataSource.getConnection();
    }
}