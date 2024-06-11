package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SingleConnectionDataSourceTest {

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String DATABASE_URL = "jdbc:test:database";
    private static final String SQL_INSERT = "INSERT INTO test_table VALUES (?, ?)";
    private static final String SQL_SELECT = "SELECT * FROM test_table";
    private static final int LOGIN_TIMEOUT = 5000;

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;

    @BeforeEach
    public void setup() {
        this.eventPublisher = mock(EventPublisher.class, Strictness.LENIENT);
        this.dataSource = new SingleConnectionDataSource(true) {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                this.connection = createConnection();
                return this.connection;
            }

            @Override
            protected void onConnectionCreated(Connection connection) {
                // no-op
            }

            @Override
            protected void initConnection(Connection connection) throws SQLException {
                // no-op
            }

            @Override
            protected void prepareConnection(Connection con) throws SQLException {
                // no-op
            }

            @Override
            public void close() {
                if (Objects.nonNull(connection)) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        // ignore
                    }
                    connection = null;
                }
                super.close();
            }

            private Connection createConnection() throws SQLException {
                return Mockito.mock(Connection.class);
            }
        };
        this.dataSource.setUsername(USERNAME);
        this.dataSource.setPassword(PASSWORD);
        this.dataSource.setAutoReconnect(true);
        this.dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(1)).thenReturn(1);
        Mockito.when(resultSet.getBigDecimal(2)).thenReturn(new BigDecimal("10"));

        Connection connection = this.dataSource.getConnection();
        Statement statement1 = connection.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(SQL_SELECT);
        assertTrue(resultSet1.next());
        assertEquals(1, resultSet1.getInt(1));
        assertEquals(new BigDecimal("10"), resultSet1.getBigDecimal(2));

        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnection_InvalidCredentials() throws SQLException {
        Assertions.assertThrows(SQLException.class, () -> this.dataSource.getConnection("wrongUser", "wrongPassword"));
    }

    @Test
    public void testGetConnection_ConnectionReset() throws SQLException, InterruptedException {
        Mockito.when(connection.isClosed()).thenReturn(true);
        this.dataSource.getConnection();
        Mockito.verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));

        this.dataSource.resetConnection();
        Mockito.verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(eventPublisher, times(2)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(statement.executeQuery(any(String.class))).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(1)).thenReturn(1);

        Connection connection = this.dataSource.getConnection();
        Statement statement1 = connection.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(SQL_SELECT);
        assertTrue(resultSet1.next());
        assertEquals(1, resultSet1.getInt(1));

        this.dataSource.resetConnection();
        Connection connection1 = this.dataSource.getConnection();
        Statement statement2 = connection1.createStatement();
        ResultSet resultSet2 = statement2.executeQuery(SQL_SELECT);
        assertFalse(resultSet2.next());

        Mockito.verify(connection, times(2)).close();
        Mockito.verify(eventPublisher, times(2)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        verify(initializer, times(1)).init(this.connection);
    }

    @Test
    public void testAddInitializer_Duplicate() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        this.dataSource.addInitializer(initializer);
        verify(initializer, times(1)).init(this.connection);
    }

    @Test
    public void testAutoCommit() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.getAutoCommit()).thenReturn(false);

        this.dataSource.setAutoCommit(true);
        Connection connection1 = this.dataSource.getConnection();
        Mockito.verify(connection1).setAutoCommit(true);

        this.dataSource.setAutoCommit(false);
        Connection connection2 = this.dataSource.getConnection();
        Mockito.verify(connection2).setAutoCommit(false);
    }

    @Test
    public void testAutoCommit_ResetConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.getAutoCommit()).thenReturn(false);

        this.dataSource.setAutoCommit(true);
        Connection connection1 = this.dataSource.getConnection();
        Mockito.verify(connection1).setAutoCommit(true);

        this.dataSource.resetConnection();
        Connection connection2 = this.dataSource.getConnection();
        Mockito.verify(connection2).setAutoCommit(false);
    }

    @Test
    public void testAutoCommit_NullAutoCommit() throws SQLException {
        this.dataSource.setAutoCommit(null);
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(connection, times(1)).setAutoCommit(null);
    }

    @Test
    public void testTimeOutMillis() throws SQLException {
        this.dataSource.setTimeOutMillis(100);
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(connection, times(1)).setConnectTimeout(100);
    }

    @Test
    public void testTimeOutMillis_ResetConnection() throws SQLException {
        this.dataSource.setTimeOutMillis(100);
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(connection, times(1)).setConnectTimeout(100);

        this.dataSource.resetConnection();
        Connection connection1 = this.dataSource.getConnection();
        Mockito.verify(connection1, times(1)).setConnectTimeout(100);

        this.dataSource.setTimeOutMillis(200);
        Connection connection2 = this.dataSource.getConnection();
        Mockito.verify(connection2, times(1)).setConnectTimeout(200);
    }

    @Test
    public void testTimeOutMillis_Negative() throws SQLException {
        this.dataSource.setTimeOutMillis(-1);
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(connection, times(1)).setConnectTimeout(-1);
    }

    @Test
    public void testTimeOutMillis_Null() throws SQLException {
        this.dataSource.setTimeOutMillis(null);
        Connection connection = this.dataSource.getConnection();
        Mockito.verify(connection, times(1)).setConnectTimeout(0);
    }

    @Test
    public void testEventPublisher() {
        ConnectionResetEvent event = new ConnectionResetEvent(this.connection);
        this.dataSource.setEventPublisher(null);
        this.dataSource.getConnection();
        assertNull(this.eventPublisher);

        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.getConnection();
        verify(this.eventPublisher, times(1)).publishEvent(event);
    }

    @Test
    public void testEventPublisher_NullEventPublisher() {
        ConnectionResetEvent event = new ConnectionResetEvent(this.connection);
        this.dataSource.setEventPublisher(null);
        this.dataSource.resetConnection();
        assertNull(this.eventPublisher);

        this.dataSource.setEventPublisher(this.eventPublisher);
        this.dataSource.resetConnection();
        verify(this.eventPublisher, times(1)).publishEvent(event);
    }
}