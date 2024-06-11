package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    @InjectMocks
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.initMocks(this);
        Mockito.strict stub = Mockito.stub(DriverManager.class);
        stub.when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(mock(Connection.class));
        dataSource.setEventPublisher(Mockito.mock(EventPublisher.class, Strictness.LENIENT));
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // No-op
            }
        });
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalid_username");
        dataSource.setPassword("invalid_password");
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithAlreadyInitializedConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithFailedConnectionCreation() throws SQLException {
        Mockito.when(DriverManager.getConnection(anyString(), anyString(), anyString()))
                .thenThrow(new SQLException("Failed to create connection"));
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());

        Connection newConnection = dataSource.getConnection();
        assertSame(newConnection, dataSource.getConnection());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithConflictingConnectionRequest() throws SQLException {
        dataSource.getConnection();
        dataSource.getConnection();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // No-op
            }
        });

        List<ConnectionInitializer> initializers = dataSource.getInitializerList();
        assertTrue(initializers.size() > 0);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        dataSource.setAutoCommit(null);
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testEventPublisher() throws SQLException {
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class, Strictness.LENIENT);
        dataSource.setEventPublisher(eventPublisher);

        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithTimeOut() throws SQLException {
        dataSource.setTimeOutMillis(0);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithAutoReconnect() throws SQLException {
        dataSource.setAutoReconnect(true);
        Connection connection = dataSource.getConnection();
        connection.close();
        Connection newConnection = dataSource.getConnection();
        assertSame(newConnection, dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithAutoReconnectDisabled() throws SQLException {
        dataSource.setAutoReconnect(false);
        Connection connection = dataSource.getConnection();
        connection.close();
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }
}