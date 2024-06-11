package com.oceanbase.odc.core.datasource.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.test.util.TestConnection;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 * @date 2023-02-18 10:15
 */
class SingleConnectionDataSourceTest {

    private static Connection mockConnection;
    private static SingleConnectionDataSource dataSource;
    private static EventPublisher eventPublisher;

    @BeforeAll
    static void setup() throws Exception {
        mockConnection = Mockito.mock(Connection.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSource = new SingleConnectionDataSource(true) {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return mockConnection;
            }

            @Override
            public void addInitializer(@NonNull ConnectionInitializer connectionInitializer) {
                // do nothing
            }

            @Override
            public void onConnectionReset(Connection connection) {
                // do nothing
            }
        };
        dataSource.setEventPublisher(eventPublisher);
    }

    @AfterAll
    static void teardown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()} method
     *
     * @throws SQLException
     */
    @Test
    void testGetConnection() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyLong())).thenReturn(true);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(anyLong());
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()} method when connection is closed
     *
     * @throws SQLException
     */
    @Test
    void testGetConnectionWhenConnectionIsClosed() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        when(mockConnection.isValid(anyLong())).thenReturn(false);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
        verify(mockConnection, times(1)).isValid(anyLong());
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()} method when connection is not valid
     *
     * @throws SQLException
     */
    @Test
    void testGetConnectionWhenConnectionIsNotValid() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyLong())).thenReturn(false);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
        verify(mockConnection, times(1)).isValid(anyLong());
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()} method when autoReconnect is false
     *
     * @throws SQLException
     */
    @Test
    void testGetConnectionWhenAutoReconnectIsFalse() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        when(mockConnection.isValid(anyLong())).thenReturn(false);
        when(mockConnection.resetPool()).thenThrow(new SQLException("Failed to reset the pool"));
        when(mockConnection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(anyLong());
        verify(mockConnection, times(1)).resetPool();
        verify(mockConnection, times(1)).createStatement();
    }

    /**
     * Test for {@link SingleConnectionDataSource#getConnection()} method when autoReconnect is true
     *
     * @throws SQLException
     */
    @Test
    void testGetConnectionWhenAutoReconnectIsTrue() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        when(mockConnection.isValid(anyLong())).thenReturn(false);
        when(mockConnection.resetPool()).thenReturn(mockConnection);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        verify(mockConnection, times(1)).isValid(anyLong());
        verify(mockConnection, times(1)).resetPool();
    }

    /**
     * Test for {@link SingleConnectionDataSource#addInitializer(ConnectionInitializer)} method
     */
    @Test
    void testAddInitializer() {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializer);
        verify(dataSource, times(1)).initConnection(mockConnection);
    }

    /**
     * Test for {@link SingleConnectionDataSource#close()} method
     *
     * @throws SQLException
     */
    @Test
    void testClose() throws SQLException {
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }
}