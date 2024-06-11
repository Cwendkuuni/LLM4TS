package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertEquals(dataSource.getConnectionProxy(connection), connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";
        SQLException exception = null;
        try {
            dataSource.getConnection(username, password);
        } catch (SQLException e) {
            exception = e;
        }
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    public void testResetConnection() throws SQLException {
        SQLException exception = null;
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    public void testAddInitializer() throws NoSuchFieldException, IllegalAccessException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Field initializerListField = SingleConnectionDataSource.class.getDeclaredField("initializerList");
        initializerListField.setAccessible(true);
        assertEquals(1, ((List<ConnectionInitializer>) initializerListField.get(dataSource)).size());
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.connection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        boolean result = dataSource.tryLock();
        assertEquals(true, result);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SQLException exception = null;
        try {
            dataSource.innerCreateConnection();
        } catch (SQLException e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        Lock lock = mock(Lock.class);
        Connection connection = mock(Connection.class);
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        assertNull(handler.invoke(connection, connection.getClass().getMethod("close"), null));
        assertEquals(false, handler.invoke(connection, connection.getClass().getMethod("isClosed"), null));
    }
}