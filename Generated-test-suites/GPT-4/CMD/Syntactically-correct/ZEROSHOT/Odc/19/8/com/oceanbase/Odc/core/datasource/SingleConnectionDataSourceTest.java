
package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private Connection connection;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        connection = mock(Connection.class);
        eventPublisher = mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
    }

    @Test(expected = SQLException.class)
    public void getConnection_Null_ThrowsSQLException() throws SQLException {
        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void getConnection_ClosedOrInvalid_ThrowsSQLException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        singleConnectionDataSource.getConnection("username", "password");
    }

    @Test
    public void resetConnection_Successful() throws SQLException {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.resetConnection();
        verify(connection).close();
    }

    @Test(expected = SQLException.class)
    public void getConnection_InvalidUsernameOrPassword_ThrowsException() throws SQLException {
        singleConnectionDataSource.setUrl("url");
        singleConnectionDataSource.setUsername("username");
        singleConnectionDataSource.setPassword("password");
        singleConnectionDataSource.getConnection("user", "pass");
    }
  
    @Test
    public void close_Successful() throws SQLException {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.close();
        verify(connection).close();
    }

    @Test(expected = RuntimeException.class)
    public void tryLock_InterruptedException_ThrowsRuntimeException() {
        singleConnectionDataSource.lock = null;
        singleConnectionDataSource.tryLock();
    }

    @Test
    public void prepareConnection_SetAutoCommit_SameAsGiven() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(true);
    }

    @Test(expected = SQLException.class)
    public void innerCreateConnection_NotNull_ThrowsStateException() throws SQLException {
        singleConnectionDataSource.connection = connection;
        singleConnectionDataSource.innerCreateConnection();
    }

    @Test(expected = SQLException.class)
    public void getConnection_InnerCreateConnection_ThrowsSQLException() throws SQLException {
        singleConnectionDataSource.getConnection();
    }

    // Add more tests as per methods in the source class...

}

