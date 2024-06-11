package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // initialization logic
            }
        });
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Test getting a connection for the first time
        assertNotNull(dataSource.getConnection());

        // Test getting a connection after the first one has been closed
        dataSource.close();
        assertThrows(SQLException.class, () -> dataSource.getConnection());

        // Test getting a connection after the first one has been reset
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());

        // Test getting a connection with a different username and password
        assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUsername", "wrongPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        // Test resetting a connection that is null
        dataSource.resetConnection();

        // Test resetting a connection that is already closed
        dataSource.close();
        dataSource.resetConnection();

        // Test resetting a connection that is already reset
        dataSource.resetConnection();
        dataSource.resetConnection();

        // Test publishing an event when resetting a connection
        dataSource.resetConnection();
        assertTrue(dataSource.eventPublisher.isPublished(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        // Test adding a null initializer
        assertThrows(NullPointerException.class, () -> dataSource.addInitializer(null));

        // Test adding an empty list of initializers
        dataSource.addInitializer(new ArrayList<>());

        // Test adding a list of initializers with one or more elements
        List<ConnectionInitializer> initializers = new ArrayList<>();
        initializers.add(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                // initialization logic
            }
        });
        dataSource.addInitializer(initializers);
    }

    @After
    public void cleanup() {
        dataSource.close();
    }
}