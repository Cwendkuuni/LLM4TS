package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2021-11-08 16:20
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private static final String DB_URL = "jdbc:mock:testdb";
    private static final String DB_USER = "testuser";
    private static final String DB_PASSWORD = "testpassword";

    private SingleConnectionDataSource singleConnectionDataSource;
    private EventPublisher eventPublisherMock;
    private Connection connectionMock;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock = Mockito.mock(EventPublisher.class));
        singleConnectionDataSource.setAutoReconnect(true);

        when(eventPublisherMock.publishEvent(Mockito.any(ConnectionResetEvent.class)))
                .thenReturn(true);

        connectionMock = Mockito.mock(Connection.class);
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                prepareConnection(connection);
            }
        });
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
        eventPublisherMock = null;
        connectionMock = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);

        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);

        assertEquals(singleConnectionDataSource.getConnection(), connection);
        verify(connectionMock, times(1)).isValid(anyLong());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(false);

        singleConnectionDataSource.getConnection();

        verify(connectionMock, times(1)).isValid(anyLong());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullConnection() throws SQLException {
        singleConnectionDataSource.setConnection(null);

        singleConnectionDataSource.getConnection();

        verify(eventPublisherMock, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);

        singleConnectionDataSource.resetConnection();

        verify(eventPublisherMock, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithLockedConnection() throws SQLException {
        when(connectionMock.isValid(anyLong())).thenReturn(true);
        when(lockMock.tryLock(anyLong(), anyLong(), anyInt())).thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.setConnection(connectionMock);

        singleConnectionDataSource.close();

        verify(connectionMock, times(1)).close();
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(connectionInitializerMock);

        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertEquals(connectionInitializerMock, initializerList.get(0));
    }

    @Test
    public void testGetAutoCommit() throws SQLException {
        boolean autoCommit = true;

        singleConnectionDataSource.setAutoCommit(autoCommit);

        assertEquals(autoCommit, singleConnectionDataSource.isAutoCommit());
    }

    @Test
    public void testSetAutoCommitWithNullValue() throws SQLException {
        singleConnectionDataSource.setAutoCommit(null);

        assertFalse(singleConnectionDataSource.isAutoCommit());
    }

    @Test
    public void testSetAutoCommitWithFalseValue() throws SQLException {
        singleConnectionDataSource.setAutoCommit(false);

        assertFalse(singleConnectionDataSource.isAutoCommit());
    }

    @Test
    public void testSetAutoCommitWithTrueValue() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);

        assertTrue(singleConnectionDataSource.isAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();

        singleConnectionDataSource.getConnection("INVALID_USERNAME", "INVALID_PASSWORD");
    }

    private void prepareConnection(Connection connection) throws SQLException {
        // Empty implementation
    }
}