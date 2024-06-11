package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;

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
    }

    @Test
    public void testGetConnectionWithUsernamePassword() {
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connection -> {
            // do initialization
        });
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.prepareConnection(connection);
        // assert preparation
    }

    @Test
    public void testTryLock() {
        boolean locked = singleConnectionDataSource.tryLock();
        assertTrue(locked);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.initConnection(connection);
        // assert initialization
    }

    @Test
    public void testOnConnectionCreated() {
        singleConnectionDataSource.onConnectionCreated(null);
        // assert action after connection created
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

}