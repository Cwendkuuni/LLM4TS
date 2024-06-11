package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-25 10:30
 * @since ODC_release_X.X.X
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final long TIME_OUT_MILLISECONDS = 10 * 1000;

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private ConnectionInitializer connectionInitializerMock;
    private EventPublisher eventPublisherMock;
    private List<ConnectionInitializer> initializerList;
    private Lock lock;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionMock = Mockito.mock(Connection.class);
        this.connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.initializerList = new LinkedList<>();
        this.initializerList.add(this.connectionInitializerMock);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
        this.dataSource.setInitializerList(this.initializerList);
        this.dataSource.setAutoReconnect(true);
        this.lock = new ReentrantLock();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Mockito.when(this.connectionMock.isValid(ArgumentMatchers.longThat((Long l) -> l > 0))).thenReturn(true);

        this.dataSource.addInitializer(this.connectionInitializerMock);

        Mockito.doNothing().when(this.connectionInitializerMock).init(this.connectionMock);

        Connection connection = this.dataSource.getConnection();

        assertNotNull(connection);
        Mockito.verify(this.connectionMock, Mockito.times(1)).isValid(ArgumentMatchers.longThat((Long l) -> l > 0));
        Mockito.verify(this.connectionInitializerMock, Mockito.times(1)).init(this.connectionMock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        this.dataSource.setUsername("invalid_username");
        this.dataSource.setPassword("invalid_password");

        try {
            this.dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }

        String message = e.getMessage();
        assertTrue(message.contains("Invalid username or password"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionConnectionIsClosed() throws SQLException {
        Mockito.when(this.connectionMock.isClosed()).thenReturn(true);

        try {
            this.dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }

        String message = e.getMessage();
        assertTrue(message.contains("Connection was closed or not valid"));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionConnectionIsOccupied() throws SQLException {
        this.dataSource.setLock(new ReentrantLock());
        this.dataSource.setTimeOutMillis(0);

        try {
            Connection connection1 = this.dataSource.getConnection();
            Connection connection2 = this.dataSource.getConnection();
            fail();
        } catch (Exception e) {
            // Expected
        }

        String message = e.getMessage();
        assertTrue(message.contains("Connection is occupied"));
    }

    @Test
    public void testGetConnectionAutoCommit() throws SQLException {
        this.dataSource.setAutoCommit(true);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testGetConnectionAutoCommitFalse() throws SQLException {
        this.dataSource.setAutoCommit(false);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithWrongCredentials() throws SQLException {
        this.dataSource.setUsername("wrong_username");
        this.dataSource.setPassword("wrong_password");

        try {
            this.dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }

        String message = e.getMessage();
        assertTrue(message.contains("Invalid username or password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = this.dataSource.getConnection();

        this.dataSource.resetConnection();

        assertNull(this.dataSource.getConnection());

        Mockito.verify(this.eventPublisherMock, Mockito.times(1)).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);

        this.dataSource.addInitializer(connectionInitializer);

        assertEquals(1, this.dataSource.getInitializerList().size());
        Mockito.verify(connectionInitializer, Mockito.times(1)).init(ArgumentMatchers.any(Connection.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddInitializerNull() {
        try {
            this.dataSource.addInitializer(null);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        String message = e.getMessage();
        assertTrue(message.contains("initializer cannot be null"));
    }

    @Test
    public void testSetAutoReconnect() {
        this.dataSource.setAutoReconnect(false);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);

        connection.close();

        try {
            this.dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }

        Mockito.verify(this.eventPublisherMock, Mockito.times(0)).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testSetAutoReconnectTrue() throws SQLException {
        this.dataSource.setAutoReconnect(true);

        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);

        connection.close();

        Connection connectionReset = this.dataSource.getConnection();
        assertNotNull(connectionReset);

        Mockito.verify(this.eventPublisherMock, Mockito.times(1)).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() throws SQLException {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }
}