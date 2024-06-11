package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.baseclass.BaseClassBasedDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private Connection connection;
    private Connection initialConnection;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        initialConnection = mock(Connection.class);
        connection = mock(Connection.class);
        eventPublisher = mock(EventPublisher.class);
        initializerList = new LinkedList<>();

        when(initialConnection.isValid(any(Integer.class))).thenReturn(true);

        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connection;
            }

            @Override
            protected void onConnectionCreated(Connection connection) {
                super.onConnectionCreated(connection);
                verify(eventPublisher).publishEvent(any(ConnectionCreatedEvent.class));
            }
        };

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                try {
                    connection.createStatement().execute("CREATE TABLE test (id INT PRIMARY KEY)");;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @After
    public void tearDown() {
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(dataSource.connection).thenReturn(null);
        when(dataSource.lock).thenReturn(new ReentrantLock());

        Connection connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());

        verify(dataSource).innerCreateConnection();
        verify(dataSource, Mockito.times(1)).getConnectionProxy(any(Connection.class));
        verify(dataSource, Mockito.times(1)).initConnection(connection);
        verify(dataSource, Mockito.times(1)).prepareConnection(connection);
        verify(dataSource, Mockito.times(1)).onConnectionCreated(connection);

        dataSource.connection = initialConnection;
        Connection connectionFromCache = dataSource.getConnection();
        assertSame(initialConnection, connectionFromCache);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(dataSource.connection).thenReturn(initialConnection);
        when(initialConnection.isValid(any(Integer.class))).thenReturn(false);

        dataSource.getConnection();
        fail();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullConnection() throws SQLException {
        when(dataSource.connection).thenReturn(null);

        dataSource.getConnection();
        fail();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithLockTimeout() throws SQLException, InterruptedException {
        when(dataSource.connection).thenReturn(null);
        when(dataSource.lock).thenReturn(new ReentrantLock());

        dataSource.getConnection();
        Thread.sleep(dataSource.timeOutMillis + 10);

        dataSource.getConnection();
        ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(dataSource).publishEvent(Mockito.argThat(event -> event instanceof ConnectionResetEvent &&
                errorMessageArgumentCaptor.getValue().contains("Connection is occupied, waited " + dataSource.timeOutMillis + " millis")));
    }

    @Test
    public void testGetConnectionWithAutoReconnect() throws SQLException, InterruptedException {
        when(dataSource.connection).thenReturn(initialConnection);
        when(initialConnection.isValid(any(Integer.class))).thenReturn(false);
        when(dataSource.lock).thenReturn(new ReentrantLock());

        dataSource.getConnection();
        Thread.sleep(dataSource.timeOutMillis + 10);

        Connection newConnection = mock(Connection.class);
        when(newConnection.isValid(any(Integer.class))).thenReturn(true);
        when(dataSource.innerCreateConnection()).thenReturn(newConnection);

        Connection connectionFromCache = dataSource.getConnection();
        assertSame(newConnection, connectionFromCache);

        verify(dataSource).publishEvent(Mockito.argThat(event -> event instanceof ConnectionCreatedEvent));
        verify(dataSource).publishEvent(Mockito.argThat(event -> event instanceof ConnectionResetEvent &&
                event.getMessage().contains("The connection will be reset soon")));
    }

    @Test
    public void testGetConnectionWithAutoCommit() throws SQLException {
        Connection connectionWithAutoCommit = mock(Connection.class);
        when(connectionWithAutoCommit.getAutoCommit()).thenReturn(true);

        dataSource.autoCommit = true;
        Connection connection = dataSource.getConnection();
        assertSame(connectionWithAutoCommit, connection);
        assertFalse(connection.getAutoCommit()); // JDBC driver might change the autocommit flag
        assertTrue(dataSource.autoCommit);
    }

    @Test
    public void testGetConnectionWithNoAutoCommit() throws SQLException {
        Connection connectionWithoutAutoCommit = mock(Connection.class);
        when(connectionWithoutAutoCommit.getAutoCommit()).thenReturn(false);

        dataSource.autoCommit = false;
        Connection connection = dataSource.getConnection();
        assertSame(connectionWithoutAutoCommit, connection);
        assertFalse(connection.getAutoCommit());
        assertFalse(dataSource.autoCommit);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");

        try {
            dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals(ErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorCode(), e.getErrorCode());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(dataSource.connection).thenReturn(initialConnection);

        dataSource.resetConnection();
        verify(dataSource).close();
        verify(dataSource).onConnectionReset(initialConnection);
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWithNullConnection() throws SQLException {
        dataSource.connection = null;

        try {
            dataSource.resetConnection();
            fail();
        } catch (SQLException e) {}
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);

        verify(dataSource).initializerList.add(initializer);
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.initConnection(initialConnection);

        verify(initialConnection).createStatement();
    }

    @Test
    public void testPublishEvent() throws SQLException {
        dataSource.publishEvent(new ConnectionCreatedEvent(initialConnection));
        verify(eventPublisher).publishEvent(any(ConnectionCreatedEvent.class));

        dataSource.publishEvent(new ConnectionResetEvent(initialConnection));
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }
}