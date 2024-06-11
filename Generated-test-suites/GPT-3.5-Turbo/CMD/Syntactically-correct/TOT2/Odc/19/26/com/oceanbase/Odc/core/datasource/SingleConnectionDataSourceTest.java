package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testAutoCommitSetterGetter() {
        dataSource.setAutoCommit(true);
        assertEquals(true, dataSource.getAutoCommit());
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
    }

    @Test(expected = SQLException.class)
    public void testInvalidCredentials() throws SQLException {
        dataSource.getConnection("testUser", "testPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = connection -> {
            // do nothing
        };

        dataSource.addInitializer(mockInitializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

}