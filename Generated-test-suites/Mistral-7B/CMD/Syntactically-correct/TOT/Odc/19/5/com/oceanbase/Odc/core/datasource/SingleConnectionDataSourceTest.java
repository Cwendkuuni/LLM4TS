package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true, eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);
        when(connection.isValid(anyLong())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });

        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
        verify(dataSource).prepareConnection(connection);
        verifyZeroInteractions(eventPublisher);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);
        when(connection.isValid(anyLong())).thenReturn(false);

        dataSource.getConnection();

        verify(connection).close();
        verify(eventPublisher).publishEvent(argThat(isA(ConnectionResetEvent.class)));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalid_user");
        dataSource.setPassword("invalid_password");

        try {
            dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }

        verifyZeroInteractions(dataSource.innerCreateConnection());
        verifyZeroInteractions(eventPublisher);
    }

    @Test
    public void testResetConnection() {
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        dataSource.resetConnection();

        verify(connection).close();
        verify(eventPublisher).publishEvent(argThat(isA(ConnectionResetEvent.class)));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);

        ArgumentCaptor<ConnectionInitializer> connectionInitializerArgumentCaptor = ArgumentCaptor.forClass(ConnectionInitializer.class);
        verify(dataSource.initializerList).add(connectionInitializerArgumentCaptor.capture());
        assertEquals(initializer, connectionInitializerArgumentCaptor.getValue());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhenConnectionIsOccupied() throws SQLException {
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);
        when(connection.isValid(anyLong())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });

        when(dataSource.lock.tryLock(eq(dataSource.timeOutMillis), eq(TimeUnit.MILLISECONDS))).thenReturn(false);

        dataSource.getConnection();

        verify(connection, never()).close();
        verify(eventPublisher).publishEvent(argThat(isA(ConnectionResetEvent.class)));
    }
}