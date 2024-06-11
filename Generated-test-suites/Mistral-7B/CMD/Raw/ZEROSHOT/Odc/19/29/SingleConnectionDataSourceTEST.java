package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.ConnectionResetEvent;
import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    @Spy
    private EventPublisher eventPublisher;

    @Mock
    private Connection connectionMock;

    @Mock
    private ConnectionInitializer connectionInitializerMock;

    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        initializerList = new ArrayList<>();
        singleConnectionDataSource.setInitializerList(initializerList);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connectionMock.isValid(anyInt())).thenReturn(true);
        when(connectionMock.isClosed()).thenReturn(false);
        when(connectionMock.getAutoCommit()).thenReturn(null);

        singleConnectionDataSource.addInitializer(connectionInitializerMock);

        Connection connection = singleConnectionDataSource.getConnection();
        verify(connectionMock).isValid(anyInt());
        verify(connectionMock).isClosed();
        verify(connectionMock).getAutoCommit();
        verify(connectionInitializerMock).init(connectionMock);

        assertEquals(connection, singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.setUsername("invalidUsername");
        singleConnectionDataSource.setPassword("invalidPassword");

        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);

        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
        verify(connectionInitializer, times(1)).init(any(Connection.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.setConnection(connectionMock);

        singleConnectionDataSource.resetConnection();

        verify(connectionMock).close();
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
        verify(singleConnectionDataSource, times(1)).setConnection(null);
        verify(singleConnectionDataSource, times(1)).setLock(null);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);

        Connection connection = mock(Connection.class);
        singleConnectionDataSource.prepareConnection(connection);

        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setLock(mock(Lock.class));

        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testTryLockWithTimeout() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setLock(mock(Lock.class));

        assertFalse(singleConnectionDataSource.tryLock(1));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = mock(Connection.class);
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();

        singleConnectionDataSource.setLock(mock(Lock.class));

        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);

        assertNotNull(connectionProxy);
        verify(connection, times(1)).isValid(anyInt());
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
        connectionMock = null;
        initializerList = null;
        eventPublisher = null;
        connectionInitializerMock = null;
    }
}