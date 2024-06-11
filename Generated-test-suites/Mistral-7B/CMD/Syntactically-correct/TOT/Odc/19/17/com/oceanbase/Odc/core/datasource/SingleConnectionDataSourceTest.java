package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.impl.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.common.event.EventType;

/**
 * Tests for {@link SingleConnectionDataSource}
 */
public class SingleConnectionDataSourceTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final int TEST_TIMEOUT = 500;
    private static final long TEST_TIMEOUT_MILLIS = TEST_TIMEOUT * 1000;
    private SingleConnectionDataSource dataSource;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        eventPublisher = new TestEventPublisher();
        dataSource.setEventPublisher(eventPublisher);
        connectionInitializer = new ConnectionInitializerImpl();
        dataSource.addInitializer(connectionInitializer);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalidUsername");
        dataSource.setPassword("invalidPassword");
        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        connection = dataSource.getConnection();
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertSame(connection, connectionProxy);
    }

    @Test(expected = ConflictException.class)
    public void testConnectionReset() throws SQLException {
        connection = dataSource.getConnection();
        dataSource.resetConnection();
        dataSource.getConnection();
        fail("Expected ConflictException");
    }

    @Test
    public void testConnectionResetWithAutoReconnect() throws SQLException {
        connection = dataSource.getConnection();
        dataSource.resetConnection();
        connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        connection = dataSource.getConnection();
        dataSource.resetConnection();
        verifyConnectionResetEventPublished();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        connection = dataSource.getConnection();
        dataSource.prepareConnection(connection);
        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testInitConnection() throws SQLException {
        connection = dataSource.getConnection();
        dataSource.initConnection(connection);
        verifyConnectionInitialized();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertEquals(connectionInitializer, initializerList.get(0));
    }

    @Test
    public void testClose() throws SQLException, InterruptedException {
        connection = dataSource.getConnection();
        dataSource.close();
        try {
            dataSource.getConnection();
            fail("Expected ConnectionClosedException");
        } catch (SQLException e) {
            assertTrue(e instanceof SQLException);
            assertEquals("Connection was closed or not valid", e.getMessage());
        }
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        connection = dataSource.getConnection();
        Connection closer = dataSource.getConnectionProxy(connection);
        try {
            closer.close();
            fail("Expected null");
        } catch (Throwable ignored) {
        }
    }

    private void verifyConnectionInitialized() throws SQLException {
        assertEquals(1, dataSource.getInitializerList().size());
        Connection connection = dataSource.getConnection();
        assertEquals(1, connection.getMetaData().getDriverMajorVersion());
        assertEquals(1, connection.getMetaData().getDriverMinorVersion());
        assertEquals(1, connection.getMetaData().getDriverVersion());
    }

    private void verifyConnectionResetEventPublished() {
        verifyPublishedEvent(EventType.CONNECTION_RESET, Arrays.asList(connection));
    }

    private void verifyPublishedEvent(EventType eventType, List<Object> expectedData) {
        List<Object> eventData = eventPublisher.getPublishedEvents().get(0).getData();
        assertEquals(eventType, eventData.get(0));
        assertEquals(expectedData, eventData.subList(1, eventData.size()));
    }

}

class TestEventPublisher implements EventPublisher {

    private final List<EventData> publishedEvents = new LinkedList<>();

    @Override
    public void publishEvent(EventData eventData) {
        publishedEvents.add(eventData);
    }

    public List<EventData> getPublishedEvents() {
        return publishedEvents;
    }

}