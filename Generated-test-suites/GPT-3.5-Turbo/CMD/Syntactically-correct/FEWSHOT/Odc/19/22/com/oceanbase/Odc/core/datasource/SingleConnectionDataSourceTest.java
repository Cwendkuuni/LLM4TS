package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
        
        dataSource.setAutoReconnect(true);
        assertNotNull(dataSource.getConnection());
        
        assertNotNull(dataSource.getConnection("user", "pass"));
        assertNull(dataSource.getConnection("invalid", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertFalse(dataSource.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(dataSource.getConnection());
        assertTrue(dataSource.getConnection().getAutoCommit());
        
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(dataSource.getConnection());
        assertFalse(dataSource.getConnection().getAutoCommit());
    }

    private static class TestConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) throws Exception {
            // Test initialization
        }
    }

}