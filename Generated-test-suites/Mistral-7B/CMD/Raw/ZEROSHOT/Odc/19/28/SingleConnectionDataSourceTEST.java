package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisherStub;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionResetEvent;

/**
 * Unit test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {

    private static final String DB_URL = "jdbc:mock:testdb";

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpassword";

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource(true);
        this.connection = mock(Connection.class);
        this.eventPublisher = mock(EventPublisher.class);
        when(this.dataSource.getConnection()).thenReturn(this.connection);
        DataSourceTestUtil.setConnectionPool(BaseClassBasedDataSource.class, () -> this.dataSource);
    }

    @Test
    public void testGetConnection() throws SQLException {
        this.dataSource.setEventPublisher(this.eventPublisher);
        assertNull(this.dataSource.getConnection());

        when(this.connection.isClosed()).thenReturn(false);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(true);

        Connection result = this.dataSource.getConnection();
        assertNotNull(result);
        assertSame(this.connection, result);

        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionResetEvent(this.connection)));
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        this.dataSource.setEventPublisher(this.eventPublisher);

        when(this.connection.isClosed()).thenReturn(false);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(false);

        assertThrows(SQLException.class, () -> this.dataSource.getConnection());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionResetEvent(this.connection)),
                messageCaptor.capture());

        String message = messageCaptor.getValue();
        assertTrue(message.startsWith("Invalid username or password"));
    }

    @Test
    public void testGetConnectionWithClosedConnection() throws SQLException {
        this.dataSource.setEventPublisher(this.eventPublisher);

        when(this.connection.isClosed()).thenReturn(true);

        assertThrows(SQLException.class, () -> this.dataSource.getConnection());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionResetEvent(this.connection)),
                messageCaptor.capture());

        String message = messageCaptor.getValue();
        assertTrue(message.startsWith("Connection was closed or not valid"));
    }

    @Test
    public void testGetConnectionWithUninitializedConnection() throws SQLException {
        this.dataSource.setEventPublisher(this.eventPublisher);

        when(this.connection.isClosed()).thenReturn(false);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(false);

        DataSourceTestUtil.resetConnection(this.dataSource);

        Connection result = this.dataSource.getConnection();
        assertNotNull(result);
        assertSame(this.connection, result);

        verify(this.eventPublisher, times(2)).publishEvent(eq(new ConnectionResetEvent(this.connection)));
    }

    @Test
    public void testGetConnectionWithTimeout() throws SQLException, InterruptedException {
        this.dataSource.setEventPublisher(this.eventPublisher);

        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenThrow(new SQLTimeoutException());

        assertThrows(SQLException.class, () -> this.dataSource.getConnection());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionResetEvent(this.connection)),
                messageCaptor.capture());

        String message = messageCaptor.getValue();
        assertTrue(message.startsWith("Connection initialization failed"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        this.dataSource.setEventPublisher(this.eventPublisher);

        when(this.connection.isClosed()).thenReturn(true);

        this.dataSource.resetConnection();

        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionResetEvent(this.connection)));
    }

    @Test
    public void testAddInitializer() {
        this.dataSource.addInitializer(new DummyConnectionInitializer());

        Connection result = this.dataSource.getConnection();
        assertNotNull(result);

        ArgumentCaptor<Connection> connectionCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(this.connection, times(1)).isValid(eq(this.dataSource.getLoginTimeout()));
        verify(this.connection, times(1)).createStatement();
        verify(this.connection, times(1)).setAutoCommit(eq(false));

        verify(this.dataSource.initializerList, times(1)).add(argThat(c -> c instanceof DummyConnectionInitializer));
    }

    @Test
    public void testInitializeConnection() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);

        Connection connection = mock(Connection.class);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(true);

        this.dataSource.initConnection(connection);

        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testInitializeConnectionWithInvalidConnection() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        Connection connection = mock(Connection.class);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(false);

        this.dataSource.initConnection(connection);

        ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
        verify(initializer, times(0)).init(connection);
        verify(this.dataSource, times(1)).close();

        SQLException exception = (SQLException) exceptionCaptor.getValue();
        assertEquals(ErrorCodes.ConnectionInitializationFailed, exception.getErrorCode());
    }

    @Test
    public void testOnConnectionCreated() {
        this.dataSource.setEventPublisher(this.eventPublisher);

        Connection connection = mock(Connection.class);
        when(this.connection.isValid(eq(this.dataSource.getLoginTimeout()))).thenReturn(true);

        this.dataSource.onConnectionCreated(connection);

        ArgumentCaptor<Connection> connectionCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(this.eventPublisher, times(1)).publishEvent(eq(new ConnectionCreatedEvent(connection)));
    }

    @Test
    public void testAddInitializerWithNullInitializer() {
        this.dataSource.addInitializer(null);

        assertThrows(NullPointerException.class, () -> this.dataSource.getConnection());
    }

    @Test
    public void testSetAutoCommit() {
        this.dataSource.setAutoCommit(true);

        Connection connection = this.dataSource.getConnection();
        connection.setAutoCommit(true);

        this.dataSource.close();

        Connection result = this.dataSource.getConnection();
        assertNotNull(result);
        assertSame(connection, result);

        connection.setAutoCommit(false);

        this.dataSource.setAutoCommit(false);
        result = this.dataSource.getConnection();
        assertNotNull(result);
        assertSame(connection, result);

        connection.setAutoCommit(true);

        this.dataSource.setAutoCommit(true);
        result = this.dataSource.getConnection();
        assertNotNull(result);
        assertSame(connection, result);
    }

    @Test
    public void testSetAutoCommitWithNullDataSource() {
        assertThrows(NullPointerException.class, () -> this.dataSource.setAutoCommit(true));
    }

    @Test
    public void testSetEventPublisher() {
        EventPublisher eventPublisher = new EventPublisherStub();

        this.dataSource.setEventPublisher(eventPublisher);

        assertSame(eventPublisher, this.dataSource.getEventPublisher());
    }

    @Test
    public void testSetEventPublisherWithNullEventPublisher() {
        assertThrows(NullPointerException.class, () -> this.dataSource.setEventPublisher(null));
    }

    @Test
    public void testClose() throws SQLException {
        this.dataSource.close();

        assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = this.dataSource.innerCreateConnection();

        assertNotNull(connection);
        assertSame(this.connection, connection);
    }

    private static class DummyConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) throws SQLException {
            connection.createStatement();
            connection.setAutoCommit(false);
        }
    }

    private static class SQLTimeoutException extends SQLException {
        SQLTimeoutException() {
            super();
        }

        SQLTimeoutException(String message) {
            super(message);
        }

        SQLTimeoutException(String message, Throwable cause) {
            super(message, cause);
        }

        SQLTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

        SQLTimeoutException(String message, Throwable cause, long serialVersionUID) {
            super(message, cause, serialVersionUID);
        }

        @Override
        public int getErrorCode() {
            return ErrorCodes.ConnectionInitializationFailed.getErrorCode();
        }
    }
}