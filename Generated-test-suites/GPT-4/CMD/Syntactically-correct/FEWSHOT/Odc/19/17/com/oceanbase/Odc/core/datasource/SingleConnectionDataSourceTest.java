package com.oceanbase.odc.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        try {
            ds.setAutoCommit(true);
            Connection conn = ds.getConnection("user", "password");
            assertNotNull(conn);
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testInvalidUsernameOrPassword() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        try {
            ds.getConnection("invalidUser", "invalidPass");
            fail("Should have thrown SQLException due to wrong username or password");
        } catch (SQLException sqle) {
            assertEquals("Invalid username or password", sqle.getMessage());
        }
    }

    @Test
    public void testgetAutoCommitValue() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getAutoCommit());
    }

    @Test
    public void testAutoReconnect() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        try {
            assertFalse(ds.autoReconnect);
            ds = new SingleConnectionDataSource(true);
            assertTrue(ds.autoReconnect);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        try {
            ds.resetConnection();
            fail("Should have thrown null pointer exception since connection is null initially");
        } catch (NullPointerException npe) {
            // Test passed.
        } catch (SQLException sqle) {
            fail(sqle.getMessage());
        }
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource ds = Mockito.spy(new SingleConnectionDataSource());
        ds.close();
        Mockito.verify(ds, Mockito.times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        try {
            SingleConnectionDataSource ds = new SingleConnectionDataSource();
            ConnectionInitializer initializer = conn -> {};
            ds.addInitializer(initializer);
            // No exception means test passed
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}