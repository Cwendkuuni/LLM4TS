package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.closureignoreinvocationhandler.CloseIgnoreInvocationHandler;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setup() {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionMock = Mockito.mock(Connection.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        Connection result = this.dataSource.getConnection();
        assertEquals(this.connectionMock, result);
        verify(this.dataSource).prepareConnection(this.connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernameAndPassword() throws SQLException {
        this.dataSource.setUsername("testUsername");
        this.dataSource.setPassword("testPassword");
        this.dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenThrow(new SQLException());
        this.dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        this.dataSource.resetConnection();
        verify(this.dataSource).close();
        verify(this.dataSource).onConnectionReset(this.connectionMock);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializerMock);
        verify(this.dataSource).initializerList.add(initializerMock);
    }

    @Test
    public void testClose() throws Exception {
        when(this.connectionMock.close()).thenThrow(new SQLException());
        this.dataSource.close();
        verify(this.connectionMock).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = true;
        this.dataSource.setAutoCommit(autoCommit);
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Lock lockMock = Mockito.mock(Lock.class);
        CloseIgnoreInvocationHandler handlerMock = Mockito.mock(CloseIgnoreInvocationHandler.class);
        when(handlerMock.getConnection()).thenReturn(connection);
        when(handlerMock.getLock()).thenReturn(lockMock);
        Connection result = this.dataSource.getConnectionProxy(connection);
        assertEquals(connection, result);
        verify(lockMock).tryLock(anyLong(), any(TimeUnit.class));
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.setInitializerList(initializerList);
        this.dataSource.addInitializer(initializerMock);
        this.dataSource.initConnection(connection);
        verify(initializerMock).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.onConnectionCreated(connection);
        verify(this.dataSource, times(1)).log.debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
        this.dataSource.onConnectionReset(connection);
        verify(this.eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() {
        Mockito.reset(this.connectionMock, this.eventPublisherMock);
    }
}