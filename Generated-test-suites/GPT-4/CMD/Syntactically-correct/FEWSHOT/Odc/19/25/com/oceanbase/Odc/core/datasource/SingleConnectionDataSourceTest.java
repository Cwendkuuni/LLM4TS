
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer;
  
    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        initializer = Mockito.mock(ConnectionInitializer.class);
    }
  
    @Test(expected = SQLException.class)
    public void testGetConnection() throws SQLException {
        dataSource.getConnection();
    }

    @Test(expected=SQLException.class)
    public void testGetConnectionWithUsernameAndPassword_failsWithIncorrectCredentials() throws SQLException {
        Connection conn = dataSource.getConnection("test_user", "test_password");
    }
  
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.addInitializer(initializer);
        dataSource.resetConnection();
        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
    }
  
    @Test 
    public void testPrepareConnection() throws SQLException {
        Connection con = Mockito.mock(Connection.class);
        Mockito.when(con.getAutoCommit()).thenReturn(false);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(con);
        Mockito.verify(con, Mockito.times(1)).setAutoCommit(true);
    }
  
    @Test
    public void testClose() throws SQLException {
        Connection con = Mockito.mock(Connection.class);
        dataSource.connection = con;
        dataSource.close();
        Mockito.verify(con, Mockito.times(1)).close();
    }

    @Test
    public void testCloseIgnoreInvocationHandler_close() throws Throwable {
        Connection target = Mockito.mock(Connection.class);
        ReentrantLock lock = new ReentrantLock();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(target, lock);
        handler.invoke(Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler),
                Connection.class.getMethod("close"), null);
        assertFalse(lock.isLocked());
    }
    
    @Test
    public void testCloseIgnoreInvocationHandler_isClosed() throws Throwable {
        Connection target = Mockito.mock(Connection.class);
        ReentrantLock lock = new ReentrantLock();
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(target, lock);
        Boolean isClosed = (Boolean) handler.invoke(Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler),
                Connection.class.getMethod("isClosed"), null);
        assertFalse(isClosed);
    }
  
    // Similar tests can be written for other methods.
}
