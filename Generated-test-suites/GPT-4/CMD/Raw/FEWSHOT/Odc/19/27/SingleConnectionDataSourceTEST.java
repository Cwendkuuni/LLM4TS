
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        dataSource.setTimeOutMillis(TimeUnit.SECONDS.toMillis(5));
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue("connection should be open", !connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void getConnectionWithInvalidUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        assertTrue(!connection1.isClosed());

        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();
        assertTrue(!connection2.isClosed());

        assertNotSame(connection1, connection2);
    }

    @Test
    public void testInitializeWithInvalidConnectionInitializer() {
        dataSource.addInitializer(conn -> {throw new SQLException("Failed to initialize");});
        try {
            dataSource.getConnection();
            fail("Should have thrown exception");
        } catch (SQLException ex) {
            // Expected exception
        }
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());

        dataSource.close();
        assertTrue(connection.isClosed());
    }
}
