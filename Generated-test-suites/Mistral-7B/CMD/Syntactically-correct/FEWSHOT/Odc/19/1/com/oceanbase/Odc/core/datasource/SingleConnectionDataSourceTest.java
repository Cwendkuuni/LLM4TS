package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

/**
 * Test cases for SingleConnectionDataSource class
 */
public class SingleConnectionDataSourceTest {
    private static final String TEST_DATASOURCE_CLASS_NAME = "com.mock.DatabaseDriver";
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final long TEST_TIMEOUT = 5000;
    private List<ConnectionInitializer> connectionInitializerList;
    private Connection connection;
    private EventPublisher eventPublisher;
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(Connection.class);
        connectionInitializerList = new ArrayList<>();
        eventPublisher = Mockito.mock(EventPublisher.class);
        singleConnectionDataSource = new SingleConnectionDataSource() {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return connection;
            }

            @Override
            protected void onConnectionCreated(Connection connection) {
                super.onConnectionCreated(connection);
                // Do nothing
            }

            @Override
            public void addInitializer(@NonNull ConnectionInitializer connectionInitializer) {
                connectionInitializerList.add(connectionInitializer);
                super.addInitializer(connectionInitializer);
            }

            @Override
            public void onConnectionReset(Connection connection) {
                super.onConnectionReset(connection);
                // Do nothing
            }
        };
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
            }
        });
        when(connection.isValid(anyLong())).thenReturn(true);
    }

    @After
    public void tearDown() throws Exception {
        reset(connection);
    }

    @Test
    public void testGetConnection() throws Exception {
        singleConnectionDataSource.setAutoReconnect(true);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(TEST_TIMEOUT)).thenReturn(true);

        Connection actualConnection = singleConnectionDataSource.getConnection();
        assertEquals(connection, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullPassword() throws Exception {
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword(null);

        try {
            singleConnectionDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNullUsername() throws Exception {
        singleConnectionDataSource.setUsername(null);
        singleConnectionDataSource.setPassword(TEST_PASSWORD);

        try {
            singleConnectionDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws Exception {
        singleConnectionDataSource.setUsername(TEST_USERNAME);
        singleConnectionDataSource.setPassword("InvalidPassword");

        try {
            singleConnectionDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testResetConnection() throws Exception {
        singleConnectionDataSource.setAutoReconnect(true);
        when(connection.isClosed()).thenReturn(true);

        singleConnectionDataSource.resetConnection();

        verify(connection).close();

        Connection actualConnection = singleConnectionDataSource.getConnection();
        assertEquals(connection, actualConnection);
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionFalseAutoReconnect() throws Exception {
        singleConnectionDataSource.setAutoReconnect(false);
        when(connection.isClosed()).thenReturn(true);

        try {
            singleConnectionDataSource.resetConnection();
            fail();
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testAddInitializer() throws Exception {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);

        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertTrue(singleConnectionDataSource.getInitializerList().contains(connectionInitializer));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithoutInitializers() throws Exception {
        singleConnectionDataSource.setAutoCommit(false);

        try {
            singleConnectionDataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals(ErrorCodes.InitializersMissing, e.getErrorCode());
        }
    }

    @Test
    public void testGetConnectionWithInitializers() throws Exception {
        singleConnectionDataSource.setAutoCommit(false);
        singleConnectionDataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
            }
        });

        Connection actualConnection = singleConnectionDataSource.getConnection();
        assertEquals(connection, actualConnection);
    }

    @Test
    public void testEventPublisher() throws Exception {
        ConnectionResetEvent connectionResetEvent = new ConnectionResetEvent(connection);

        singleConnectionDataSource.setEventPublisher(eventPublisher);

        when(connection.isClosed()).thenReturn(true);
        singleConnectionDataSource.resetConnection();

        verify(eventPublisher).publishEvent(connectionResetEvent);
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.setConnection(connection);

        singleConnectionDataSource.close();

        verify(connection).close();
    }
}