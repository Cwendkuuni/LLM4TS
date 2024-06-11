package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer;
    private Connection connection;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        initializer = mock(ConnectionInitializer.class);
        connection = mock(Connection.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
        
        dataSource.setConnection(connection);
        assertEquals(connection, dataSource.getConnection());

        when(connection.isClosed()).thenReturn(true);
        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        assertNotNull(dataSource.getConnection("user", "password"));
        try {
            dataSource.getConnection("invalidUser", "invalidPassword");
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.resetConnection();

        verify(dataSource).close();
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(initializer);
        List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
        assertTrue(initializerList.contains(initializer));
    }

    // Add more test cases as needed
    
}