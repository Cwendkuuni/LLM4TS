
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    @Mock
    private Connection mockConnection;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(mockConnection);

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        Connection resultConnectionProxy = singleConnectionDataSource.getConnection();
        assertNotNull(resultConnectionProxy);
        assertTrue(resultConnectionProxy instanceof Connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedFailedConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(false);
        singleConnectionDataSource.setConnection(mockConnection);

        when(mockConnection.isClosed()).thenReturn(true);

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(mockConnection);
        singleConnectionDataSource.setUsername("testuser");
        singleConnectionDataSource.setPassword("testpass");

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        Connection resultConnectionProxy = singleConnectionDataSource.getConnection("testuser", "testpass");
        
        assertNotNull(resultConnectionProxy);
        assertTrue(resultConnectionProxy instanceof Connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithWrongUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
      
        singleConnectionDataSource.getConnection("wronguser", "wrongpass");
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(initializer);
      
        // No exception thrown, reserve something you want 
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(mockConnection);
        singleConnectionDataSource.close();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setConnection(mockConnection);
        singleConnectionDataSource.setAutoCommit(true);
        
        mockConnection.setAutoCommit(false);
        
        singleConnectionDataSource.prepareConnection(mockConnection);

        verify(mockConnection, times(1)).setAutoCommit(true);
    }
}
