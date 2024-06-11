
package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource singleConnectionDataSource;
    Connection connection;
    Lock lock;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        lock = new ReentrantLock();
    }

    @Test
    public void testGetConnection() throws SQLException {
        singleConnectionDataSource.connection = connection;
        Connection actualConn = singleConnectionDataSource.getConnection();
        assertTrue(actualConn.equals(connection));

        singleConnectionDataSource.autoReconnect = true;
        when(connection.isClosed()).thenReturn(true);
        actualConn = singleConnectionDataSource.getConnection();
        assertTrue(actualConn.equals(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalid() throws SQLException {
        singleConnectionDataSource.autoReconnect = false;
        when(connection.isClosed()).thenReturn(true);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException, NoSuchFieldException, IllegalAccessException {
        Field usernameField = singleConnectionDataSource.getClass().getSuperclass().getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(singleConnectionDataSource, "TestUsername");

        Field passwordField = singleConnectionDataSource.getClass().getSuperclass().getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(singleConnectionDataSource, "TestPassword");

        Connection conn = singleConnectionDataSource.getConnection("TestUsername", "TestPassword");
        assertTrue(conn.equals(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.getConnection("InvalidUsername", "InvalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.eventPublisher = mock(EventPublisher.class);
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.lock = lock;

        singleConnectionDataSource.resetConnection();
        assertTrue(singleConnectionDataSource.connection == null);
        assertTrue(singleConnectionDataSource.lock == null);
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.close();

        assertTrue(singleConnectionDataSource.connection == null);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy() throws SQLException {
        singleConnectionDataSource.timeOutMillis = -1;
        singleConnectionDataSource.getConnectionProxy(connection);
    }
}
