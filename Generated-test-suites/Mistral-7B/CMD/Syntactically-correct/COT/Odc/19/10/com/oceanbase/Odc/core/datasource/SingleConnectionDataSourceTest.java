package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.ConnectionInitializedEvent;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;

    @Before
    public void setUp() throws SQLException {
        this.dataSource = new SingleConnectionDataSource();
        this.connection = Mockito.mock(Connection.class);
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.addInitializer(connectionInitializer = Mockito.mock(ConnectionInitializer.class));
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosedOrInvalid() throws SQLException {
        Mockito.when(connection.isClosed()).thenReturn(true);
        dataSource.setConnection(connection);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWhenConnectionIsValid() throws SQLException {
        Mockito.when(connection.isClosed()).thenReturn(false);
        dataSource.setConnection(connection);

        Connection returnedConnection = dataSource.getConnection();
        assertSame(connection, returnedConnection);
    }

    @Test
    public void testGetConnectionWhenUsernameAndPasswordDoNotMatch() throws SQLException {
        assertFalse(dataSource.supportsProperty(Constants.USER));
        assertFalse(dataSource.supportsProperty(Constants.PASS));

        assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUsername", "wrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.resetConnection();
        assertNull(dataSource.getConnection());

        Connection newConnection = Mockito.mock(Connection.class);
        Mockito.when(connection.isValid(Mockito.anyInt())).thenReturn(false);
        Mockito.when(newConnection.isValid(Mockito.anyInt())).thenReturn(true);
        dataSource.setConnection(connection);
        dataSource.resetConnection();
        Connection resetConnection = dataSource.getConnection();
        assertSame(newConnection, resetConnection);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);
        assertTrue(dataSource.initializerList.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);
        Boolean autoCommit = Boolean.TRUE;

        dataSource.prepareConnection(testConnection);
        assertSame(autoCommit, testConnection.getAutoCommit());
    }

    @Test
    public void testTryLock() throws InterruptedException {
        dataSource.lock = Mockito.mock(ReentrantLock.class);

        assertTrue(dataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testTryLockWhenTimeoutExceeded() throws InterruptedException, SQLException {
        dataSource.lock = Mockito.mock(ReentrantLock.class);
        Mockito.when(dataSource.lock.tryLock(1, TimeUnit.MILLISECONDS)).thenReturn(false);

        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);
        dataSource.lock = Mockito.mock(ReentrantLock.class);

        Connection proxyConnection = dataSource.getConnectionProxy(testConnection);
        assertSame(testConnection, proxyConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);

        dataSource.initConnection(testConnection);

        Mockito.verify(connectionInitializer).init(testConnection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);

        dataSource.onConnectionCreated(testConnection);

        Mockito.verify(eventPublisher).publishEvent(Mockito.isInstanceOf(ConnectionCreatedEvent.class));
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);
        ConnectionResetEvent event = new ConnectionResetEvent(testConnection);

        dataSource.onConnectionReset(testConnection);

        Mockito.verify(eventPublisher).publishEvent(event);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection testConnection = Mockito.mock(Connection.class);

        dataSource.setConnection(null);
        dataSource.setLock(Mockito.mock(ReentrantLock.class));

        Connection returnedConnection = dataSource.innerCreateConnection();
        assertSame(testConnection, returnedConnection);
    }
}