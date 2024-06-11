package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherMock;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionOccupiedException;

/**
 * Tests for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {
    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";
    private static final String TEST_DRIVER = "test_driver";
    private static final String TEST_URL = "jdbc:test:test_db";
    private static final String TEST_DATABASE = "test_database";

    @Mock
    private ConnectionInitializer mockConnectionInitializer;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionResetEvent mockConnectionResetEvent;
    @Mock
    private EventPublisherMock eventPublisherMock;
    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connection;
    private ReentrantLock lock;
    private CloseIgnoreInvocationHandler closeIgnoreInvocationHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        closeIgnoreInvocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
        when(eventPublisherMock.publishEvent(any()))
                .thenReturn(new VoidEvent(0, null));

        try {
            Class.forName(TEST_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        when(connection.getAutoCommit()).thenReturn(Boolean.FALSE);
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ConnectionInitializer initializer = (ConnectionInitializer) args[0];
            initializer.init(connection);
            return null;
        }).when(mockConnectionInitializer).init(connection);
    }

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        try {
            singleConnectionDataSource.setUsername(TEST_USERNAME);
            singleConnectionDataSource.setPassword(TEST_PASSWORD);
            Connection connection = singleConnectionDataSource.getConnection();
            verify(connection).createStatement();
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        Connection connection = singleConnectionDataSource.getConnection(TEST_USERNAME, TEST_PASSWORD);
        verify(connection).createStatement();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() {
        when(connection.createStatement()).thenThrow(new SQLException("Invalid username or password"));
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword(TEST_PASSWORD);
        singleConnectionDataSource.getConnection(TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword(TEST_PASSWORD);
        singleConnectionDataSource.addInitializer(mockConnectionInitializer);

        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        verify(eventPublisherMock).publishEvent(mockConnectionResetEvent);

        Connection newConnection = singleConnectionDataSource.getConnection();
        verify(connection).close();
        verify(mockConnectionInitializer).init(newConnection);
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWithNoConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
        singleConnectionDataSource.resetConnection();
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.addInitializer(mockConnectionInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNullInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.addInitializer(null);
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.close();
        verify(connection).close();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseWithNullConnection() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setLock(lock);
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);
        assertEquals(connection, connectionProxy);
        verify(lock).lock();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithConnectionOccupied() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setLock(lock);
        singleConnectionDataSource.getConnectionProxy(connection);
        singleConnectionDataSource.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setAutoCommit(Boolean.TRUE);
        singleConnectionDataSource.setInitializerList(Arrays.asList(mockConnectionInitializer));
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword(TEST_PASSWORD);
        Connection connection = singleConnectionDataSource.newConnectionFromDriver(TEST_USERNAME, TEST_PASSWORD);
        singleConnectionDataSource.initConnection(connection);
        verify(mockConnectionInitializer).init(connection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword(TEST_PASSWORD);
        Connection connection = singleConnectionDataSource.innerCreateConnection();
        verify(connection).createStatement();
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        assertTrue(singleConnectionDataSource.tryLock());
        assertFalse(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testOnConnectionReset() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisherMock);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisherMock).publishEvent(mockConnectionResetEvent);
    }
}