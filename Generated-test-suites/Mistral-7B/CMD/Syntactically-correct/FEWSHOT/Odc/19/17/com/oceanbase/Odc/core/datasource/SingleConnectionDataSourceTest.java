package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.common.event.EventPublisherImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Test case for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private EventPublisher eventPublisher;
    private Connection connection;
    private ConnectionInitializer initializerMock;
    private List<ConnectionInitializer> initializerList;
    private SingleConnectionDataSource dataSource;
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventCaptor;

    @Before
    public void setUp() {
        eventPublisher = new EventPublisherImpl();
        connection = mock(Connection.class);
        initializerMock = mock(ConnectionInitializer.class);
        initializerList = new ArrayList<>();
        initializerList.add(initializerMock);
        dataSource = new SingleConnectionDataSource(false, eventPublisher) {
            @Override
            protected List<ConnectionInitializer> getInitializerList() {
                return initializerList;
            }
        };
        connectionResetEventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
    }

    @After
    public void tearDown() {
        dataSource = null;
        initializerMock = null;
        initializerList = null;
        connection = null;
        eventPublisher = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isValid(eq(getLoginTimeout())))
                .thenReturn(true);

        Connection returnedConnection = dataSource.getConnection();
        assertSame(connection, returnedConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws SQLException {
        when(connection.isValid(eq(getLoginTimeout())))
                .thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernameOrPassword() throws SQLException {
        when(connection.isValid(eq(getLoginTimeout())))
                .thenReturn(true);

        dataSource.setUsername("username1");
        dataSource.setPassword("password1");

        try {
            dataSource.getConnection("username2", "password2");
        } catch (Exception e) {
            throw new SQLException("Invalid username or password", e);
        }
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionConnectionOccupied() throws SQLException, InterruptedException {
        when(connection.isValid(eq(getLoginTimeout())))
                .thenReturn(true);

        dataSource.setAutoCommit(true);
        dataSource.setTimeOutMillis(1);

        dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connection.isValid(eq(getLoginTimeout())))
                .thenReturn(true);

        dataSource.setAutoCommit(true);

        Connection resetConnection = dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        assertSame(connection, resetConnection);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(initializerMock);
        verify(initializerMock).init(connection);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        assertFalse(tryLock());
    }

    @Test
    public void testTryLock() {
        assertTrue(tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testTryLockTimeout() throws SQLException, InterruptedException {
        assertFalse(tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = true;
        dataSource.setAutoCommit(autoCommit);
        prepareConnection(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        initConnection(connection);
        verify(initializerMock).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection newConnection = newConnectionFromDriver(getUsername(), getPassword());
        dataSource.onConnectionReset(newConnection);
        verify(eventPublisher).publishEvent(connectionResetEventCaptor.capture());
        ConnectionResetEvent connectionResetEvent = connectionResetEventCaptor.getValue();
        assertSame(newConnection, connectionResetEvent.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection returnedConnection = dataSource.innerCreateConnection();
        assertSame(connection, returnedConnection);
    }

    private boolean tryLock() {
        return dataSource.lock.tryLock(dataSource.timeOutMillis, TimeUnit.MILLISECONDS);
    }

}