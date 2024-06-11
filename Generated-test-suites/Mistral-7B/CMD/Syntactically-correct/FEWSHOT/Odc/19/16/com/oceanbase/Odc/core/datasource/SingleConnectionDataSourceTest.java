package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test cases for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {
    private static final String URL = "jdbc:test:ds";
    private static final String USER = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final boolean AUTO_COMMIT = true;
    private static final boolean AUTO_RECONNECT = true;
    private static final long TIME_OUT_MILLIS = 10 * 1000;

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private Lock lock;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource(AUTO_RECONNECT);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        eventPublisher = mock(EventPublisher.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        lock = new ReentrantLock();
        dataSource.addInitializer(connectionInitializer);
    }

    @Test
    public void testGetConnection() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        Connection result = dataSource.getConnection();
        assertSame(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
        dataSource.setUsername("invalidUsername");
        dataSource.setPassword("invalidPassword");

        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        dataSource.getConnection();
        dataSource.resetConnection();

        Connection result = dataSource.getConnection();
        assertSame(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWithNoConnection() throws Exception {
        dataSource.resetConnection();
    }

    @Test
    public void testResetConnectionWithEventPublisher() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        dataSource.setEventPublisher(eventPublisher);

        dataSource.getConnection();
        dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> captor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        ConnectionResetEvent event = captor.getValue();
        assertSame(connection, event.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(lock.tryLock(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(false);

        dataSource.getConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Exception {
        Connection closeIgnoreConnection = mock(Connection.class);
        when(closeIgnoreConnection.isClosed()).thenReturn(false);
        when(closeIgnoreConnection.close()).thenThrow(new SQLException("Close failed"));

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(closeIgnoreConnection, lock);
        assertEquals(null, handler.invoke(handler, new Object[] { null }, new Object[0]));
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        when(lock.tryLock(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)).thenReturn(true);

        Connection result = dataSource.getConnectionProxy(connection);
        assertSame(connection, result);
    }

    @Test
    public void testInitConnection() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        dataSource.initConnection(connection);
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        when(eventPublisher.publishEvent(ArgumentMatchers.isA(ConnectionResetEvent.class))).thenReturn(null);

        dataSource.onConnectionReset(connection);
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        when(connectionInitializer.init(connection)).thenReturn(null);

        Connection result = dataSource.innerCreateConnection();
        assertSame(connection, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithConnectionExists() throws Exception {
        dataSource.connection = connection;

        dataSource.innerCreateConnection();
    }

    @Test
    public void testAddInitializer() throws Exception {
        dataSource.addInitializer(connectionInitializer);

        assertTrue(dataSource.initializerList.contains(connectionInitializer));
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullConnectionInitializer() throws Exception {
        dataSource.addInitializer(null);
    }

    @Test
    public void testClose() throws Exception {
        Connection result = dataSource.getConnection();
        dataSource.close();
        assertNull(result);
    }
}

class ArgumentMatchers {
    public static Matcher<Connection> isA(Class<? extends Connection> connectionClass) {
        return ArgumentMatchers.instanceOf(connectionClass);
    }
}