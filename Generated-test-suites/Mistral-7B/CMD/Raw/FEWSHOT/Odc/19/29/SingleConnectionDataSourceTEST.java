package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.StubEventPublisher;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final long TIME_OUT_MILLIS = 5000;
    private static final boolean AUTO_COMMIT = false;
    private static final boolean AUTO_RECONNECT = true;
    private static final boolean RESET_CONNECTION = false;
    private EventPublisher eventPublisher;
    private Connection connection;
    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private List<ConnectionInitializer> initializerList;
    private StubEventPublisher stubEventPublisher;

    @Before
    public void setUp() {
        eventPublisher = Mockito.mock(EventPublisher.class);
        connection = Mockito.mock(Connection.class);
        connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        initializerList = new ArrayList<>();
        initializerList.add(connectionInitializer);
        dataSource = new SingleConnectionDataSource() {
            @Override
            public EventPublisher getEventPublisher() {
                return eventPublisher;
            }
        };
        dataSource.setAutoReconnect(AUTO_RECONNECT);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setAutoCommit(AUTO_COMMIT);
        dataSource.setInitializerList(initializerList);
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);
        stubEventPublisher = new StubEventPublisher();
        dataSource.setEventPublisher(stubEventPublisher);
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionInitializer.init(any(Connection.class))).thenReturn(null);

        dataSource.addInitializer(connectionInitializer);
        dataSource.addInitializer(connectionInitializer);

        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenThrow(new RuntimeException());

        dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        ConnectionResetEvent event = argumentCaptor.getValue();
        assertTrue(event.isConnectionReset());
    }

    @Test
    public void testGetConnection_InvalidUsernameOrPassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testGetConnection_InitializedConnection() throws SQLException {
        when(connectionInitializer.init(any(Connection.class))).thenReturn(null);

        dataSource.addInitializer(connectionInitializer);
        dataSource.addInitializer(connectionInitializer);

        Connection connection = Mockito.mock(Connection.class);
        when(newConnectionFromDriver(USERNAME, PASSWORD)).thenReturn(connection);

        Connection connectionFromDataSource = dataSource.getConnection();
        assertEquals(connection, connectionFromDataSource);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);

        dataSource.addInitializer(connectionInitializer);

        verify(connectionInitializer, times(1)).init(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenCallRealMethod();

        Connection connection = Mockito.mock(Connection.class);
        when(newConnectionFromDriver(USERNAME, PASSWORD)).thenReturn(connection);

        Connection connectionFromDataSource = dataSource.innerCreateConnection();
        assertEquals(connection, connectionFromDataSource);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(connection.isValid(TIME_OUT_MILLIS)).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);

        assertEquals(connection, connectionProxy);
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenCallRealMethod();

        Connection connection = Mockito.mock(Connection.class);
        when(newConnectionFromDriver(USERNAME, PASSWORD)).thenReturn(connection);

        dataSource.resetConnection();

        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testTryLock() {
        Lock lock = new ReentrantLock();

        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLock_FailedToAcquireLock() {
        Lock lock = new ReentrantLock();

        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);

        dataSource.initConnection(connection);

        verify(connectionInitializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.onConnectionCreated(connection);

        verify(log, times(1)).debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.onConnectionReset(connection);

        if (eventPublisher instanceof StubEventPublisher) {
            StubEventPublisher stubEventPublisher = (StubEventPublisher) eventPublisher;
            assertEquals(1, stubEventPublisher.getPublishedEventCount());
        } else {
            fail("Unexpected event publisher: " + eventPublisher.getClass().getName());
        }
    }
}

class StubEventPublisher implements EventPublisher {
    private int publishedEventCount = 0;

    @Override
    public void publishEvent(Object event) {
        publishedEventCount++;
    }

    public int getPublishedEventCount() {
        return publishedEventCount;
    }
}