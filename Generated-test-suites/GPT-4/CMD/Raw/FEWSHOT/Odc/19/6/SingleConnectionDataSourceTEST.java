package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource dataSource;
    Connection mockConnection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        mockConnection = mock(Connection.class);
        dataSource.connection = mockConnection;
        dataSource.lock = new ReentrantLock();
    }

    @Test
    public void testGetConnection_Valid() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ClosedOrInvalidConnectionAndNoReconnection() throws SQLException {
        dataSource.setAutoReconnect(false);
        when(mockConnection.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnection_WithUsernameAndCorrectPassword() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_pass");
        assertNotNull(dataSource.getConnection("test_user", "test_pass"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WithUsernameAndIncorrectPassword() throws SQLException {
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_pass");
        dataSource.getConnection("test_user", "wrong_password");
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        // Checks whether connection gets closed in reset
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test
    public void testClose() {
        dataSource.close();
        // Checks whether connection gets closed
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.canModifyAutoCommit = true;
        dataSource.autoCommit = true;
        when(mockConnection.getAutoCommit()).thenReturn(false);
        dataSource.prepareConnection(mockConnection);
        verify(mockConnection, times(1)).setAutoCommit(true);
    }

    @After
    public void tearDown() {
        dataSource = null;
        mockConnection = null;
    }
}