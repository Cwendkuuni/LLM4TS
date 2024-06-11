package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
    public void testGetConnectionWithUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("test", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.initConnection(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.innerCreateConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        Connection connection = mock(Connection.class);
        assertEquals(connection, dataSource.new CloseIgnoreInvocationHandler(connection, dataSource.getLock()).invoke(mock(Connection.class), mock(Method.class), new Object[] {}));
    }
}