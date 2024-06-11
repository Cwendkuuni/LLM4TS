package com.oceanbase.odc.core.datasource;

import static com.oceanbase.odc.core.datasource.ErrorCodes.ConnectionOccupied;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private ConnectionInitializer connectionInitializer2;

    private SingleConnectionDataSource singleConnectionDataSource;

    private List<ConnectionInitializer> initializerList;

    private Lock lock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        initializerList = new ArrayList<>();
        singleConnectionDataSource.setInitializerList(initializerList);
        lock = new ReentrantLock();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);

        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        Connection result = singleConnectionDataSource.getConnection();

        assertSame(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionConnectionIsNull() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(null);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionConnectionClosed() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(false);

        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionConnectionInvalid() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(false);

        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionTimeOut() throws SQLException, InterruptedException {
        when(connection.isValid(anyInt())).thenReturn(false);

        when(singleConnectionDataSource.tryLock()).thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);

        assertNotNull(connectionProxy);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connection.isValid(anyInt())).thenReturn(true);

        singleConnectionDataSource.setConnection(connection);

        singleConnectionDataSource.resetConnection();

        assertNull(singleConnectionDataSource.getConnection());
        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionConnectionNull() throws SQLException {
        singleConnectionDataSource.setConnection(null);

        singleConnectionDataSource.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.addInitializer(connectionInitializer2);

        assertEquals(2, singleConnectionDataSource.getInitializerList().size());
    }

    @Test
    public void testInitConnection() throws SQLException {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.addInitializer(connectionInitializer2);

        doNothing().when(connectionInitializer).init(ArgumentMatchers.any(Connection.class));
        doNothing().when(connectionInitializer2).init(ArgumentMatchers.any(Connection.class));

        Connection connection = mock(Connection.class);

        singleConnectionDataSource.initConnection(connection);

        verify(connectionInitializer).init(connection);
        verify(connectionInitializer2).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);

        singleConnectionDataSource.onConnectionCreated(connection);

        if (log.isDebugEnabled()) {
            verify(log, times(1)).debug("Connection is created");
        }
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);

        singleConnectionDataSource.setEventPublisher(eventPublisher);

        singleConnectionDataSource.onConnectionReset(connection);

        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionConnectionInitializationError() throws SQLException {
        when(newConnectionFromDriver(anyString(), anyString())).thenThrow(new SQLException());

        singleConnectionDataSource.innerCreateConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        Connection connection = mock(Connection.class);

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);

        Object result = handler.invoke(handler, mock(Method.class), new Object[0]);

        assertNull(result);
    }
}