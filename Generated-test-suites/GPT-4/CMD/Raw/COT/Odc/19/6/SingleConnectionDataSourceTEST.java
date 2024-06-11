
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.Connection;
import java.sql.SQLException;

@RunWith(JUnit4.class)
public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
    }

    @Test
    public void testSingleConnectionDataSourceWithParam() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(true);
        assertNotNull(ds);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(false, connection.isClosed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetConnectionWithInvalidArguments() throws SQLException {
        dataSource.getConnection("wrong-username", "wrong-password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.resetConnection();
        Connection resetConnection = dataSource.getConnection();
        assertNotNull(resetConnection);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
    }

    @Test(expected = IllegalStateException.class)
    public void innerCreateConnectionTwiceThrowsIllegalStateException() throws SQLException {
        dataSource.getConnection();
        dataSource.innerCreateConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = conn -> {};
        dataSource.addInitializer(initializer);
    }
}
