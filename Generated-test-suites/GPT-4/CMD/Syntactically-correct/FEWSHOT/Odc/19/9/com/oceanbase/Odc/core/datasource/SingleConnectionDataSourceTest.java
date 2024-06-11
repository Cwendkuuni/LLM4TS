
package com.oceanbase.odc.core.datasource;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();

        ds.setUsername("user");
        ds.setPassword("pass");

        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isValid(ds.getLoginTimeout())).thenReturn(false);

        ds.connection = mockConnection;

        Connection connection = ds.getConnection();
        assertFalse(connection.isClosed());

        // Assert that the connection returned is valid
        assertTrue(connection.isValid(0));

        // Test connection with wrong login details
        boolean exceptionCaught = false;
        try {
            ds.getConnection("wrongUser", "wrongPass");
        } catch (SQLException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.resetConnection();
    }

    @Test
    public void testAddInitializer() throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.addInitializer(connection -> {
        });
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();

        ds.connection = mock(Connection.class);
        ds.close();
    }

    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
        ds.lock = new ReentrantLock();

        Connection connection = mock(Connection.class);
        Connection proxyConnection = ds.getConnectionProxy(connection);

        assertFalse(proxyConnection.isWrapperFor(connection.getClass()));
    }

    @Test(expected = InvocationTargetException.class)
    public void testCloseIgnoreInvocationHandler_invoke() throws Throwable {
        Connection mockConnection = mock(Connection.class);
        ReentrantLock lock = new ReentrantLock();

        CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mockConnection, lock);

        // Test equals method
        assertTrue((Boolean) handler.invoke(handler, Object.class.getMethod("equals", Object.class), new Object[]{handler}));

        // Test hashCode method
        assertFalse((Integer) handler.invoke(handler, Object.class.getMethod("hashCode"), null) == 0);

        // Test unwrap method
        assertFalse((Boolean) handler.invoke(handler, Connection.class.getMethod("unwrap", Class.class), new Object[]{Integer.class}));

        // Test isWrapperFor method
        assertFalse((Boolean) handler.invoke(handler, Connection.class.getMethod("isWrapperFor", Class.class), new Object[]{Connection.class}));

        // Test close method
        handler.invoke(handler, Connection.class.getMethod("close"), null);

        // Test isClosed method
        assertFalse((Boolean) handler.invoke(handler, Connection.class.getMethod("isClosed"), null));

        // Test method that will cause an exception
        handler.invoke(handler, Connection.class.getMethod("setAutoCommit", boolean.class), new Object[]{false});
    }
}
