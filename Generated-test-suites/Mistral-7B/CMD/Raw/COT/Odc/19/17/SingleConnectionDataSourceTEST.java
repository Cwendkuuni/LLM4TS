package com.oceanbase.odc.core.datasource.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection mockConnection;
    private EventPublisher mockEventPublisher;
    private List<ConnectionInitializer> initializerList;
    private ReentrantLock mockLock;

    @Before
    public void setUp() throws Exception {
        this.mockConnection = Mockito.mock(Connection.class);
        this.mockEventPublisher = Mockito.mock(EventPublisher.class);
        this.initializerList = new ArrayList<>();
        this.mockLock = new ReentrantLock();

        this.dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection innerCreateConnection() throws SQLException {
                return this.mockConnection;
            }

            @Override
            public void addInitializer(ConnectionInitializer connectionInitializer) {
                this.initializerList.add(connectionInitializer);
            }

            @Override
            protected void initConnection(Connection connection) throws SQLException {
                super.initConnection(connection);
            }

            @Override
            protected void onConnectionCreated(Connection connection) {
                super.onConnectionCreated(connection);
            }

            @Override
            protected void onConnectionReset(Connection connection) {
                super.onConnectionReset(connection);
            }

            @Override
            public void close() {
                super.close();
            }

            @Override
            protected Connection newConnectionFromDriver(String username, String password)
                    throws SQLException {
                return this.mockConnection;
            }
        };

        this.dataSource.setEventPublisher(this.mockEventPublisher);
        Mockito.when(this.mockConnection.isValid(ArgumentMatchers.longValue(0)))
                .thenReturn(true);
        Mockito.when(this.mockConnection.getAutoCommit()).thenReturn(null);
    }

    @After
    public void tearDown() throws Exception {
        this.dataSource = null;
        this.mockConnection = null;
        this.mockEventPublisher = null;
        this.initializerList = null;
        this.mockLock = null;
    }

    /**
     * Test getConnection() method
     *
     * @throws SQLException
     */
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    /**
     * Test getConnection() method with invalid connection
     *
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        Mockito.when(this.mockConnection.isValid(ArgumentMatchers.longValue(0)))
                .thenReturn(false);
        this.dataSource.getConnection();
    }

    /**
     * Test getConnection() method with different username and password
     *
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void testGetConnectionDifferentCredentials() throws SQLException {
        this.dataSource.setUsername("testUsername");
        this.dataSource.setPassword("testPassword");
        this.dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    /**
     * Test resetConnection() method
     */
    @Test
    public void testResetConnection() {
        this.dataSource.resetConnection();
        assertNull(this.dataSource.connection);
        assertNull(this.dataSource.lock);

        Connection newConnection = Mockito.mock(Connection.class);
        this.dataSource.innerCreateConnection();
        assertEquals(newConnection, this.dataSource.connection);
        assertEquals(newConnection, this.dataSource.getConnection());
        assertTrue(this.dataSource.lock.tryLock());
    }

    /**
     * Test addInitializer() method
     */
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        assertEquals(1, this.dataSource.initializerList.size());
    }

    /**
     * Test close() method
     */
    @Test
    public void testClose() throws Exception {
        this.dataSource.close();
        assertNull(this.dataSource.connection);
    }

    /**
     * Test prepareConnection() method
     *
     * @throws SQLException
     */
    @Test
    public void testPrepareConnection() throws SQLException {
        this.dataSource.setAutoCommit(true);
        this.dataSource.prepareConnection(this.mockConnection);
        assertTrue(this.mockConnection.getAutoCommit());

        this.dataSource.setAutoCommit(false);
        this.dataSource.prepareConnection(this.mockConnection);
        assertFalse(this.mockConnection.getAutoCommit());
    }

    /**
     * Test tryLock() method
     */
    @Test
    public void testTryLock() {
        assertTrue(this.dataSource.tryLock());
    }

    /**
     * Test tryLock() method with timeout
     */
    @Test(expected = InterruptedException.class)
    public void testTryLockTimeout() {
        // timeout is 1ms, so it should throw InterruptedException
        assertFalse(this.dataSource.tryLock(1, TimeUnit.MILLISECONDS));
    }

    /**
     * Test getConnectionProxy() method
     */
    @Test
    public void testGetConnectionProxy() throws Exception {
        assertTrue(this.dataSource.tryLock());

        Connection connectionProxy = this.dataSource.getConnectionProxy(this.mockConnection);
        assertEquals(this.mockConnection, connectionProxy);

        this.dataSource.close();
        try {
            this.dataSource.getConnectionProxy(this.mockConnection);
            fail("ConnectionProxy should be closed after DataSource close");
        } catch (SQLException e) {
            // expected
        }
    }

    /**
     * Test initConnection() method
     *
     * @throws SQLException
     */
    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        this.dataSource.initConnection(this.mockConnection);
        Mockito.verify(initializer).init(this.mockConnection);
    }

    /**
     * Test onConnectionCreated() method
     */
    @Test
    public void testOnConnectionCreated() throws SQLException {
        this.dataSource.onConnectionCreated(this.mockConnection);
        // no verification needed as this method doesn't have any side effects
    }

    /**
     * Test onConnectionReset() method
     */
    @Test
    public void testOnConnectionReset() throws SQLException {
        this.dataSource.onConnectionReset(this.mockConnection);
        Mockito.verify(this.mockEventPublisher, Mockito.times(1))
                .publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }
}