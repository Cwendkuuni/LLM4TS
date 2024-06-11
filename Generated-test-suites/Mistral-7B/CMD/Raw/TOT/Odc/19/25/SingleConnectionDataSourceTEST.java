package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;
    private List<SingleConnectionDataSource.ConnectionInitializer> initializers;
    private static final long TIMEOUT_MILLIS = 500;

    @Before
    public void setup() throws SQLException {
        this.dataSource = new SingleConnectionDataSource();
        this.connectionMock = Mockito.mock(Connection.class);
        this.initializers = new ArrayList<>();
        dataSource.addInitializer((connection) -> {
            Mockito.doNothing().when(connection).setAutoCommit(true);
        });
        dataSource.addInitializer((connection) -> {
            Mockito.doNothing().when(connection).setAutoCommit(false);
        });
    }

    @Test
    public void testGetConnection_shouldReturnSameConnectionInstance_whenMultipleGetConnectionCalls() throws SQLException {
        dataSource.setAutoReconnect(true);
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();
        assertEquals(connection1.hashCode(), connection2.hashCode());
    }

    @Test
    public void testGetConnection_shouldThrowSQLException_whenInvalidUsernameOrPassword() throws SQLException {
        dataSource.setUsername("wrong_username");
        dataSource.setPassword("wrong_password");
        try {
            dataSource.getConnection();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals(ErrorCodes.InvalidUsernameOrPassword, e.getErrorCode());
        }
    }

    @Test
    public void testResetConnection_shouldResetConnectionAndNotifyEventPublisher() throws SQLException {
        dataSource.setEventPublisher(Mockito.mock(SingleConnectionDataSource.EventPublisher.class));
        dataSource.getConnection();
        dataSource.resetConnection();
        verify(dataSource.getEventPublisher()).publishEvent(Mockito.isInstanceOf(SingleConnectionDataSource.ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_shouldThrowSQLException_whenConnectionCreationFails() throws SQLException {
        Mockito.when(this.connectionMock.isValid(TIMEOUT_MILLIS)).thenReturn(false);
        dataSource.setConnection(this.connectionMock);
        dataSource.getConnection();
    }

    @After
    public void teardown() throws SQLException {
        if (dataSource.getConnection() != null) {
            dataSource.close();
        }
    }
}