package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private EventPublisher eventPublisherMock;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
        this.eventPublisherMock = Mockito.mock(EventPublisher.class);
        this.dataSource.setEventPublisher(this.eventPublisherMock);
        try {
            this.connectionMock = Mockito.mock(Connection.class);
        } catch (SQLException e) {
            fail("Failed to create mock connection");
        }
        Mockito.when(this.connectionMock.isClosed()).thenReturn(false);
        Mockito.when(this.connectionMock.isValid(Mockito.anyInt())).thenReturn(true);
        Mockito.when(this.connectionMock.getAutoCommit()).thenReturn(null);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Mockito.when(this.dataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        Connection connection = this.dataSource.getConnection();
        assertNotNull(connection);
        Mockito.verify(this.dataSource).prepareConnection(this.connectionMock);
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        assertThrows(SQLException.class, () -> this.dataSource.getConnection("wrongUsername", "wrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(this.connectionMock.isClosed()).thenReturn(true);
        this.dataSource.resetConnection();
        assertNull(this.dataSource.getConnection());
        Mockito.verify(this.eventPublisherMock).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        assertEquals(1, this.dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testCloseMultipleTimes() throws SQLException {
        this.dataSource.close();
        this.dataSource.close();
        assertNull(this.dataSource.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhileAnotherIsBeingUsed() throws SQLException, InterruptedException {
        this.dataSource.lock = new ReentrantLock();
        SingleConnectionDataSource anotherDataSource = Mockito.spy(new SingleConnectionDataSource());
        anotherDataSource.lock = new ReentrantLock();
        Mockito.when(anotherDataSource.innerCreateConnection()).thenReturn(this.connectionMock);
        anotherDataSource.getConnection();
        assertThrows(ConflictException.class, () -> this.dataSource.getConnection());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Connection proxyConnection = this.dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
        assertSame(connection, proxyConnection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.prepareConnection(connection);
        assertFalse(connection.isAutoCommit());
        Mockito.verify(connection).setAutoCommit(this.dataSource.getAutoCommit());
    }

    @Test
    public void testTryLockSuccessful() {
        assertTrue(this.dataSource.tryLock());
    }

    @Test
    public void testTryLockFailed() {
        assertFalse(this.dataSource.tryLock());
    }

    @Test
    public void testTimeOutWhenLockIsNotGranted() throws InterruptedException {
        assertFalse(this.dataSource.tryLock(1, TimeUnit.MILLISECONDS));
    }

    @After
    public void tearDown() throws Exception {
        this.dataSource = null;
        this.connectionMock = null;
        this.eventPublisherMock = null;
    }
}