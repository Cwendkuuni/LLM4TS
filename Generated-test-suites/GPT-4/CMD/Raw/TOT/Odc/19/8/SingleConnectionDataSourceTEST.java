
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import java.sql.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void test_getConnection() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;
        
        Connection resultConnection = singleConnectionDataSource.getConnection();
        
        assertNotNull(resultConnection);
        assertNotEquals(resultConnection, mockConnection);
    }

    @Test(expected=SQLException.class)
    public void test_getConnection_exception() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;
        
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void test_getConnectionWithCredentials() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.setUsername("testUser");
        singleConnectionDataSource.setPassword("testPass");
        
        Connection resultConnection = singleConnectionDataSource.getConnection("testUser", "testPass");
        
        assertNotNull(resultConnection);
        assertNotEquals(resultConnection, mockConnection);
    }

    @Test(expected=SQLException.class)
    public void test_getConnectionWithCredentials_exception() throws Exception {
        singleConnectionDataSource.setUsername("testUser");
        singleConnectionDataSource.setPassword("testPass");
        
        singleConnectionDataSource.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void test_resetConnection() throws Exception {
        Connection mockConnection = mock(Connection.class);
        singleConnectionDataSource.connection = mockConnection;
        
        singleConnectionDataSource.resetConnection();
        
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void test_addInitializer() throws Exception {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        
        singleConnectionDataSource.addInitializer(mockInitializer);
        
        assertTrue(singleConnectionDataSource.initializerList.contains(mockInitializer));
    }

    @Test
    public void test_close() throws Exception {
        Connection mockConnection = mock(Connection.class);
        singleConnectionDataSource.connection = mockConnection;
        
        singleConnectionDataSource.close();
        
        verify(mockConnection).close();
    }
}
