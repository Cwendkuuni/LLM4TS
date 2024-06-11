package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.ReentrantLock;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

/**
 * Test class for {@link SingleConnectionDataSource}
 *
 * @author yh263208
 */
public class SingleConnectionDataSourceTest {
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final boolean AUTO_COMMIT = true;
    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private static final boolean AUTO_RECONNECT = true;

    private EventPublisher eventPublisher;
    private Connection connection;
    private SingleConnectionDataSource dataSource;

    @Captor
    private ArgumentCaptor<ConnectionInitializer> initializerCaptor;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource = new SingleConnectionDataSource(AUTO_RECONNECT);
        dataSource.setAutoCommit(AUTO_COMMIT);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.addInitializer(initializer);
        dataSource.setTimeOutMillis(TIME_OUT_MILLIS);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertSame(connection, dataSource.getConnection());
        assertTrue(dataSource.isAutoCommit());
        verify(eventPublisher).publishEvent(argThat(event -> event instanceof ConnectionResetEvent));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        String invalidUsername = "testInvalidUser";
        dataSource.setUsername(invalidUsername);
        dataSource.setPassword(PASSWORD);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection originalConnection = innerCreateConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        assertFalse(originalConnection.isClosed());
        dataSource.setConnection(originalConnection);
        Connection newConnection = dataSource.getConnection();
        assertSame(originalConnection, newConnection);
        verify(eventPublisher).publishEvent(argThat(event -> event instanceof ConnectionResetEvent));
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(mock(ConnectionInitializer.class));
        List<ConnectionInitializer> initializers = new ArrayList<>();
        dataSource.getInitializerList().forEach(initializers::add);
        assertEquals(1, initializers.size());
    }

    @Test
    public void testClose() {
        Connection connection = innerCreateConnection();
        dataSource.setConnection(connection);
        dataSource.close();
        assertNull(dataSource.getConnection());
        connection.close();
    }

    private Connection innerCreateConnection() throws SQLException {
        dataSource.setConnection(null);
        Connection connection = dataSource.newConnectionFromDriver(USERNAME, PASSWORD);
        dataSource.setConnection(connection);
        return connection;
    }

    private static <T> boolean equals(T expected, T actual) {
        return expected.equals(actual);
    }
}