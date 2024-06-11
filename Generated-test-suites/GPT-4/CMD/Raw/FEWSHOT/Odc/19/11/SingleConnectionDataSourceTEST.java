
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource dataSource;
    String username = "test";
    String password = "test";

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        dataSource.innerCreateConnection();
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testGetConnectionWithValidCredentials() throws SQLException {
        dataSource.innerCreateConnection();
        Connection connection = dataSource.getConnection(username, password);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.innerCreateConnection();
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.innerCreateConnection();
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
        dataSource.close();
        assertTrue(dataSource.getConnection().isClosed());
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() {
        assertFalse(dataSource.tryLock());
        dataSource.resetConnection();
        dataSource.innerCreateConnection();
        assertTrue(dataSource.tryLock());
    }
}
