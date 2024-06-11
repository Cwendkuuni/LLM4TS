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
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.EventPublisherTestUtil;
import com.oceanbase.odc.core.datasource.OceanbaseDataSourceTestConstants;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {
    private ConnectionInitializer connectionInitializerMock;
    private EventPublisher eventPublisherMock;
    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws SQLException {
        connectionInitializerMock = mock(ConnectionInitializer.class);
        eventPublisherMock = Mockito.spy(EventPublisherTestUtil.createMockEventPublisher());
        dataSource = new SingleConnectionDataSource(true) {
            {
                setEventPublisher(eventPublisherMock);
                addInitializer(connectionInitializerMock);
            }
        };
        connectionMock = mock(Connection.class);
        initializerList = new ArrayList<>();
        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(connectionMock.getAutoCommit()).thenReturn(null);
        doNothing().when(eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(connectionMock.getAutoCommit()).thenReturn(null);

        Connection connection = dataSource.getConnection();
        assertEquals(connectionMock, connection);
        verify(connectionMock, times(1)).isValid(eq(getLoginTimeout()));
        verify(connectionMock, times(1)).getAutoCommit();
        verifyNoMoreInteractions(connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(connectionMock.getAutoCommit()).thenReturn(null);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernameOrPassword() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(false);

        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        when(connectionMock.isValid(anyLong())).thenReturn(false);

        dataSource.resetConnection();
        verify(connectionMock, times(1)).close();

        Connection newConnection = dataSource.innerCreateConnection();
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer1 = mock(ConnectionInitializer.class);
        ConnectionInitializer connectionInitializer2 = mock(ConnectionInitializer.class);

        dataSource.addInitializer(connectionInitializer1);
        dataSource.addInitializer(connectionInitializer2);

        assertEquals(2, dataSource.initializerList.size());
    }

    @Test
    public void testClose() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);

        dataSource.close();

        verify(connectionMock, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = Boolean.TRUE;
        when(connectionMock.getAutoCommit()).thenReturn(Boolean.FALSE);

        dataSource.prepareConnection(connectionMock);

        assertEquals(autoCommit, connectionMock.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        long timeOutMillis = 10 * 1000;
        boolean expectedResult = true;

        Lock lockMock = mock(ReentrantLock.class);
        when(lockMock.tryLock(eq(timeOutMillis), eq(TimeUnit.MILLISECONDS))).thenReturn(expectedResult);

        boolean result = dataSource.tryLock();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = connectionMock;
        Lock lockMock = mock(ReentrantLock.class);
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lockMock);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);

        assertEquals(handler, Proxy.getInvocationHandler(connectionProxy));
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = connectionMock;

        dataSource.initConnection(connection);

        for (ConnectionInitializer initializer : dataSource.initializerList) {
            verify(initializer).init(connection);
        }
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = connectionMock;

        dataSource.onConnectionCreated(connection);

        verify(log, times(1)).debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = connectionMock;

        dataSource.onConnectionReset(connection);

        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = connectionMock;

        when(newConnectionFromDriver(anyString(), anyString())).thenReturn(connection);

        Connection innerConnection = dataSource.innerCreateConnection();

        assertEquals(connection, innerConnection);
        verify(connection, times(1)).isValid(eq(getLoginTimeout()));
    }
}