package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private Connection connectionMock;

    @Mock
    private ConnectionInitializer connectionInitializerMock;

    private SingleConnectionDataSource dataSource;

    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.dataSource = new SingleConnectionDataSource(true);
        this.initializerList = new ArrayList<>();
        this.dataSource.addInitializer(connectionInitializerMock);
        this.dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(1000)).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        verify(connectionMock, times(1)).isValid(1000);
        verify(connectionMock, times(1)).isClosed();

        connection.close();

        Connection connection2 = this.dataSource.getConnection();
        assertNotNull(connection2);
        verify(connectionMock, times(2)).isValid(1000);
        verify(connectionMock, times(2)).isClosed();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        when(connectionMock.isValid(1000)).thenReturn(false);

        this.dataSource.getConnection();

        verify(connectionMock, times(1)).isValid(1000);
        verify(connectionMock, times(1)).isClosed();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connectionMock.isValid(1000)).thenReturn(true);

        this.dataSource.resetConnection();

        Connection newConnection = this.dataSource.getConnection();
        assertNotNull(newConnection);

        verify(connectionMock, times(1)).isValid(1000);
        verify(connectionMock, times(1)).isClosed();
        verify(connectionMock, times(1)).close();
        verify(this.dataSource, times(1)).onConnectionReset(newConnection);
    }

    @Test
    public void testResetConnectionMultipleCalls() throws SQLException {
        when(connectionMock.isValid(1000)).thenReturn(true);

        this.dataSource.resetConnection();
        this.dataSource.resetConnection();
        this.dataSource.resetConnection();

        Connection newConnection1 = this.dataSource.getConnection();
        assertNotNull(newConnection1);

        Connection newConnection2 = this.dataSource.getConnection();
        assertNotNull(newConnection2);

        Connection newConnection3 = this.dataSource.getConnection();
        assertNotNull(newConnection3);

        verify(connectionMock, times(3)).isValid(1000);
        verify(connectionMock, times(3)).isClosed();
        verify(connectionMock, times(3)).close();
        verify(this.dataSource, times(3)).onConnectionReset(newConnection1);
        verify(this.dataSource, times(3)).onConnectionReset(newConnection2);
        verify(this.dataSource, times(3)).onConnectionReset(newConnection3);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionConnectionOccupied() throws SQLException {
        this.dataSource.setLock(new ReentrantLock());
        this.dataSource.getConnection();
        this.dataSource.getConnection();

        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        ConflictException exception = assertThrows(ConflictException.class, () -> this.dataSource.getConnection());
        assertEquals(ErrorCodes.ConnectionOccupied, exception.getCode());
        assertEquals("Connection is occupied, waited 10000 millis", exception.getMessage());
        verify(this.dataSource.getLock(), times(1)).tryLock(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAddInitializer() {
        Connection connection = mock(Connection.class);
        this.dataSource.addInitializer(connection);
        assertEquals(1, this.dataSource.getInitializerList().size());
    }

    @Test
    public void testInitializeConnection() throws SQLException {
        when(connectionMock.isValid(1000)).thenReturn(false);

        doNothing().when(connectionInitializerMock).init(connectionMock);

        this.dataSource.getConnection();

        verify(connectionMock, times(1)).isValid(1000);
        verify(connectionMock, times(1)).isClosed();
        verify(connectionMock, never()).close();
        verify(connectionInitializerMock, times(1)).init(connectionMock);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        this.dataSource.getConnection();

        this.dataSource.close();

        verify(connection, times(1)).close();
    }
}