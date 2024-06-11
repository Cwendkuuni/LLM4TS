package com.oceanbase.odc.core.datasource;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection mockConnection;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalid() throws SQLException {
        dataSource.setConnection(mockConnection);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(false);
        Connection conn = dataSource.getConnection();
        assertNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidUsernamePassword() throws SQLException {
        dataSource.setConnection(mockConnection);
        Mockito.when(mockConnection.isValid(Mockito.anyInt())).thenReturn(true);
        Connection conn = dataSource.getConnection("invalid_username", "invalid_password");
        assertNull(conn);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void addInitializerConnection() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.initializerList.size());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.close();
        Mockito.verify(mockConnection, Mockito.times(1)).close();
    }

    @After
    public void teardown() {
        dataSource = null;
        mockConnection = null;
    }
}