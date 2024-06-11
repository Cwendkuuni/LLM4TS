package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.setEventPublisher(mock(EventPublisher.class));
        singleConnectionDataSource.setTimeOutMillis(5000);
    }

    @After
    public void tearDown() throws SQLException {
        singleConnectionDataSource.close();
        singleConnectionDataSource = null;
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("user", "password");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        singleConnectionDataSource.resetConnection();
        Connection newConnection = singleConnectionDataSource.getConnection();
        assertNotNull(newConnection);
        assertFalse(newConnection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

}