package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource scds;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        scds = new SingleConnectionDataSource();
        connection = Mockito.mock(Connection.class);
        scds.connection = connection;
    }

    @Test
    public void testGetConnection() throws SQLException {
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.when(connection.isValid(Mockito.anyInt())).thenReturn(true);

        Connection result = scds.getConnection();
        assertNotNull(result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithAutoReconnectFalse() throws SQLException {
        scds = new SingleConnectionDataSource(false);
        scds.connection = Mockito.mock(Connection.class);
        Mockito.when(scds.connection.isClosed()).thenReturn(true);

        scds.getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        scds.setUsername("username");
        scds.setPassword("password");

        Connection result = scds.getConnection("username", "password");
        assertNotNull(result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithWrongUsernameAndPassword() throws SQLException {
        scds.setUsername("username");
        scds.setPassword("password");

        scds.getConnection("wrongusername", "wrongpassword");
    }

    @Test
    public void testClose() {
        scds.close();

        assertTrue(scds.connection == null);
    }
}
//