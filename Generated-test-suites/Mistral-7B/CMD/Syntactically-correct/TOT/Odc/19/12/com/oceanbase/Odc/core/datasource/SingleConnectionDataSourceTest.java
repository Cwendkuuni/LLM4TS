package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisherMock;
    @Captor
    private ArgumentCaptor<ConnectionResetEvent> eventCaptor;

    @Before
    public void setUp() {
        eventPublisherMock = Mockito.mock(EventPublisher.class);
        dataSource = new SingleConnectionDataSource(true, eventPublisherMock);
    }

    @Test
    public void testGetConnectionWithoutInitialization() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithInitialization() throws SQLException {
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializerMock);
        Connection connection = dataSource.getConnection();
        Mockito.verify(initializerMock).init(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertFalse(connection.equals(newConnection));
        Mockito.verify(eventPublisherMock).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testResetConnectionWhileConnectionIsBusy() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            dataSource.resetConnection();
        } finally {
            connection.close();
        }
        dataSource.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializerMock = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializerMock);
        assertTrue(dataSource.getInitializerList().contains(initializerMock));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
        connection.close();
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(false);
        Connection connection = dataSource.getConnection();
        assertFalse(connection.getAutoCommit());

        dataSource.setAutoCommit(true);
        connection.setAutoCommit(true);
        assertTrue(connection.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidAutoReconnect() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false, eventPublisherMock);
        dataSource.getConnection();
        dataSource.resetConnection();
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithValidAutoReconnect() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true, eventPublisherMock);
        dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        Mockito.verify(eventPublisherMock).publishEvent(eventCaptor.capture());
        ConnectionResetEvent event = eventCaptor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidTimeOut() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
            @Override
            public long getLoginTimeout() {
                throw new UnsupportedOperationException();
            }
        };
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithValidTimeOut() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
            @Override
            public long getLoginTimeout() {
                return 3000;
            }
        };
        Connection connection = dataSource.getConnection();
        Mockito.verify(connection).createStatement(getLoginTimeout(), TimeUnit.MILLISECONDS);
    }
}