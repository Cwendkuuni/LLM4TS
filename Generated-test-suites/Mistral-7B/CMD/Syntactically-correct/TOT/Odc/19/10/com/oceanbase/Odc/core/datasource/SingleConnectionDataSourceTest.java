package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.event.ConnectionInitializerStub;
import com.oceanbase.odc.core.datasource.event.ConnectionResetEventStub;

/**
 * Test class for {@link SingleConnectionDataSource}.
 */
public class SingleConnectionDataSourceTest {

    private ConnectionInitializerStub connectionInitializer;
    private EventPublisher eventPublisher;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        connectionInitializer = new ConnectionInitializerStub();
        eventPublisher = mock(EventPublisher.class);
        dataSource = new SingleConnectionDataSource(true, connectionInitializer, eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertEquals("SingleConnectionDataSource returned a connection", connection, dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testGetConnectionWithNullAutoCommit() throws SQLException {
        dataSource.setAutoCommit(null);
        Connection connection = dataSource.getConnection();
        assertNull("Autocommit is null, connection should not have it set", connection.getAutoCommit());
    }

    @Test
    public void testGetConnectionWithAutoCommit() throws SQLException {
        dataSource.setAutoCommit(Boolean.TRUE);
        Connection connection = dataSource.getConnection();
        assertTrue("Autocommit is set to true, connection should have it set", connection.getAutoCommit());
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull("Connection has been reset, it should be null", dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals("Initializer was added to the list", 1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws Exception {
        try {
            dataSource.close();
        } finally {
            assertNull("Connection has been closed, it should be null", dataSource.getConnection());
        }
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);

        dataSource.onConnectionReset(connection);
        eventPublisher.publishEvent(ArgumentCaptor.capture(ConnectionResetEvent.class));
        ConnectionResetEvent event = ArgumentCaptor.getOnlyArgumentCaptor(ConnectionResetEvent.class).getValue();
        assertEquals("Connection has been reset, the event should contain the connection", connection, event.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull("A connection was created", connection);
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }
}

class BaseClassBasedDataSourceTest {

    @Test
    public void testBaseClassBasedDataSourceGetConnection() throws SQLException {
        BaseClassBasedDataSource dataSource = mock(BaseClassBasedDataSource.class);
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        assertEquals("BaseClassBasedDataSource returned a connection", connection, dataSource.getConnection());
    }

    @Test
    public void testBaseClassBasedDataSourceGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        BaseClassBasedDataSource dataSource = mock(BaseClassBasedDataSource.class);
        when(dataSource.getUsername()).thenReturn("invalidUsername");
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }
}

class ConnectionInitializerStub implements ConnectionInitializer {

    @Override
    public void init(Connection connection) throws SQLException {
        // Do nothing
    }

    @Override
    public String getUsername() {
        return "testUsername";
    }
}

class ConnectionResetEventStub implements ConnectionResetEvent {

    @Getter
    private Connection connection;

    public ConnectionResetEventStub(Connection connection) {
        this.connection = connection;
    }
}