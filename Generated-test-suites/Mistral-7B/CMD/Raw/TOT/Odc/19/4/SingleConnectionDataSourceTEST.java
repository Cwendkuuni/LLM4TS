package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for SingleConnectionDataSource.
 */
public class SingleConnectionDataSourceTest {
    private static final String DUMMY_USERNAME = "testUser";
    private static final String DUMMY_PASSWORD = "testPassword";
    private static final String DUMMY_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String DUMMY_URL = "jdbc:mysql://localhost:3306/testdb";

    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisherMock;
    private Connection dummyConnection;

    @Before
    public void setup() throws SQLException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(getConnectionFromDriver(DUMMY_USERNAME, DUMMY_PASSWORD)).thenReturn(dummyConnection);
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Happy path
        Assert.assertNotNull(dataSource.getConnection());

        // AutoCommit test
        Connection connection = dataSource.getConnection();
        Assert.assertFalse(connection.isAutoCommit());
        dataSource.setAutoCommit(true);
        Assert.assertTrue(dataSource.getConnection().isAutoCommit());
        dataSource.setAutoCommit(false);
        Assert.assertFalse(dataSource.getConnection().isAutoCommit());

        // AutoReconnect test
        dataSource.setAutoReconnect(false);
        try {
            dataSource.getConnection();
            dataSource.getConnection();
            dataSource.getConnection();
            dataSource.resetConnection();
            dataSource.getConnection();
            Mockito.verify(eventPublisherMock, Mockito.times(1)).publishEvent(Mockito.isInstanceOf(ConnectionResetEvent.class));
        } catch (SQLException e) {
            Assert.assertEquals("Connection was closed or not valid", e.getMessage());
        }

        // Timeout test
        dataSource.setTimeOutMillis(1);
        try {
            dataSource.getConnection();
            Assert.fail();
        } catch (SQLException e) {
            Assert.assertEquals("Timeout: wait_timeout exceeded", e.getMessage());
        }

        // Initializer test
        Mockito.when(dummyConnection.isValid(Mockito.anyInt())).thenReturn(false);
        dataSource.addInitializer(connection -> {});
        Mockito.verify(dummyConnection, Mockito.times(1)).setAutoCommit(Mockito.anyBoolean());
        dataSource.getConnection();
    }

    @After
    public void teardown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private Connection getConnectionFromDriver(String username, String password) throws SQLException {
        try {
            Class.forName(DUMMY_DRIVER_CLASS);
            return DriverManager.getConnection(DUMMY_URL, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Could not load JDBC driver", e);
        }
    }

    private static class ConnectionResetEvent {
        private final Connection connection;

        ConnectionResetEvent(Connection connection) {
            this.connection = connection;
        }
    }
}