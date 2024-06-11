package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 *
 * @author yh263208
 * @date 2023-03-15 23:25
 */
public class SingleConnectionDataSourceTest {

    @Test
    public void testEmptyInitializerList() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testInitializerList() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializerMock);
        Connection connection = dataSource.getConnection();
        verify(initializerMock).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testInvalidCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("invalid_username", "invalid_password");
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException, InterruptedException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        doCallRealMethod().when(dataSource).innerCreateConnection();
        dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testConnectionReset() throws SQLException, InterruptedException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        EventPublisher eventPublisherMock = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisherMock);
        doCallRealMethod().when(dataSource).innerCreateConnection();
        dataSource.resetConnection();
        verify(eventPublisherMock).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAutoCommit() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        SingleConnectionDataSource dataSourceWithAutoCommit = new SingleConnectionDataSource(true);
        Connection connection = dataSource.getConnection();
        assertFalse(connection.getAutoCommit());
        Connection connectionWithAutoCommit = dataSourceWithAutoCommit.getConnection();
        assertTrue(connectionWithAutoCommit.getAutoCommit());
    }

    @Test
    public void testTimeOut() throws SQLException, InterruptedException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setTimeOutMillis(100);
        doThrow(new SQLException("Timeout")).when(dataSource).innerCreateConnection();
        try {
            dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Timeout", e.getMessage());
        }
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        Lock lock = dataSource.getLock();
        dataSource.close();
        assertNull(connection);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testConnectionProxy() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertEquals(connection, proxyConnection);
    }

    @Test
    public void testGettersAndSetters() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
        SingleConnectionDataSource dataSourceWithEventPublisher = new SingleConnectionDataSource();
        dataSourceWithEventPublisher.setEventPublisher(mock(EventPublisher.class));
        assertEquals(false, dataSource.isAutoReconnect());
        assertEquals(null, dataSource.getEventPublisher());
        assertEquals(10000L, dataSource.getTimeOutMillis());
        dataSource.setAutoCommit(true);
        assertEquals(true, dataSource.isAutoCommit());
        dataSource.setAutoReconnect(true);
        assertEquals(true, dataSource.isAutoReconnect());
        dataSource.setEventPublisher(mock(EventPublisher.class));
        assertEquals(mock(EventPublisher.class), dataSource.getEventPublisher());
        dataSource.setTimeOutMillis(5000L);
        assertEquals(5000L, dataSource.getTimeOutMillis());
    }
}