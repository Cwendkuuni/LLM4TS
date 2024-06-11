package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

/**
 * Tests for {@link SingleConnectionDataSource}
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private List<ConnectionInitializer> initializerList;
    private EventPublisher eventPublisherMock;

    @Before
    public void setup() {
        this.connectionMock = Mockito.mock(Connection.class);
        this.initializerList = new ArrayList<>();
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.dataSource = new SingleConnectionDataSource(true, initializerList, eventPublisherMock);
    }

    /**
     * Test getConnection method
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConnection() throws Exception {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        verify(this.dataSource).prepareConnection(connection);
        verifyNoMoreInteractions(this.dataSource);
    }

    /**
     * Test getConnection method with invalid username or password
     *
     * @throws Exception if an error occurs
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws Exception {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        when(this.connectionMock.isClosed()).thenReturn(false);
        when(this.connectionMock.isValid(anyInt())).thenReturn(false);
        this.dataSource.getConnection();
        verify(this.connectionMock).close();
    }

    /**
     * Test resetConnection method
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testResetConnection() throws Exception {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        when(this.connectionMock.isClosed()).thenReturn(true);
        this.dataSource.resetConnection();
        verify(this.dataSource.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    /**
     * Test addInitializer method
     */
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializerMock);
        assertTrue(this.dataSource.initializerList.contains(initializerMock));
    }

    /**
     * Test close method
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testClose() throws Exception {
        when(this.dataSource.innerCreateConnection()).thenThrow(new SQLException());
        this.dataSource.close();
        verify(this.dataSource.connection).close();
    }

    /**
     * Test tryLock method
     */
    @Test
    public void testTryLock() {
        assertFalse(this.dataSource.tryLock());
    }

    /**
     * Test getConnectionProxy method
     */
    @Test
    public void testGetConnectionProxy() {
        Connection connection = Mockito.mock(Connection.class);
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, this.dataSource.lock);
        Connection connectionProxy = this.dataSource.getConnectionProxy(connection);
        assertEquals(handler, connectionProxy.getClass().getProxyClass().getHandler());
    }

    /**
     * Test initConnection method
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testInitConnection() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.initConnection(connection);
        verify(initializerMock).init(connection);
    }

    /**
     * Test onConnectionCreated method
     */
    @Test
    public void testOnConnectionCreated() {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.onConnectionCreated(connection);
    }

    /**
     * Test onConnectionReset method
     */
    @Test
    public void testOnConnectionReset() {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.onConnectionReset(connection);
        verify(this.dataSource.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    /**
     * Test innerCreateConnection method
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testInnerCreateConnection() throws Exception {
        Connection connection = this.dataSource.innerCreateConnection();
        assertNotNull(connection);
    }
}