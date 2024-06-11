package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    Connection mockConnection;
    @Mock
    ConnectionInitializer mockConnectionInitializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.addInitializer(mockConnectionInitializer);
    }

    @Test
    public void testGetConnection_NullConnection() throws SQLException {
        Connection result = singleConnectionDataSource.getConnection();
        assertNotNull(result);
    }

    @Test
    public void testGetConnection_WithClosedConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;

        Connection result = singleConnectionDataSource.getConnection();
        assertNotNull(result);
    }

    @Test
    public void testGetConnection_WithConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        singleConnectionDataSource.connection = mockConnection;

        Connection result = singleConnectionDataSource.getConnection();
        assertNotNull(result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WithUsernamePassword_Incorrect() throws SQLException {
        singleConnectionDataSource.getUsername();
        singleConnectionDataSource.getPassword();
        singleConnectionDataSource.getConnection("username", "password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void testCloseConnection() throws SQLException {
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testOnConnectionCreated() {
        singleConnectionDataSource.onConnectionCreated(mockConnection);
        // No exception means test passed.
    }
}
