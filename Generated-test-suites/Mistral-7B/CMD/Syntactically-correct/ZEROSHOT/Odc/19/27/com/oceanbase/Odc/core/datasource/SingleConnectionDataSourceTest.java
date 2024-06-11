package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-28 12:00
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private Connection connectionMock;

    @Mock
    private ConnectionInitializer connectionInitializerMock;

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource(true, eventPublisher);
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        verify(connectionMock).isValid(anyInt());
        verify(connectionMock, never()).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(false);
        Connection connection = singleConnectionDataSource.getConnection();
        fail("Should throw SQLException");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(true);

        singleConnectionDataSource.connection = connectionMock;
        Connection connection = singleConnectionDataSource.getConnection();
        fail("Should throw SQLException");
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);

        singleConnectionDataSource.connection = connectionMock;
        singleConnectionDataSource.resetConnection();

        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
        verify(singleConnectionDataSource, times(1)).addInitializer(connectionInitializerMock);
    }

    @Test(expected = ConflictException.class)
    public void testAddInitializerWithExistingInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
        fail("Should throw ConflictException");
    }

}