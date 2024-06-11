package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private final String username = "username";
    private final String password = "password";


    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = Mockito.mock(Connection.class);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        dataSource.connection = connection;
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithDifferentCredentials() throws SQLException {
        dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.connection = connection;
        dataSource.resetConnection();
        assertTrue(dataSource.getConnection() == null);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.connection = connection;
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test(expected = InvocationTargetException.class)
    public void testCloseIgnoreInvocationHandler() throws Throwable {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, dataSource.lock);
        handler.invoke(dataSource, SingleConnectionDataSource.class.getDeclaredMethod("close"), null);
    }

}
