package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.EventPublisherFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {
    private static final String DATABASE_URL = "jdbc:test:testdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private static final String NEW_USERNAME = "new_user";
    private static final String NEW_PASSWORD = "new_password";
    private static final long TIME_OUT_MILLIS = 10 * 1000;

    private EventPublisher eventPublisher;
    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Connection initialConnection;

    @Before
    public void setup() throws Exception {
        eventPublisher = EventPublisherFactory.getDefaultEventPublisher();
        dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setAutoCommit(true);
        dataSource.setAutoReconnect(true);
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);

        when(DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)).thenReturn(initialConnection);

        connection = dataSource.getConnection();
    }

    @Test
    public void testGetConnection() throws Exception {
        assertTrue(connection != null);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosedConnection() throws Exception {
        resetConnection(connection);
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws Exception {
        dataSource.setUsername(NEW_USERNAME);
        dataSource.setPassword(NEW_PASSWORD);
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        resetConnection(connection);
        Connection newConnection = dataSource.getConnection();
        assertNull(connection);
        assertSame(newConnection, dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws Exception {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializer);

        Connection connection = dataSource.getConnection();
        verify(initializer).init(connection);
    }

    @Test
    public void testClose() throws Exception {
        Statement stmt = connection.createStatement();

        try {
            stmt.execute("CREATE TABLE test_table (id INT PRIMARY KEY)");
            dataSource.close();
            dataSource.getConnection();
            fail();
        } finally {
            stmt.close();
        }
    }

    @Test
    public void testTimeOut() throws Exception {
        when(DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)).thenThrow(new SQLException("Timeout"));

        try {
            dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("waited " + TIME_OUT_MILLIS + " millis"));
        }
    }

    @Test
    public void testPublishConnectionResetEvent() throws Exception {
        ConnectionResetEvent event = new ConnectionResetEvent(connection);

        dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher).publishEvent(argumentCaptor.capture());
        assertSame(event, argumentCaptor.getValue());
    }

    @Test
    public void testConnectionProxy() throws Exception {
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());

        Proxy connectionProxy = (Proxy) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        assertSame(connectionProxy, dataSource.getConnectionProxy(connection));
    }

    @After
    public void teardown() throws Exception {
        if (connection != null) {
            connection.close();
        }

        if (initialConnection != null) {
            initialConnection.close();
        }
    }

    private void resetConnection(Connection connection) throws SQLException {
        dataSource.resetConnection();
        connection = dataSource.getConnection();
    }
}