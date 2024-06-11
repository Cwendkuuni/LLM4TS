package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherStub;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializerStub;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-03-27 10:11
 */
public class SingleConnectionDataSourceTest {

    private static final String DATABASE_URL = "jdbc:test:connection";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final long TIME_OUT_MILLISECONDS = 10 * 1000;
    private static final int LOGIN_TIMEOUT = 5;
    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connection;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        DriverManager.registerDriver(new SingleConnectionDataSourceDriver());
        this.singleConnectionDataSource = new SingleConnectionDataSource();
        this.singleConnectionDataSource.setEventPublisher(eventPublisher = new EventPublisherStub());
        this.singleConnectionDataSource.addInitializer(connectionInitializer = new ConnectionInitializerStub());
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection getConnection = singleConnectionDataSource.getConnection();
        assertNotNull(getConnection);
        assertEquals(SingleConnectionDataSource.class, getConnection.getClass().getSuperclass());
        assertEquals("testUser", getConnection.getMetaData().getUserName());
        assertEquals("jdbc:test:connection", getConnection.getMetaData().getURL());
        assertEquals("testPassword", getConnection.getMetaData().getPassword());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        singleConnectionDataSource.setUsername("invalidUsername");
        singleConnectionDataSource.setPassword("invalidPassword");
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithSameCredentials() throws SQLException {
        Connection connection1 = singleConnectionDataSource.getConnection();
        Connection connection2 = singleConnectionDataSource.getConnection();
        assertEquals(connection1, connection2);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
        Connection newConnection = singleConnectionDataSource.getConnection();
        assertNotSame(connection, newConnection);
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        List<ConnectionInitializer> initializerList = new ArrayList<>();
        ConnectionInitializer connectionInitializer = new ConnectionInitializerStub();
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals(initializerList.size(), singleConnectionDataSource.getInitializerList().size());
        assertEquals(connectionInitializer, singleConnectionDataSource.getInitializerList().get(0));
    }

    @Test
    public void testClose() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithTimeOut() throws SQLException, InterruptedException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();
        SingleConnectionDataSource singleConnectionDataSourceWithTimeOut = new SingleConnectionDataSource();
        singleConnectionDataSourceWithTimeOut.setTimeOutMillis(0);
        singleConnectionDataSourceWithTimeOut.getConnection();
        TimeUnit.MILLISECONDS.sleep(TIME_OUT_MILLISECONDS + 1);
        singleConnectionDataSourceWithTimeOut.getConnection();
    }

    @Test
    public void testAutoCommit() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        Connection connection = singleConnectionDataSource.getConnection();
        connection.setAutoCommit(false);
        connection.commit();
        singleConnectionDataSource.setAutoCommit(false);
        connection.setAutoCommit(true);
        singleConnectionDataSource.getConnection().commit();
    }

    @Test
    public void testAutoReconnect() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource(true);
        Connection connection = singleConnectionDataSource.getConnection();
        connection.close();
        Connection connection1 = singleConnectionDataSource.getConnection();
        connection1.close();
        Connection connection2 = singleConnectionDataSource.getConnection();
        assertNotSame(connection, connection2);
    }

    @Test
    public void testPublishEvent() throws SQLException, InterruptedException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.resetConnection();
        eventPublisher.publishEvent(new ConnectionResetEvent(connection));
        Connection newConnection = singleConnectionDataSource.getConnection();
        assertNotSame(connection, newConnection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        CloseIgnoreInvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, null);
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, invocationHandler);
        assertNotNull(proxy);
        assertFalse(connection.isClosed());
        proxy.close();
        assertTrue(connection.isClosed());
    }
}

class SingleConnectionDataSourceDriver implements java.sql.Driver {

    @Override
    public Connection connect(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, int flags) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int initializeDatabase(String url) throws SQLException {
        return 0;
    }

    @Override
    public void finalize() throws Throwable {
    }
}

class ConnectionInitializerStub implements ConnectionInitializer {

    @Override
    public void init(Connection connection) throws SQLException {
        // Empty implementation
    }
}

class EventPublisherStub implements EventPublisher {

    @Override
    public void publishEvent(Object event) {
        // Empty implementation
    }
}

class ConnectionResetEvent {

    private final Connection connection;

    public ConnectionResetEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}