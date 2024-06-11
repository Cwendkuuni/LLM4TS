package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.AfterClass;
import org.junit.Test;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;
    private static Connection conn;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        dataSource = new SingleConnectionDataSource();
        conn = mock(Connection.class);
    }

    @AfterClass
    public static void afterClass() {
        try { 
            conn.close(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getConnection() throws SQLException {
        dataSource.connection = conn;
        when(conn.isClosed()).thenReturn(false);
        when(conn.isValid(anyInt())).thenReturn(true);
        Connection result = dataSource.getConnection();
        assertSame(conn, result);
    }

    @Test(expected = SQLException.class)
    public void getConnection_noReconnect_closedConnection() throws SQLException {
        dataSource.connection = conn;
        when(conn.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test
    public void getConnection_authorized() throws SQLException {
        dataSource.connection = conn;
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        Connection result = dataSource.getConnection("user", "password");
        assertSame(conn, result);
    }

    @Test(expected=SQLException.class)
    public void getConnection_unauthorized() throws SQLException {
        dataSource.connection = conn;
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        dataSource.getConnection("wrongUser", "wrongpassword");
    }

    @Test(expected = SQLException.class)
    public void getConnection_connectionNotNull() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field connField = SingleConnectionDataSource.class.getDeclaredField("connection");
        connField.setAccessible(true);
        connField.set(dataSource, conn);
        dataSource.innerCreateConnection();
    }

    @Test
    public void addInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test
    public void close() throws SQLException {
        dataSource.connection = conn;
        dataSource.close();
        verify(conn).close();
        assertNull(dataSource.connection);
    }
}