package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        dataSource.setAutoReconnect(true);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        assertEquals(connection, dataSource.getConnection("testUser", "testPassword"));
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "wrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNotNull(dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(connection);
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        assertNotNull(dataSource.getConnectionProxy(connection));
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.setConnection(connection);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(connection);
        // Verify if debug message is logged
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
        assertThrows(IllegalStateException.class, () -> dataSource.innerCreateConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler =
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, mock(Lock.class));
        assertNull(handler.invoke(connection, Connection.class.getMethod("close"), null));
    }
}