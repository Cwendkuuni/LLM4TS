package com.oceanbase.odc.core.datasource;

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

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Tests for {@link SingleConnectionDataSource} class
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final long TIMEOUT = 15 * 1000;

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private ConnectionInitializer connectionInitializerMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() {
        connectionMock = mock(Connection.class);
        connectionInitializerMock = mock(ConnectionInitializer.class);
        eventPublisherMock = mock(EventPublisher.class);

        when(connectionMock.isValid(TIMEOUT)).thenReturn(true);

        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connectionMock;
            }

            @Override
            public void addInitializer(ConnectionInitializer connectionInitializer) {
                this.initializerList.add(connectionInitializerMock);
            }

            @Override
            public EventPublisher getEventPublisher() {
                return eventPublisherMock;
            }
        };

        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializerMock);

        doNothing().when(connectionInitializerMock).init(connectionMock);

        Connection connection = dataSource.getConnection();

        ArgumentCaptor<Connection> connectionArgument = ArgumentCaptor.forClass(Connection.class);
        verify(connectionInitializerMock).init(connectionArgument.capture());
        Connection capturedConnection = connectionArgument.getValue();
        assertEquals(connectionMock, capturedConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        when(connectionMock.isValid(TIMEOUT)).thenReturn(false);

        try {
            dataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // Expected
        }

        verify(connectionMock, never()).isValid(TIMEOUT);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConnectionOccupied() throws SQLException {
        Connection connection = dataSource.getConnection();

        doThrow(new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
                "Connection is occupied, waited " + TIMEOUT + " millis")).when(connectionMock)
                .close();

        try {
            dataSource.getConnection();
            fail("Expected ConflictException to be thrown");
        } catch (ConflictException e) {
            // Expected
        }

        verify(connectionMock).close();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();

        verify(eventPublisherMock).publishEvent(ArgThat.instanceOf(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testClose() throws SQLException {
        try {
            dataSource.close();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            // Expected
        }

        verify(connectionMock, never()).close();
    }
}