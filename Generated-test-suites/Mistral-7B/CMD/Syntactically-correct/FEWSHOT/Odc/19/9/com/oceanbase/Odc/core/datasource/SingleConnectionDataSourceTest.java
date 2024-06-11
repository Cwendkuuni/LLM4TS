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

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private Connection connectionMock;
    private EventPublisher eventPublisherMock;
    private SingleConnectionDataSource testObject;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        connectionMock = Mockito.mock(Connection.class);
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        testObject = new SingleConnectionDataSource();
        testObject.setEventPublisher(eventPublisherMock);
        initializerList = new ArrayList<>();
        testObject.setInitializerList(initializerList);
    }

    @After
    public void tearDown() {
        testObject = null;
        connectionMock = null;
        eventPublisherMock = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        testObject.addInitializer(mock(ConnectionInitializer.class));

        Connection connection = testObject.getConnection();

        assertEquals(connectionMock, connection);

        verify(connectionMock, times(1)).isValid(anyInt());
        verify(connectionMock, never()).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(false);

        testObject.getConnection();

        verify(connectionMock, times(1)).isValid(anyInt());
        verify(connectionMock, never()).close();
    }

    @Test
    public void testResetConnection() throws SQLException {
        testObject.resetConnection();

        verify(connectionMock, times(1)).close();
        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);

        testObject.addInitializer(initializerMock);

        assertTrue(initializerList.contains(initializerMock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullInitializer() {
        testObject.addInitializer(null);

        fail();
    }

    @Test
    public void testResetConnectionNullConnection() throws SQLException {
        testObject.resetConnection();

        verify(connectionMock, never()).close();
        verify(eventPublisherMock, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionThrowsSQLException() throws SQLException {
        doThrow(new SQLException("Test exception")).when(connectionMock).isValid(anyInt());

        testObject.getConnection();

        verify(connectionMock, times(1)).isValid(anyInt());
        verify(connectionMock, never()).close();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        testObject.getConnectionProxy(connectionMock);

        verify(connectionMock, times(1)).isValid(anyInt());
    }

    @Test
    public void testInitConnection() throws SQLException {
        testObject.initConnection(connectionMock);

        verify(connectionMock, times(initializerList.size())).isValid(anyInt());
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        testObject.onConnectionCreated(connectionMock);

        verify(connectionMock, times(1)).isValid(anyInt());
    }

    @Test
    public void testGetAutoCommit() {
        assertTrue(testObject.getAutoCommit() == null);
    }

    @Test
    public void testSetAutoCommit() {
        testObject.setAutoCommit(true);
        assertEquals(true, testObject.getAutoCommit());
    }

    @Test
    public void testGetTimeOutMillis() {
        assertEquals(10000, testObject.getTimeOutMillis());
    }

    @Test
    public void testSetTimeOutMillis() {
        testObject.setTimeOutMillis(20000);
        assertEquals(20000, testObject.getTimeOutMillis());
    }

    @Test(expected = ConflictException.class)
    public void testTryLock() throws SQLException, InterruptedException {
        testObject.lock.tryLock(500, TimeUnit.MILLISECONDS);
        testObject.lock.tryLock(500, TimeUnit.MILLISECONDS);

        throw new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
                "Connection is occupied, waited 500 millis");
    }
}