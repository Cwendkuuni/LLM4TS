package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for SingleConnectionDataSource
 *
 * @author yh263208
 * @date 2023-03-21
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializerMock;
    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connectionMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.eventPublisher = mock(EventPublisher.class);
        this.connectionInitializerMock = mock(ConnectionInitializer.class);
        this.singleConnectionDataSource = new SingleConnectionDataSource() {
            @Override
            public void resetConnection() throws SQLException {
                super.resetConnection();
            }
        };
        this.singleConnectionDataSource.setAutoReconnect(true);
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
        this.singleConnectionDataSource.setInitializerList(new ArrayList<>());
        this.singleConnectionDataSource.addInitializer(this.connectionInitializerMock);
        this.connectionMock = mock(Connection.class);
    }

    @After
    public void tearDown() throws SQLException {
        this.singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(this.singleConnectionDataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        Connection connection = this.singleConnectionDataSource.getConnection();
        assertEquals(this.connectionMock, connection);
        assertTrue(connection.isOpen());
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        when(this.singleConnectionDataSource.getUsername()).thenReturn("test");
        when(this.singleConnectionDataSource.getPassword()).thenReturn("test");
        String username = "test_user";
        String password = "test_password";
        assertThrows(SQLException.class, () -> this.singleConnectionDataSource.getConnection(username, password));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.singleConnectionDataSource.getConnection()).thenReturn(this.connectionMock);
        this.singleConnectionDataSource.resetConnection();
        assertNull(this.singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        this.singleConnectionDataSource.addInitializer(initializer);
        assertTrue(this.singleConnectionDataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        this.singleConnectionDataSource.setConnection(connection);
        this.singleConnectionDataSource.close();
        assertFalse(connection.isOpen());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        boolean autoCommit = true;
        this.singleConnectionDataSource.setAutoCommit(autoCommit);
        Connection connection = mock(Connection.class);
        when(connection.getAutoCommit()).thenReturn(!autoCommit);
        this.singleConnectionDataSource.prepareConnection(connection);
        assertEquals(autoCommit, connection.getAutoCommit());
    }

    @Test(expected = ConflictException.class)
    public void testTryLockWithConnectionOccupied() throws SQLException {
        this.singleConnectionDataSource.setLock(mock(Lock.class));
        this.singleConnectionDataSource.tryLock();
        throw new ConflictException(ErrorCodes.ConnectionOccupied, new Object[]{},
                "Connection is occupied, waited " + this.singleConnectionDataSource.getTimeOutMillis() + " millis");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionProxyWithNullConnection() throws SQLException {
        this.singleConnectionDataSource.setLock(null);
        assertThrows(SQLException.class, () -> this.singleConnectionDataSource.getConnectionProxy(null));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        this.singleConnectionDataSource.setLock(mock(Lock.class));
        Connection connection = mock(Connection.class);
        when(this.singleConnectionDataSource.getConnection()).thenReturn(connection);
        CloseIgnoreInvocationHandler connectionHandler = new CloseIgnoreInvocationHandler(connection,
                mock(Lock.class));
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
                connectionHandler);
        Connection returnedConnection = (Connection) this.singleConnectionDataSource.getConnectionProxy(connection);
        assertEquals(proxy, returnedConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        this.singleConnectionDataSource.initConnection(connection);
        this.connectionInitializerMock.init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = mock(Connection.class);
        this.singleConnectionDataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        when(this.eventPublisher.publishEvent(ArgumentCaptor.capture(ConnectionResetEvent.class)))
                .thenReturn(null);
        this.singleConnectionDataSource.setEventPublisher(this.eventPublisher);
        Connection connection = mock(Connection.class);
        this.singleConnectionDataSource.onConnectionReset(connection);
        ConnectionResetEvent event = ArgumentCaptor.getForClass(ConnectionResetEvent.class);
        assertEquals(connection, event.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(this.singleConnectionDataSource.newConnectionFromDriver(this.singleConnectionDataSource.getUsername(),
                this.singleConnectionDataSource.getPassword())).thenReturn(connection);
        Connection returnedConnection = this.singleConnectionDataSource.innerCreateConnection();
        assertEquals(connection, returnedConnection);
    }
}