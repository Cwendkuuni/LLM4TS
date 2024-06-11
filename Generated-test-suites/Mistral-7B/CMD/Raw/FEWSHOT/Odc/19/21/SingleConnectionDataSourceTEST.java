package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.util.NewConnectionFromDriver;

/**
 * Test cases for SingleConnectionDataSource class
 */
public class SingleConnectionDataSourceTest {

    /**
     * Mocked Connection object
     */
    private Connection mockConnection;

    /**
     * Mocked EventPublisher object
     */
    private EventPublisher mockEventPublisher;

    /**
     * Mocked ConnectionInitializer object
     */
    private ConnectionInitializer mockConnectionInitializer;

    /**
     * Test data source instance
     */
    private SingleConnectionDataSource singleConnectionDataSource;

    /**
     * Setup mocks and initialize test data source
     */
    @Before
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
        mockEventPublisher = Mockito.mock(EventPublisher.class);
        mockConnectionInitializer = Mockito.mock(ConnectionInitializer.class);

        singleConnectionDataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return mockConnection;
            }

            @Override
            public void addInitializer(ConnectionInitializer connectionInitializer) {
                initializerList.add(connectionInitializer);
            }
        };

        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
    }

    /**
     * Test getConnection method with valid credentials
     */
    @Test
    public void testGetConnectionWithValidCredentials() throws SQLException {
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        when(mockConnection.isClosed()).thenReturn(false);

        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(anyInt());
        verify(mockConnection, never()).close();
    }

    /**
     * Test getConnection method with invalid credentials
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        when(mockConnection.isValid(anyInt())).thenReturn(false);

        singleConnectionDataSource.getConnection();

        verify(mockConnection, times(1)).isValid(anyInt());
        verify(mockConnection, never()).close();
    }

    /**
     * Test resetConnection method
     */
    @Test
    public void testResetConnection() throws SQLException {
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        singleConnectionDataSource.resetConnection();

        verify(mockConnection, times(1)).close();
        verify(mockEventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    /**
     * Test addInitializer method
     */
    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(mockConnectionInitializer);
        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();

        assertEquals(1, initializerList.size());
        verify(mockConnectionInitializer, times(1)).init(eq(mockConnection));
    }

    /**
     * Test close method
     */
    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();

        verify(mockConnection, times(1)).close();
    }

    /**
     * Test innerCreateConnection method
     */
    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithConnectionNotNull() throws SQLException {
        singleConnectionDataSource.connection = mockConnection;

        singleConnectionDataSource.innerCreateConnection();
    }

    /**
     * Test innerCreateConnection method with valid connection creation
     */
    @Test
    public void testInnerCreateConnectionWithValidConnectionCreation() throws SQLException {
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        singleConnectionDataSource.innerCreateConnection();

        verify(mockConnection, times(1)).isValid(anyInt());
        verify(mockConnection, never()).close();
        verify(singleConnectionDataSource, times(1)).onConnectionCreated(eq(mockConnection));
    }

    /**
     * Test innerCreateConnection method with invalid connection creation
     */
    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionWithInvalidConnectionCreation() throws SQLException {
        doThrow(SQLException.class).when(mockConnection).isValid(anyInt());

        singleConnectionDataSource.innerCreateConnection();
    }

    /**
     * Test getConnectionProxy method
     */
    @Test
    public void testGetConnectionProxy() throws SQLException {
        SingleConnectionDataSource testDataSource = new SingleConnectionDataSource();

        Connection connection = testDataSource.innerCreateConnection();
        Lock lock = new ReentrantLock();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);

        Connection connectionProxy = testDataSource.getConnectionProxy(connection);

        assertEquals(connection, connectionProxy);
        assertTrue(handler.getClass().isInstance(Proxy.getInvocationHandler(connectionProxy)));
    }

    /**
     * Test tryLock method
     */
    @Test
    public void testTryLock() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        Lock lock = new ReentrantLock();

        assertTrue(singleConnectionDataSource.tryLock(10, TimeUnit.MILLISECONDS));
        assertFalse(singleConnectionDataSource.tryLock(10, TimeUnit.MILLISECONDS));
        assertTrue(lock.tryLock(10, TimeUnit.MILLISECONDS));
        assertFalse(lock.tryLock(10, TimeUnit.MILLISECONDS));
    }

    /**
     * Test initConnection method
     */
    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        singleConnectionDataSource.initConnection(connection);

        for (ConnectionInitializer initializer : singleConnectionDataSource.getInitializerList()) {
            verify(initializer, times(1)).init(eq(connection));
        }
    }
}