package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.event.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Tests for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @date 2023-03-02
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private DriverManager driverManager;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(driverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mock(Connection.class));
        when(driverManager.getLoginTimeout()).thenReturn(10);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertEquals(singleConnectionDataSource.getConnection(), connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUsername";
        String password = "testPassword";
        singleConnectionDataSource.setUsername(USERNAME);
        singleConnectionDataSource.setPassword(PASSWORD);
        Connection connection = singleConnectionDataSource.getConnection(username, password);
        assertEquals(singleConnectionDataSource.getConnection(), connection);
        verify(driverManager).getConnection(USERNAME, PASSWORD);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        String invalidUsername = "invalidUsername";
        String invalidPassword = "invalidPassword";
        singleConnectionDataSource.setUsername(USERNAME);
        singleConnectionDataSource.setPassword(PASSWORD);
        singleConnectionDataSource.getConnection(invalidUsername, invalidPassword);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
        ArgumentCaptor<ConnectionResetEvent> eventArgumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher).publishEvent(eventArgumentCaptor.capture());
        ConnectionResetEvent event = eventArgumentCaptor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
        assertEquals(connectionInitializer, singleConnectionDataSource.getInitializerList().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullInitializer() {
        singleConnectionDataSource.addInitializer(null);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        Connection connectionProxy = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setConnectionProxy(connectionProxy);
        singleConnectionDataSource.close();
        verify(connection).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        Boolean autoCommit = true;
        singleConnectionDataSource.setAutoCommit(autoCommit);
        singleConnectionDataSource.prepareConnection(connection);
        assertEquals(autoCommit, connection.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testCreateConnectionWithException() throws SQLException {
        Exception exception = new SQLException("Test exception");
        when(driverManager.getConnection(anyString(), anyString())).thenThrow(exception);
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection());
    }

    @Test
    public void testTryLock() {
        singleConnectionDataSource.lock = new ReentrantLock();
        assertFalse(singleConnectionDataSource.tryLock());
        singleConnectionDataSource.lock = new ReentrantLock();
        singleConnectionDataSource.lock.lock();
        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setLock(new ReentrantLock());
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);
        assertSame(connectionProxy, connection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithTimeOut() throws SQLException {
        singleConnectionDataSource.lock = new ReentrantLock();
        when(driverManager.getConnection(anyString(), anyString())).thenReturn(mock(Connection.class));
        singleConnectionDataSource.setTimeOutMillis(5);
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setInitializerList(new ArrayList<>());
        singleConnectionDataSource.setInitializerList(List.of(connectionInitializer));
        singleConnectionDataSource.initConnection(connection);
        verify(connectionInitializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
        connectionInitializer = null;
        driverManager = null;
        eventPublisher = null;
    }
}