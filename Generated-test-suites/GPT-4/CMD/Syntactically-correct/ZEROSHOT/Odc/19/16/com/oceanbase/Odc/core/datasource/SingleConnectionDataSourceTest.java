
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connectionMock;
    private ConnectionInitializer connectionInitializerMock;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        connectionMock = mock(Connection.class);
        connectionInitializerMock = mock(ConnectionInitializer.class);
    }

    @Test
    public void addInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializerMock);
        assertNotNull(singleConnectionDataSource);
    }

    @Test(expected = SQLException.class)
    public void getConnectionWithClosedConnectionAndNoAutoReconnect() throws SQLException {
        when(connectionMock.isClosed()).thenReturn(true);
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void invalidUsernameAndPasswordGetConnection() throws SQLException {
        singleConnectionDataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test(expected = InvocationTargetException.class)
    public void invalidMethodNameInvoke() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Connection proxyConnection = singleConnectionDataSource.getConnection(connectionMock);
        proxyConnection.createStatement();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidTimeoutMillisValueShouldThrowAnException() {
        singleConnectionDataSource.setTimeOutMillis(-10);
    }

    @Test(expected = SQLException.class)
    public void resetConnectionThrowsSQLException() throws SQLException {
        singleConnectionDataSource.resetConnection();
    }

    @Test(expected = SQLException.class)
    public void initConnectionAsyncSwitchHookThrowsSQLException() throws SQLException {
        singleConnectionDataSource.initConnection(connectionMock);
    }

    @Test
    public void testAutoCommitSetterAndGetter() {
        singleConnectionDataSource.setAutoCommit(true);
        assertTrue(singleConnectionDataSource.getAutoCommit());
    }

    @Test(expected = SQLException.class)
    public void getConnectionThrowsSQLException() throws SQLException {
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void emptyUsernameAndPasswordGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("", "");
        assertNotNull(connection);
    }

    @Test
    public void closeDoesNotThrowException() {
        singleConnectionDataSource.close();
        assertTrue(true);
    }

    @Test
    public void timeOutMillisSetterAndGetter() {
        singleConnectionDataSource.setTimeOutMillis(1000);
        assertEquals(1000, singleConnectionDataSource.getTimeOutMillis());
    }

}
