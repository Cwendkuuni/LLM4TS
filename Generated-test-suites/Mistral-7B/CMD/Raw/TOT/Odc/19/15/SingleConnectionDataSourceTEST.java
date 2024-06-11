package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for SingleConnectionDataSource.
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connection.isValid(anyInt())).thenReturn(true);
        dataSource = new SingleConnectionDataSource(true);
        dataSource.addInitializer(connectionInitializer);
        dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Given
        when(connectionInitializer.init(connection)).thenReturn(null);

        // When
        Connection actualConnection = dataSource.getConnection();

        // Then
        assertNotNull(actualConnection);
        verify(connectionInitializer, times(1)).init(connection);
    }

    @Test
    public void testGetConnection_resetConnection() throws SQLException {
        // Given
        when(connectionInitializer.init(connection)).thenThrow(new SQLException("Initialization failed"));
        when(connection.isClosed()).thenReturn(true);
        dataSource.resetConnection();

        // When
        Connection actualConnection = dataSource.getConnection();

        // Then
        assertNull(actualConnection);
        verify(eventPublisher, times(1)).publishEvent(isA(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnection_invalidCredentials() throws SQLException {
        // Given
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");

        // When
        SQLException exception = assertThrows(SQLException.class, () -> dataSource.getConnection());

        // Then
        assertTrue(exception.getMessage().contains("Invalid username or password"));
    }

    @Test
    public void testAddInitializer() {
        // Given

        // When
        dataSource.addInitializer(connectionInitializer);

        // Then
        ArgumentCaptor<ConnectionInitializer> initializerArgumentCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        verify(dataSource, times(1)).addInitializer(initializerArgumentCaptor.capture());
        ConnectionInitializer initializer = initializerArgumentCaptor.getValue();

        // Then
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        // Given
        dataSource.setAutoCommit(false);

        // When
        dataSource.close();

        // Then
        assertNull(dataSource.getConnection());
        verify(connection, times(1)).close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEventPublisherNull() {
        // Given

        // When
        dataSource.setEventPublisher(null);

        // Then
        throw new AssertionError(); // This test should throw an IllegalArgumentException
    }

    @After
    public void tearDown() throws SQLException, InterruptedException {
        dataSource.close();
        Mockito.reset(connection, connectionInitializer, eventPublisher);
    }
}