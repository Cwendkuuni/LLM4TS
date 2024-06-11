package com.oceanbase.odc.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername(USERNAME);
        singleConnectionDataSource.setPassword(PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        singleConnectionDataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isWrapperFor(Connection.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithDifferentUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection initialConnection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        Connection resetConnection = singleConnectionDataSource.getConnection();
        assertNotNull(resetConnection);
        assertTrue(resetConnection.isWrapperFor(Connection.class));
        assertEquals(initialConnection, resetConnection);
    }

    @Test
    public void testAddInitializer() {
        int initialSize = singleConnectionDataSource.getInitializerList().size();
        singleConnectionDataSource.addInitializer(connection -> {});
        int finalSize = singleConnectionDataSource.getInitializerList().size();
        assertEquals(initialSize + 1, finalSize);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        singleConnectionDataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        Connection connection = singleConnectionDataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());
    }
}