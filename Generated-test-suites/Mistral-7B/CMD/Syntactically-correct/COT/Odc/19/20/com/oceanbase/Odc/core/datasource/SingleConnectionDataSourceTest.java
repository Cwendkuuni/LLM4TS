package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() {
        this.eventPublisherMock = mock(EventPublisher.class);
        this.initializerList = new ArrayList<>();
        this.dataSource = new SingleConnectionDataSource() {
            @Override
            public void addInitializer(ConnectionInitializer connectionInitializer) {
                this.initializerList.add(connectionInitializer);
            }

            @Override
            public EventPublisher getEventPublisher() {
                return this.eventPublisherMock;
            }
        };
    }

    /**
     * Test for constructor with no arguments
     */
    @Test
    public void testConstructorWithNoArguments() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        assertNotNull(singleConnectionDataSource);
    }

    /**
     * Test for constructor with autoReconnect argument
     */
    @Test
    public void testConstructorWithAutoReconnectArgument() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        assertTrue(singleConnectionDataSource.isAutoReconnect());
        SingleConnectionDataSource singleConnectionDataSourceFalse = new SingleConnectionDataSource(false);
        assertFalse(singleConnectionDataSourceFalse.isAutoReconnect());
    }

    /**
     * Test for getConnection method when connection is not initialized
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionIsNotInitialized() throws SQLException {
        when(dataSource.innerCreateConnection()).thenThrow(new SQLException());
        dataSource.getConnection();
    }

    /**
     * Test for getConnection method when connection is available
     */
    @Test
    public void testGetConnectionWhenConnectionIsAvailable() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        Connection actualConnection = dataSource.getConnection();
        assertEquals(connectionMock, actualConnection);
    }

    /**
     * Test for getConnection method when username and password are correct
     */
    @Test
    public void testGetConnectionWithCorrectUsernameAndPassword() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        when(connectionMock.isValid()).thenReturn(true);

        Connection actualConnection = dataSource.getConnection("username", "password");
        assertEquals(connectionMock, actualConnection);
    }

    /**
     * Test for getConnection method when username and password are incorrect
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionWithIncorrectUsernameAndPassword() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        when(connectionMock.isValid()).thenReturn(false);

        dataSource.getConnection("username", "password");
    }

    /**
     * Test for resetConnection method
     */
    @Test
    public void testResetConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        dataSource.resetConnection();
        verify(dataSource, times(1)).close();
        verify(dataSource, times(1)).innerCreateConnection();
        verify(dataSource, times(1)).onConnectionReset(connectionMock);
    }

    /**
     * Test for addInitializer method
     */
    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializerMock = mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializerMock);
        ArgumentCaptor<ConnectionInitializer> connectionInitializerArgumentCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        verify(dataSource, times(1)).initConnection(connectionInitializerMock.getConnection());
        verify(this.initializerList, times(1)).add(connectionInitializerMock);
    }

    /**
     * Test for close method
     */
    @Test
    public void testClose() {
        Connection connectionMock = mock(Connection.class);
        when(dataSource.connection).thenReturn(connectionMock);
        dataSource.close();
        verify(connectionMock).close();
    }

    /**
     * Test for initConnection method
     */
    @Test
    public void testInitConnection() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        ConnectionInitializer connectionInitializerMock = mock(ConnectionInitializer.class);
        when(dataSource.innerCreateConnection()).thenReturn(connectionMock);
        dataSource.initConnection(connectionMock);
        verify(connectionInitializerMock, times(1)).init(connectionMock);
    }

    /**
     * Test for onConnectionCreated method
     */
    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connectionMock = mock(Connection.class);
        dataSource.onConnectionCreated(connectionMock);
        verify(dataSource, times(1)).log.info("Connection is created");
    }

    /**
     * Test for onConnectionReset method
     */
    @Test
    public void testOnConnectionReset() {
        Connection connectionMock = mock(Connection.class);
        dataSource.onConnectionReset(connectionMock);
        verify(this.eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    /**
     * Test for getAutoCommit method when autoCommit is null
     */
    @Test
    public void testGetAutoCommitWhenAutoCommitIsNull() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        assertNull(singleConnectionDataSource.getAutoCommit());
    }

    /**
     * Test for getAutoCommit method when autoCommit is true
     */
    @Test
    public void testGetAutoCommitWhenAutoCommitIsTrue() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    /**
     * Test for getAutoCommit method when autoCommit is false
     */
    @Test
    public void testGetAutoCommitWhenAutoCommitIsFalse() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(false);
        assertFalse(singleConnectionDataSource.getAutoCommit());
    }

    /**
     * Test for timeOutMillis property
     */
    @Test
    public void testTimeOutMillisProperty() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setTimeOutMillis(5000);
        assertEquals(5000, singleConnectionDataSource.getTimeOutMillis());
    }
}