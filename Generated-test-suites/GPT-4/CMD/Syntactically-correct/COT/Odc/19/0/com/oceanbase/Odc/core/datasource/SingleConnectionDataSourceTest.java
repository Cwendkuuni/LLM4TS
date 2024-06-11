package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true);
    }

    @Test
    public void testGetConnection() throws SQLException {
        doReturn(true).when(connection).isClosed();
        doReturn(false).when(connection).isValid(anyInt());

        dataSource.connection = connection;
        dataSource.getConnection();

        verify(connection).close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalid() throws SQLException {
        doReturn(true).when(connection).isClosed();
        doReturn(true).when(connection).isValid(anyInt());

        dataSource.connection = connection;
        dataSource.getConnection();

        verify(connection, never()).close();
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");

        dataSource.connection = connection;  
        dataSource.getConnection("username", "password");

        verify(connection, never()).close();
    }
    
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");

        dataSource.connection = connection;
        dataSource.getConnection("wrongusername", "wrongpassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.connection = connection;

        dataSource.resetConnection();

        verify(connection).close();
    }
    
    @Test(expected = SQLException.class)
    public void testResetConnectionException() throws SQLException {
        doThrow(new SQLException()).when(connection).close();
        
        dataSource.connection = connection;
        dataSource.resetConnection(); 
    }

    @Test(expected = RuntimeException.class)
    public void testPrepareConnection() throws SQLException {
        doReturn(true).when(connection).getAutoCommit();
        
        dataSource.connection = connection;
        dataSource.setAutoCommit(false);
        dataSource.prepareConnection(connection);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddInitializerNullArgument() {
        dataSource.addInitializer(null);
    }
    
    @Test
    public void testClose() {
        dataSource.connection = connection;

        dataSource.close();

        verify(connection).shutdown();
    }
}
