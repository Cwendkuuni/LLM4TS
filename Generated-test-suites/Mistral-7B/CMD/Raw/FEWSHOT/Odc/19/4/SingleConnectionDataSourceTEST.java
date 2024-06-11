package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.DefaultEventPublisher;

/**
 * Test cases for {@link SingleConnectionDataSource} class.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {
    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final long TEST_TIMEOUT_MILLIS = 10 * 1000;
    private static final int TEST_CONNECTION_TIMEOUT = 1;

    private Connection initialConnection;
    private Connection resetConnection;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() throws Exception {
        initialConnection = mock(Connection.class, Answers.RETURNS_DEEP_STUBS);
        resetConnection = mock(Connection.class, Answers.RETURNS_DEEP_STUBS);
        eventPublisher = new DefaultEventPublisher();
        initializerList = new ArrayList<>();
        dataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return Objects.equals(username, TEST_USERNAME) && Objects.equals(password, TEST_PASSWORD)
                        ? initialConnection : resetConnection;
            }
        };
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws Exception {
        when(initialConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(true);
        when(initialConnection.getAutoCommit()).thenReturn(null);

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Statement statement = connection.createStatement();
                statement.execute("INSERT INTO test_table VALUES(1)");
                statement.close();
            }
        });

        Connection connection = dataSource.getConnection();
        assertSame(initialConnection, connection);
        assertTrue(connection.isValid(TEST_CONNECTION_TIMEOUT));
        assertNull(connection.getAutoCommit());

        dataSource.prepareConnection(connection);

        Connection connection2 = dataSource.getConnection();
        assertSame(initialConnection, connection2);
        assertTrue(connection2.isValid(TEST_CONNECTION_TIMEOUT));
        assertNull(connection2.getAutoCommit());

        when(initialConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(false);
        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws Exception {
        String wrongUsername = "wrongUsername";
        try {
            dataSource.getConnection(wrongUsername, TEST_PASSWORD);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // expected
        }

        try {
            dataSource.getConnection(TEST_USERNAME, wrongPassword);
            fail("Expected SQLException");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testResetConnection() throws Exception {
        when(initialConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(true);
        when(initialConnection.isClosed()).thenReturn(false);
        when(resetConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(false);

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // empty
            }
        });

        Connection connection = dataSource.getConnection();
        assertSame(initialConnection, connection);
        assertTrue(connection.isValid(TEST_CONNECTION_TIMEOUT));

        dataSource.resetConnection();

        Connection connection2 = dataSource.getConnection();
        assertSame(resetConnection, connection2);
        assertFalse(connection2.isValid(TEST_CONNECTION_TIMEOUT));

        when(resetConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(true);
        Connection connection3 = dataSource.getConnection();
        assertSame(resetConnection, connection3);
        assertTrue(connection3.isValid(TEST_CONNECTION_TIMEOUT));
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(new ConnectionInitializer() {
            // empty
        });
    }

    @Test(expected = SQLException.class)
    public void testClose() throws Exception {
        dataSource.close();
        dataSource.getConnection();
    }

    @Test
    public void testOnConnectionReset() {
        ConnectionResetEvent event = new ConnectionResetEvent(resetConnection);
        when(resetConnection.isValid(TEST_CONNECTION_TIMEOUT)).thenReturn(false);

        dataSource.onConnectionReset(resetConnection);

        ArgumentCaptor<ConnectionResetEvent> eventCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ConnectionResetEvent publishedEvent = eventCaptor.getValue();
        assertSame(resetConnection, publishedEvent.getConnection());
    }
}