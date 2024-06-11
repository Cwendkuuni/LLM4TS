package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.ErrorCodes;
import com.oceanbase.odc.core.datasource.OceanbaseSingleConnectionDataSource;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OceanSingleConnectionDataSourceTest {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private static final boolean AUTO_COMMIT = true;
    private static final boolean AUTO_RECONNECT = true;
    private static final String JOHN_DOE = "john.doe";

    private OceanbaseSingleConnectionDataSource singleConnectionDataSource;
    private Connection connection;
    private Connection initialConnection;
    private List<ConnectionInitializer> connectionInitializers;
    private EventPublisher eventPublisher;
    private Lock lock;

    @Before
    public void setUp() throws Exception {
        Class.forName(DRIVER);
        singleConnectionDataSource = new OceanbaseSingleConnectionDataSource(AUTO_RECONNECT);
        connectionInitializers = new LinkedList<>();
        eventPublisher = Mockito.mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        lock = new ReentrantLock();

        when(eventPublisher.publishEvent(any(ConnectionResetEvent.class))).thenReturn(null);

        singleConnectionDataSource.addInitializer(mock(ConnectionInitializer.class));
        initialConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = singleConnectionDataSource.getConnection();
        assertEquals(initialConnection, connection);
        assertFalse(connection.isClosed());
        assertSame(initialConnection.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testGetConnection_InvalidUsernameOrPassword() throws Exception {
        assertThrows(SQLException.class, () -> {
            singleConnectionDataSource.getConnection("john.doe", "password");
        });
    }

    @Test
    public void testResetConnection() throws Exception {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
        verify(connectionInitializer, times(1)).init(eq(singleConnectionDataSource.getConnection()));
    }

    @Test
    public void testClose() throws Exception {
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        when(singleConnectionDataSource.newConnectionFromDriver(eq(USERNAME), eq(PASSWORD)))
                .thenReturn(connection);

        Connection connectionFromDataSource = singleConnectionDataSource.getConnection();
        assertSame(connection, connectionFromDataSource);
    }

    @Test
    public void testTryLock() throws Exception {
        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        when(singleConnectionDataSource.lock.isHeldByCurrentThread())
                .thenReturn(false);
        Connection connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);
        assertSame(connection, connectionProxy);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnection_ConnectionOccupied() throws Exception {
        when(singleConnectionDataSource.lock.isHeldByCurrentThread())
                .thenReturn(true);
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testInitConnection() throws Exception {
        singleConnectionDataSource.initConnection(connection);
        verify(connectionInitializers, times(1)).init(eq(singleConnectionDataSource.getConnection()));
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        singleConnectionDataSource.onConnectionCreated(connection);
        verify(singleConnectionDataSource, times(1)).prepareConnection(connection);
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @After
    public void tearDown() throws Exception {
        closeQuietly(connection);
        closeQuietly(initialConnection);
        singleConnectionDataSource.close();
    }

    private void closeQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Failed to close the connection", e);
            }
        }
    }
}