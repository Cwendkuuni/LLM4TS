package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private Connection connection;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
    }
    
    @After
    public void tearDown() {
        singleConnectionDataSource.close();
        singleConnectionDataSource = null;
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);

        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        singleConnectionDataSource.setConnection(null);
        Connection result = singleConnectionDataSource.getConnection();

        Assert.assertNotNull(result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUserNamePasswordIncorrectDetails() throws SQLException {
        singleConnectionDataSource.getConnection("Incorrectusername","Incorrectpassword");
    }

    @Test
    public void testGetConnectionWithUserNamePasswordCorrectDetails() throws SQLException {
        String username = "Correctusername";
        String password = "Correctpassword";
        singleConnectionDataSource.setUsername(username);
        singleConnectionDataSource.setPassword(password);
        
        Connection result = singleConnectionDataSource.getConnection(username, password);
        Assert.assertNotNull(result);
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWhenConnectionIsNull() throws SQLException {
        singleConnectionDataSource.setConnection(null);
        singleConnectionDataSource.resetConnection();
        
        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.close();

        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(connection.getAutoCommit()).thenReturn(false);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.setAutoCommit(true);

        singleConnectionDataSource.prepareConnection(connection);
        Assert.assertEquals(singleConnectionDataSource.getAutoCommit(), connection.getAutoCommit());
    }
}
