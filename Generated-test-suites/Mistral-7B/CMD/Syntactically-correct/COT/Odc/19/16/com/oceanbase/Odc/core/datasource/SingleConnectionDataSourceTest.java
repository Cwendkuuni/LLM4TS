package com.oceanbase.odc.core.datasource.test;

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
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionIgnoreCloseHandler;

/**
 * Test class for SingleConnectionDataSource
 *
 * @author yh263208
 * @date 2023-03-22 10:00
 */
public class SingleConnectionDataSourceTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final int TEST_TIMEOUT = 15 * 1000;

    private Connection connection;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        eventPublisher = mock(EventPublisher.class);
        initializerList = new ArrayList<>();
        dataSource = new SingleConnectionDataSource() {{
            setUsername(TEST_USERNAME);
            setPassword(TEST_PASSWORD);
            setAutoCommit(null);
            setAutoReconnect(true);
            setEventPublisher(eventPublisher);
            setInitializerList(initializerList);
            setTimeOutMillis(TEST_TIMEOUT);
        }};
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        ArgumentCaptor<ConnectionResetEvent> eventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ConnectionResetEvent event = (ConnectionResetEvent) args[0];
            assertNotNull(event);
            eventCaptor.getValue().accept(event);
            return null;
        });
        Connection returnedConnection = dataSource.getConnection();
        assertNotNull(returnedConnection);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        String differentUsername = "differentUsername";
        String differentPassword = "differentPassword";
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.getUsername()).thenReturn(TEST_USERNAME);
        when(connection.getPassword()).thenReturn(TEST_PASSWORD);
        when(connection.getAutoCommit()).thenReturn(null);
        dataSource.getConnection(differentUsername, differentPassword);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(true);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);
        dataSource.getConnection();
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        verify(initializer, times(1)).init(eq(dataSource.getConnection()));
    }

    @Test
    public void testClose() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(newConnectionFromDriver(TEST_USERNAME, TEST_PASSWORD)).thenReturn(connection);
        Connection returnedConnection = dataSource.innerCreateConnection();
        assertSame(connection, returnedConnection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        when(tryLock()).thenReturn(false);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithConnectionProxy() throws SQLException {
        Connection returnedConnection = dataSource.getConnectionProxy(connection);
        assertSame(connection, returnedConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);
        ConnectionIgnoreCloseHandler handler = mock(ConnectionIgnoreCloseHandler.class);
        Proxy proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);
        dataSource.initConnection(proxy);
        verify(handler, times(1)).invoke(any(Object.class), eq(dataSource.getLock()), any(Proxy.class));
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
        verify(dataSource, times(1)).getLogger().debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        verify(connection, times(1)).close();
        verify(dataSource, times(1)).getConnection();
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }
}