package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private EventPublisher mockEventPublisher;

    @Captor
    private ArgumentCaptor<ConnectionResetEvent> eventCaptor;

    @InjectMocks
    private SingleConnectionDataSource dataSource;

    private final ConnectionInitializer initializer = new ConnectionInitializer() {
        @Override
        public void init(Connection connection) throws SQLException {
            // no-op
        }
    };

    @Before
    public void setUp() {
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(mockEventPublisher);
        dataSource.addInitializer(initializer);
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Mockito.when(mockConnection.isClosed()).thenReturn(false);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
        Connection connection = dataSource.getConnection();
        assertSame(mockConnection, connection);
    }

    @Test
    public void testGetConnectionInvalidCredentials() throws SQLException {
        try {
            dataSource.getConnection("admin", "password");
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionNullConnection() throws SQLException {
        Mockito.when(mockConnection.isClosed()).thenReturn(true);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(false);
        dataSource.setConnection(mockConnection);
        try {
            dataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionNullConnectionProxy() throws SQLException {
        Mockito.when(mockConnection.isClosed()).thenReturn(false);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
        dataSource.setConnection(getConnectionProxy(mockConnection));
        Connection connection = dataSource.getConnection();
        assertSame(mockConnection, connection);
    }

    @Test
    public void testGetConnectionNullCredentials() throws SQLException {
        try {
            dataSource.getConnection(null, null);
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionTimeout() throws SQLException, InterruptedException {
        Mockito.when(mockConnection.isClosed()).thenReturn(false);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(false);
        dataSource.setConnection(mockConnection);
        try {
            dataSource.getConnection(10);
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // expected
        }
        Mockito.verifyNoMoreInteractions(mockConnection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = innerCreateConnection();
        dataSource.setConnection(connection);
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddInitializerListFull() {
        for (int i = 0; i < SingleConnectionDataSource.MAX_INITIALIZERS; i++) {
            dataSource.addInitializer(initializer);
        }
        dataSource.addInitializer(initializer);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.close();
        Mockito.verify(mockConnection).close();
    }

    @Test
    public void testConnectionProxyClose() throws SQLException {
        Connection connection = innerCreateConnection();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        dataSource.setConnection(proxy);
        dataSource.close();
        Mockito.verifyNoInteractions(connection);
    }

    @Test
    public void testConnectionProxyIsWrapperFor() {
        Connection connection = innerCreateConnection();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        assertTrue(proxy.isWrapperFor(Connection.class));
    }

    @Test
    public void testConnectionProxyIsClosed() {
        Connection connection = innerCreateConnection();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        assertFalse(proxy.isClosed());
    }

    @Test
    public void testConnectionProxyEquals() {
        Connection connection = innerCreateConnection();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        assertTrue(proxy.equals(proxy));
        assertTrue(proxy.equals(connection));
        assertFalse(proxy.equals(null));
        assertFalse(proxy.equals(new Object()));
    }

    @Test
    public void testConnectionProxyHashCode() {
        Connection connection = innerCreateConnection();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        assertEquals(System.identityHashCode(connection), System.identityHashCode(proxy));
    }

    @Test
    public void testPublishEvent() throws SQLException, InterruptedException {
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
        dataSource.getConnection();
        dataSource.resetConnection();
        Mockito.verify(mockEventPublisher).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();
        assertSame(mockConnection, event.getConnection());
    }

    private Connection innerCreateConnection() throws SQLException {
        return dataSource.innerCreateConnection();
    }

    private Connection getConnectionProxy(Connection connection) {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                new CloseIgnoreInvocationHandler(connection, null));
    }

    private static class ConnectionInitializer {
        public void init(Connection connection) throws SQLException {
            // no-op
        }
    }
}