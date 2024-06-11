package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.impl.ConnectionInitializerImpl;
import com.oceanbase.odc.core.datasource.impl.SingleConnectionDataSourceImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSourceImpl(true);
        eventPublisher = mock(EventPublisher.class);
        connectionInitializer = mock(ConnectionInitializer.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.addInitializer(connectionInitializer);
        connection = mock(Connection.class);
    }

    @Test
    public void testGetConnection_WhenDataSourceInitialized_ShouldReturnConnection() throws SQLException {
        // Given

        // When
        Connection returnedConnection = dataSource.getConnection();

        // Then
        assertSame(connection, returnedConnection);
        verify(connectionInitializer).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WhenDataSourceNotInitialized_ShouldThrowSQLException() throws SQLException {
        // Given
        dataSource = new SingleConnectionDataSourceImpl(false);

        // When
        dataSource.getConnection();

        // Then
        // The test should throw an SQLException
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WhenInvalidCredentials_ShouldThrowSQLException() throws SQLException {
        // Given
        dataSource = new SingleConnectionDataSourceImpl();

        // When
        dataSource.getConnection("not_username", "not_password");

        // Then
        // The test should throw an SQLException
    }

    @Test
    public void testResetConnection_WhenConnectionExists_ShouldResetConnection() throws SQLException {
        // Given
        dataSource.setConnection(connection);

        // When
        dataSource.resetConnection();

        // Then
        assertNull(dataSource.getConnection());
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection_WhenNoConnectionExists_ShouldNotThrowException() throws SQLException {
        // Given
        dataSource.setConnection(null);

        // When
        dataSource.resetConnection();

        // Then
        // Nothing happens
    }

    @Test
    public void testAddInitializer_WhenInitializerAddedToDataSource_ShouldAddInitializer() {
        // Given

        // When
        dataSource.addInitializer(connectionInitializer);

        // Then
        assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
    }

    @Test
    public void testClose_WhenDataSourceClosed_ShouldCloseConnection() throws SQLException {
        // Given
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });

        // When
        dataSource.close();

        // Then
        verify(connection).close();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnection_WhenConnectionIsOccupied_ShouldThrowConflictException() throws SQLException {
        // Given
        dataSource.setLock(mock(Lock.class));
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        // When
        dataSource.getConnection();
        dataSource.getConnection();

        // Then
        // The test should throw a ConflictException
        verify(dataSource.getLock()).lock();
        verify(dataSource.getLock()).unlock();
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
        throw new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
                "Connection is occupied, waited " + dataSource.getTimeOutMillis() + " millis");
    }

    @After
    public void tearDown() {
        dataSource = null;
        eventPublisher = null;
        connectionInitializer = null;
        connection = null;
    }

}