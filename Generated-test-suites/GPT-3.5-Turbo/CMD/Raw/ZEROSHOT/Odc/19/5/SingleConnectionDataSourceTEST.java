package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("username", "password");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = connection -> {
            // initialize connection
        };
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = singleConnectionDataSource.getConnectionProxy(mockConnection);
        assertNotNull(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.initConnection(connection);
        // assert initialization
    }

    @Test
    public void testOnConnectionCreated() {
        // mock eventPublisher
        singleConnectionDataSource.onConnectionCreated(mockConnection);
        // assert event
    }

    @Test
    public void testOnConnectionReset() {
        // mock eventPublisher
        singleConnectionDataSource.onConnectionReset(mockConnection);
        // assert event
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(mockConnection, mockLock);
        // test InvocationHandler methods
    }
}