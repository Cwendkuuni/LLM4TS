package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.util.ConnectionInitializer;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setup() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializerList = singleConnectionDataSource.getInitializerList();
        assertEquals(1, initializerList.size());
        assertEquals(connectionInitializer, initializerList.get(0));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        assertTrue(singleConnectionDataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.getConnectionProxy(connection);
        singleConnectionDataSource.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.initConnection(connection);
        verify(connectionInitializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        // Not much to test since it's only logging
        singleConnectionDataSource.onConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.getConnectionProxy(connection);
        singleConnectionDataSource.getConnectionProxy(connection);
        singleConnectionDataSource.getConnectionProxy(connection);
        singleConnectionDataSource.getConnectionProxy(connection);
    }

}