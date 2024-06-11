
package com.oceanbase.odc.core.datasource;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection mockConnection;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(false);
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(mockConnection);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isClosed();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(mockConnection);
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        // test valid case
        Connection connection = dataSource.getConnection("test", "test");
        assertNotNull(connection);
        verify(mockConnection, times(1)).isClosed();
        
        // test invalid case
        try {
            dataSource.getConnection("invalid", "invalid");
            fail("Expected SQLException not thrown");
        } catch(SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(mockConnection);
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(mockConnection);
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }
}
