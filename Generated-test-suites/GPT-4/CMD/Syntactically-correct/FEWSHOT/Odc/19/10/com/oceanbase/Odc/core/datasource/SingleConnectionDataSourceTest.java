
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        dataSource = new SingleConnectionDataSource(true);
        connection = mock(Connection.class);
        dataSource.setConnection(connection);
    }

    @Test
    public void getConnection_NullConnection_AutoReconnect_True() throws SQLException {
        dataSource.setConnection(null);
        assertNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void getConnection_ClosedConnection_AutoReconnect_False() throws SQLException {
        dataSource.setAutoReconnect(false);
        when(connection.isClosed()).thenReturn(true);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void getConnection_InvalidConnection_AutoReconnect_False() throws SQLException {
        dataSource.setAutoReconnect(false);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void getConnection_DifferentUsernameAndPassword() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.getConnection("differentUsername", "differentPassword");
    }

    @Test
    public void resetConnection() throws SQLException {
        dataSource.resetConnection();
        verify(connection, times(1)).close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void close() throws SQLException {
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void prepareConnection_AutoCommit_Null() throws SQLException {
        assertNull(dataSource.getAutoCommit());
    }

    @Test
    public void prepareConnection_AutoCommit_False() throws SQLException {
        dataSource.setAutoCommit(false);
        when(connection.getAutoCommit()).thenReturn(true);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(false);
    }

    @Test
    public void prepareConnection_AutoCommit_True() throws SQLException {
        dataSource.setAutoCommit(true);
        when(connection.getAutoCommit()).thenReturn(false);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(true);
    }
}
