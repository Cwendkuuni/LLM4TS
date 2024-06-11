package com.oceanbase.odc.core.datasource;

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        dataSource.close();
    }

    @Test(expected = SQLException.class)
    public void testInvalidUsernamePassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("testUser", "testPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection1 = dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();
        assertNotEquals(connection1, connection2);
        dataSource.close();
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // Initialization logic here
            }
        };
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        dataSource.close();
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

}