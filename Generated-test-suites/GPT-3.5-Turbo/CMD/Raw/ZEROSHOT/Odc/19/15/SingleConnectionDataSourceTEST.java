package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        
        dataSource.initConnection(connection);
        
        verify(initializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
        // No exception means test passes
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setEventPublisher(mock(EventPublisher.class));
        dataSource.onConnectionReset(mock(Connection.class));
        // No exception means test passes
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
    }

}