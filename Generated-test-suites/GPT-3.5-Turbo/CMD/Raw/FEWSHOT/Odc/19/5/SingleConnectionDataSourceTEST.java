package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        dataSource.setConnection(connection);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertNotNull(dataSource.getConnection("user", "pass"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() {
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
}