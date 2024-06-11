
package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource testDataSource;
    Connection mockConnection;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        testDataSource = new SingleConnectionDataSource() {
            protected Connection getConnectionProxy(Connection connection) {
                return connection;
            }
            
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return mockConnection;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        testDataSource.close();
    }

    @Test
    public void testNewConnectionAndGetConnection() throws Exception {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        Connection c = testDataSource.getConnection();
        assertNotNull(c);
        assertTrue(c.isValid(anyInt()));
        assertFalse(c.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testInvalidConnection() throws Exception {
        when(mockConnection.isClosed()).thenReturn(true);
        testDataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        testDataSource.resetConnection();
        assertNull(testDataSource.connection);
        assertNull(testDataSource.lock);
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        testDataSource.setUsername("test");
        testDataSource.setPassword("password");
        Connection conn = testDataSource.getConnection("test", "password");
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        testDataSource.setUsername("test");
        testDataSource.setPassword("password");
        testDataSource.getConnection("invalid", "invalid");
    }

    @Test
    public void testClose() throws Exception {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        Connection c = testDataSource.getConnection();
        assertNotNull(c);
        testDataSource.close();
        assertTrue(c.isClosed());
    }
}
