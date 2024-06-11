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
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test cases for the SingleConnectionDataSource class
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

        Connection connection = this.dataSource.getConnection();
        assertEquals("Returned expected connection", this.connectionMock, connection);

        verify(this.dataSource, times(1)).innerCreateConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        String invalidUsername = "invalidUsername";
        String invalidPassword = "invalidPassword";

        when(this.dataSource.getUsername()).thenReturn(invalidUsername);
        when(this.dataSource.getPassword()).thenReturn(invalidPassword);

        this.dataSource.getConnection("username", "password");
        fail("Expected SQLException to be thrown");

        verify(this.dataSource, times(1)).getConnection("username", "password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.dataSource.getConnection()).thenReturn(this.connectionMock);
        when(this.connectionMock.isClosed()).thenReturn(true);

        this.dataSource.resetConnection();

        verify(this.dataSource, times(1)).close();
        verify(this.dataSource, times(1)).innerCreateConnection();
        verify(this.dataSource, times(1)).onConnectionReset(this.connectionMock);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        this.dataSource.lock = mock(Lock.class);

        when(this.dataSource.tryLock()).thenReturn(false);

        this.dataSource.getConnection();

        fail("Expected ConflictException to be thrown");
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);

        this.dataSource.addInitializer(initializerMock);

        assertEquals("Expected initializer list size to be 1", 1, this.dataSource.initializerList.size());
        verify(this.dataSource, times(1)).add(initializerMock);
    }

    @Test
    public void testClose() throws SQLException {
        this.dataSource.connection = this.connectionMock;

        this.dataSource.close();

        verify(this.connectionMock, times(1)).close();
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        this.dataSource.onConnectionCreated(connection);

        verify(this.dataSource, times(1)).log(isA(org.slf4j.Logger.class), isA(Level.class), anyString());
    }

    @Test
    public void testOnConnectionReset() {
        this.dataSource.eventPublisher = this.eventPublisherMock;
        Connection connection = Mockito.mock(Connection.class);

        this.dataSource.onConnectionReset(connection);

        verify(this.eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testCreateConnection() throws SQLException {
        when(this.dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(this.connectionMock);

        Connection connection = this.dataSource.createConnection();

        assertEquals("Returned expected connection", this.connectionMock, connection);

        verify(this.dataSource, times(1)).newConnectionFromDriver(anyString(), anyString());
        verify(this.dataSource, times(1)).prepareConnection(this.connectionMock);
        verify(this.dataSource, times(1)).getConnectionProxy(this.connectionMock);
    }

    @Test
    public void testCreateConnectionWithInitializers() throws SQLException {
        ConnectionInitializer initializerMock1 = mock(ConnectionInitializer.class);
        ConnectionInitializer initializerMock2 = mock(ConnectionInitializer.class);

        List<ConnectionInitializer> initializerList = new ArrayList<>();
        initializerList.add(initializerMock1);
        initializerList.add(initializerMock2);

        when(this.dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(this.connectionMock);

        this.dataSource.addInitializer(initializerMock1);
        this.dataSource.addInitializer(initializerMock2);

        this.dataSource.createConnection();

        verify(this.connectionMock, times(2)).prepareCall(anyObject());
        verify(initializerMock1, times(1)).init(this.connectionMock);
        verify(initializerMock2, times(1)).init(this.connectionMock);
    }
}