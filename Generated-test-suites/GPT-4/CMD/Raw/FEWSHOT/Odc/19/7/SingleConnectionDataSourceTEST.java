package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {
    
    @Test
    public void testGetConnection() throws SQLException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.isClosed()).thenReturn(false);
        when(mockConn.isValid(anyInt())).thenReturn(true);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.connection = mockConn;
        dataSource.lock = mock(Lock.class);

        Connection proxyConn = dataSource.getConnection();
        assertNotNull(proxyConn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionAutoReconnectFalse_ClosedConnection() throws SQLException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.isClosed()).thenReturn(true);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.connection = mockConn;
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionDiffCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.getConnection("differentUsername", "differentPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection mockConn = mock(Connection.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.connection = mockConn;
        
        assertNotNull(dataSource.connection);
        
        dataSource.resetConnection();
        
        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyLockTimeout() {
        Connection mockConn = mock(Connection.class);
        Lock mockLock = mock(Lock.class);
        try {
            when(mockLock.tryLock(anyLong(), any())).thenThrow(new InterruptedException("fake exception"));
            SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
            dataSource.lock = mockLock;
            dataSource.getConnectionProxy(mockConn);
        } catch(InterruptedException e) {
            // ignore
        }
    }

    @Test
    public void testClose() throws SQLException {
        Connection mockConn = mock(Connection.class);
        doNothing().when(mockConn).close();

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.connection = mockConn;

        dataSource.close();

        assertNull(dataSource.connection);
    }

    @Test
    public void testConnectionInitializer() throws SQLException {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);

        dataSource.addInitializer(initializer);
        
        assertFalse(dataSource.initializerList.isEmpty());
        assertEquals(1, dataSource.initializerList.size());
    }
    
    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection mockConn = mock(Connection.class);
        EventPublisher mockPublisher = mock(EventPublisher.class);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.setEventPublisher(mockPublisher);

        dataSource.onConnectionReset(mockConn);
        
        Mockito.verify(mockPublisher, Mockito.times(1)).publishEvent(Mockito.any());
    }
}