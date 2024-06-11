package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername("username");
        singleConnectionDataSource.setPassword("password");
    }

    @Test
    public void testGetConnection() {
        try {
            Connection connection = singleConnectionDataSource.getConnection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail();
        }
    }
    
    @Test
    public void testGetConnectionWithCredentials() {
        try {
            Connection connection = singleConnectionDataSource.getConnection("username", "password");
            assertNotNull(connection);
            assertFalse(connection.isClosed());

            fail("Expected exception not thrown.");
        } catch (Exception e) {
            assertEquals(SQLException.class, e.getClass());
        }
    }
    
    @Test
    public void testResetConnection() {
        try {
            Connection connection = Mockito.mock(Connection.class);
            Mockito.when(connection.isClosed()).thenReturn(false);
            singleConnectionDataSource.connection = connection;
            
            singleConnectionDataSource.resetConnection();
            
            assertTrue(singleConnectionDataSource.connection.isClosed());
        } catch (SQLException e) {
            fail();
        }
    }
    
    @Test
    public void testConnectionClose() {
        try {
            Connection connection = Mockito.mock(Connection.class);
            Mockito.when(connection.isClosed()).thenReturn(false);
            singleConnectionDataSource.connection = connection;
            
            singleConnectionDataSource.close();
            
            assertTrue(singleConnectionDataSource.connection.isClosed());
        } catch (SQLException e) {
            fail();
        }
    }
    
    @Test
    public void testConnectionInitializerFailed() {
        try {
            ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
            Mockito.doThrow(new RuntimeException()).when(connectionInitializer).init(any());
            
            singleConnectionDataSource.addInitializer(connectionInitializer);
            singleConnectionDataSource.getConnection();
            
            fail("Expected exception not thrown.");
        } catch (Exception e) {
            assertEquals(SQLException.class, e.getClass());
        }
    }
}