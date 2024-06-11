package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SingleConnectionDataSourceTest {

    private static final String DB_URL = "jdbc:mock:db";
    private static final String USER = "test";
    private static final String PASSWORD = "test";
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private Connection connection;
    private List<RuntimeException> exceptions = new LinkedList<>();

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mock.jdbc.Driver");
        dataSource = new SingleConnectionDataSource();
        eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setUsername(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setAutoReconnect(true);
        dataSource.setTimeOutMillis(1000);
        connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    @Test
    void testGetConnection() throws SQLException {
        Assertions.assertNotNull(dataSource.getConnection());
    }

    @Test
    void testGetConnection_withInvalidCredentials() throws SQLException {
        doThrow(new SQLException("Invalid username or password"))
                .when(dataSource)
                .getConnection(anyString(), anyString());

        Executable executable = () -> dataSource.getConnection("username", "password");
        Assertions.assertThrows(SQLException.class, executable);
    }

    @Test
    void testGetConnectionProxy() throws SQLException {
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        Assertions.assertNotNull(connectionProxy);
        Assertions.assertTrue(connectionProxy instanceof SingleConnectionDataSource.CloseIgnoreInvocationHandler);
    }

    @Test
    void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Assertions.assertNull(dataSource.connection);
        Assertions.assertNull(dataSource.lock);
        connection = dataSource.innerCreateConnection();
        Assertions.assertNotNull(connection);
    }

    @Test
    void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Assertions.assertEquals(1, dataSource.initializerList.size());
        Assertions.assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test
    void testClose() {
        dataSource.close();
        Assertions.assertNull(dataSource.connection);
        Assertions.assertNull(dataSource.lock);
    }

    @Test
    void testPrepareConnection() throws SQLException {
        Boolean autoCommit = true;
        dataSource.setAutoCommit(autoCommit);
        Connection connection = dataSource.getConnection();
        dataSource.prepareConnection(connection);
        Assertions.assertTrue(connection.isAutoCommit());
    }

    @Test
    void testTryLock() {
        Assertions.assertTrue(dataSource.tryLock());
    }

    @Test
    void testTryLock_failed() {
        doThrow(new InterruptedException())
                .when(dataSource.lock)
                .tryLock(anyInt(), anyInt());

        Executable executable = dataSource::tryLock;
        Assertions.assertFalse(executable.execute());
    }

    @Test
    void testGetConnection_connectionReset() throws SQLException {
        dataSource.resetConnection();
        dataSource.setAutoReconnect(false);
        try {
            dataSource.getConnection();
            Assertions.fail();
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    void testGetConnection_conflictException() throws SQLException {
        dataSource.setTimeOutMillis(0);
        doThrow(new ConflictException(ErrorCodes.ConnectionOccupied, new Object[0],
                "Connection is occupied"))
                .when(dataSource)
                .getConnectionProxy(any(Connection.class));

        Executable executable = dataSource::getConnection;
        Assertions.assertThrows(ConflictException.class, executable);
    }
}