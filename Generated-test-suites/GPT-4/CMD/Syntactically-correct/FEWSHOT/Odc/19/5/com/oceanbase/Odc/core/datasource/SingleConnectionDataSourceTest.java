
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }    
    
    @After
    public void tearDown() {
        dataSource.close();
    }    
    
    @Test
    public void testGetConnectionNoPreviousConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        dataSource.getConnection();
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testGetConnectionWithPreviousConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertEquals(conn, dataSource.getConnection());
    }
    
    @Test(expected = SQLException.class)
    public void testInvalidGetConnection() throws SQLException {
        dataSource.getConnection("unknownuser", "badpassword");
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testAutoCommitChange() throws SQLException {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getAutoCommit());
    }
    
    @Test
    public void testAddInitializer() throws SQLException {
        dataSource.addInitializer(conn -> { /* Empty implementation for test purposes */ });
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.connection);
    }
    
    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection conn = dataSource.getConnection();
        assertTrue(conn.getAutoCommit());
    }
    
    @Test
    public void testTryLock() throws SQLException, InterruptedException {
        Connection conn = dataSource.getConnection();
        Thread.sleep(200);
        assertFalse(dataSource.tryLock());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDoubleInnerCreateConnectionException() throws SQLException {
        dataSource.innerCreateConnection();
        dataSource.innerCreateConnection();
    }
}
