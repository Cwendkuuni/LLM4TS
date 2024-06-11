package com.oceanbase.odc.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource;
    Connection connection;
    
    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNull(singleConnectionDataSource.getConnection());

        singleConnectionDataSource.connection = connection;
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws Exception {
        Field usernameField = singleConnectionDataSource.getClass().getSuperclass().getDeclaredField("username");
        Field passwordField = singleConnectionDataSource.getClass().getSuperclass().getDeclaredField("password");
        usernameField.setAccessible(true);
        passwordField.setAccessible(true);
        usernameField.set(singleConnectionDataSource, "admin");
        passwordField.set(singleConnectionDataSource, "12345");

        singleConnectionDataSource.connection = connection;

        assertNotNull(singleConnectionDataSource.getConnection("admin", "12345"));

        try {
                singleConnectionDataSource.getConnection("wrongUser", "wrongPass");
                fail("Expected SQLException to be thrown");
        } catch(SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testAutoCommit() throws SQLException {
        assertNull(singleConnectionDataSource.getAutoCommit());
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());

        singleConnectionDataSource.connection = connection;
        when(connection.getAutoCommit()).thenReturn(false);
        singleConnectionDataSource.prepareConnection(connection);
    }


    @Test
    public void testResetConnection() throws SQLException {
        assertNull(singleConnectionDataSource.connection);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.connection);
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.close();
        verify(connection).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        assertTrue(singleConnectionDataSource.initializerList.isEmpty());
        singleConnectionDataSource.addInitializer(initializer);
        assertFalse(singleConnectionDataSource.initializerList.isEmpty());
    }

    @After
    public void tearDown() {
        singleConnectionDataSource = null;
    }
}