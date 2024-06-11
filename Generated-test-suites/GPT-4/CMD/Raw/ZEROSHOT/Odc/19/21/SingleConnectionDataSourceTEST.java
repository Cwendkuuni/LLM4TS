
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource connectionDataSource;

    @Before
    public void setUp() {
        connectionDataSource = Mockito.spy(new SingleConnectionDataSource(true));
        connectionDataSource.setAutoCommit(true);
    }

    @Test
    public void testAutoReconnectInitialized() {
        assertNotNull(connectionDataSource);
        assertTrue(connectionDataSource.isAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenUsernameAndPasswordNotMatch() throws SQLException {
        connectionDataSource.setUsername("User1");
        connectionDataSource.setPassword("Pass1");
        connectionDataSource.getConnection("User2", "Pass2");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithException() throws SQLException {
        connectionDataSource.setUsername("User1");
        connectionDataSource.setPassword("Pass1");
        when(connectionDataSource.getConnection()).thenThrow(SQLException.class);
        connectionDataSource.getConnection("User1", "Pass1");
    }

    @Test
    public void testGetConnectionSuccessfully() throws SQLException {
        connectionDataSource.setUsername("User1");
        connectionDataSource.setPassword("Pass1");

        when(connectionDataSource.getConnection()).thenReturn(java.sql.DriverManager.getConnection("jdbc:h2:~/test", "User1", "Pass1"));
        assertTrue(connectionDataSource.getConnection("User1", "Pass1").isValid(1000));
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        connectionDataSource.resetConnection();
        assertNull(connectionDataSource.connection);
        assertNull(connectionDataSource.lock);
        connectionDataSource.getUsername();
    }

    @Test
    public void testCloseConnection() {
        connectionDataSource.connection = Mockito.mock(java.sql.Connection.class);
        connectionDataSource.close();
        verify(connectionDataSource.connection).close();
        assertNull(connectionDataSource.connection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        connectionDataSource.connection = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "User1", "Pass1");
        assertNotNull(connectionDataSource.connection);
        assertTrue(connectionDataSource.connection.isValid(1000));
        assertNotNull(connectionDataSource.lock);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithException() throws SQLException {
        connectionDataSource.connection = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "User1", "Pass1");
        connectionDataSource.innerCreateConnection();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        connectionDataSource.connection = Mockito.mock(java.sql.Connection.class);
        connectionDataSource.prepareConnection(connectionDataSource.connection);
        assertTrue(connectionDataSource.getAutoCommit());
    }

    @Test
    public void testAddInitializer() {
        final ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        connectionDataSource.addInitializer(initializer);

        assertTrue(connectionDataSource.initializerList.contains(initializer));
    }

    @Test
    public void testInitConnection() throws SQLException {
        final java.sql.Connection mockConn = Mockito.mock(java.sql.Connection.class);
        final ConnectionInitializer mockInitializer = Mockito.mock(ConnectionInitializer.class);

        connectionDataSource.addInitializer(mockInitializer);
        connectionDataSource.initConnection(mockConn);

        verify(mockInitializer).init(mockConn);
    }
}
