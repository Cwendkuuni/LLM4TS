package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito. Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SimpleConnectionInitializer;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;
    private ReentrantLock lock;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource(true);
        connection = mock(Connection.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        eventPublisher = mock(EventPublisher.class);
        lock = new ReentrantLock();
        when(connection.isValid(any(int.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        DataSourceInitializer.register(SingleConnectionDataSource.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializer);
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isClosed()).thenReturn(false);

        Connection connection = dataSource.getConnection();
        assertEquals(connection, dataSource.getConnection());
        verify(connectionInitializer).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        when(connection.isValid(any(int.class))).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
        when(connection.isValid(any(int.class))).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializer);
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isValid(any(int.class))).thenReturn(false);

        dataSource.resetConnection();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
        assertNull(dataSource.getConnection());
        assertEquals(false, dataSource.autoReconnect);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);
        assertTrue(dataSource.initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.addInitializer(connectionInitializer);
        when(connectionInitializer.init(connection)).thenReturn(null);
        when(connection.isValid(any(int.class))).thenReturn(true);
        dataSource.close();
        verify(connection).close();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhenConnectionIsOccupied() throws SQLException {
        ReentrantLock lock = new ReentrantLock();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setLock(lock);
        when(connection.isValid(any(int.class))).thenReturn(true);

        dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(connection.isValid(any(int.class))).thenReturn(true);
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertEquals(connectionProxy, dataSource.getConnectionProxy(connection));
        verify(lock).lock();
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializer);
        when(connectionInitializer.init(connection)).thenReturn(null);

        Connection connection = dataSource.getConnection();
        dataSource.initConnection(connection);
        verify(connectionInitializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
            @Override
            protected void onConnectionCreated(Connection connection) {
                super.onConnectionCreated(connection);
                assertTrue(true);
            }
        };

        dataSource.addInitializer(connectionInitializer);
        when(connectionInitializer.init(connection)).thenReturn(null);

        dataSource.getConnection();
    }

    @Test
    public void testTryLock() {
        ReentrantLock lock = new ReentrantLock();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setLock(lock);

        assertTrue(dataSource.tryLock());
        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());
    }

}