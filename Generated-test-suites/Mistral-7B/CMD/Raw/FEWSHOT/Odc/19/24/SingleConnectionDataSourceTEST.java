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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import lombok.extern.slf4j.Slf4j;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;

@Slf4j
public class SingleConnectionDataSourceTest {

    private Connection driverConnection;
    private DriverManager driverManager;
    private SingleConnectionDataSource singleConnectionDataSource;
    private EventPublisher eventPublisherMock;
    private ConnectionInitializer connectionInitializerMock;

    @Before
    public void setUp() throws Exception {
        driverManager = mock(DriverManager.class);
        when(driverManager.getConnection(DriverManager.getDefaultConnectionProperties())).thenReturn(driverConnection);

        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setAutoReconnect(true);
        singleConnectionDataSource.setEventPublisher(eventPublisherMock = mock(EventPublisher.class));

        connectionInitializerMock = mock(ConnectionInitializer.class);
    }

    @Test
    public void testGetConnection_InitiallyNullConnection() throws SQLException {
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnection_ConnectionReset() throws SQLException {
        singleConnectionDataSource.connection = mock(Connection.class);
        when(singleConnectionDataSource.connection.isClosed()).thenReturn(true);
        when(singleConnectionDataSource.connection.isValid(singleConnectionDataSource.getLoginTimeout())).thenReturn(false);

        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // EXPECTED
        }

        singleConnectionDataSource.resetConnection();
        Connection resetConnection = singleConnectionDataSource.getConnection();
        assertSame(driverConnection, resetConnection);
    }

    @Test
    public void testGetConnection_ConnectionInitializers() throws SQLException, InstantiationException, IllegalAccessException {
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        initializerList.add(connectionInitializerMock);
        singleConnectionDataSource.setInitializerList(initializerList);

        singleConnectionDataSource.connection = mock(Connection.class);
        singleConnectionDataSource.prepareConnection(singleConnectionDataSource.connection);

        verify(connectionInitializerMock).init(singleConnectionDataSource.connection);
    }

    @Test
    public void testAddInitializer() throws Exception {
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        assertTrue(initializerList.contains(connectionInitializerMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInitializerNull() throws Exception {
        singleConnectionDataSource.addInitializer(null);
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.connection = driverConnection;
        singleConnectionDataSource.close();
        verify(driverConnection).close();
    }

    @Test
    public void testTryLock_NoLock() throws InterruptedException {
        assertFalse(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testTryLock_LockAcquired() throws InterruptedException {
        singleConnectionDataSource.lock = new ReentrantLock();
        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy_ConnectionAcquiredButLockedByAnotherThread() throws Throwable {
        singleConnectionDataSource.lock = new ReentrantLock();
        singleConnectionDataSource.connection = driverConnection;

        try {
            singleConnectionDataSource.getConnectionProxy(singleConnectionDataSource.connection);
            singleConnectionDataSource.getConnectionProxy(singleConnectionDataSource.connection);
            fail("Expected ConflictException to be thrown");
        } catch (ConflictException e) {
            // EXPECTED
        }
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = mock(Connection.class);

        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisherMock).publishEvent(argThat(new ConnectionResetEventMatcher(connection)));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(driverManager.getConnection(DriverManager.getDefaultConnectionProperties())).thenReturn(driverConnection);

        singleConnectionDataSource.connection = null;
        singleConnectionDataSource.lock = null;
        Connection createdConnection = singleConnectionDataSource.innerCreateConnection();

        assertSame(driverConnection, createdConnection);
    }

    @Captor
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventArgumentCaptor;

    private static class ConnectionResetEventMatcher {
        private final Connection connection;

        public ConnectionResetEventMatcher(Connection connection) {
            this.connection = connection;
        }

        public boolean matches(Object argument) {
            if (!(argument instanceof ConnectionResetEvent)) {
                return false;
            }
            ConnectionResetEvent event = (ConnectionResetEvent) argument;
            return Objects.equals(event.getConnection(), connection);
        }
    }
}