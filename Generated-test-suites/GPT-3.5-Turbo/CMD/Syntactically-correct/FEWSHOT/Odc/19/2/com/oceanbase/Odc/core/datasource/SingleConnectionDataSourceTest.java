package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "pass"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, connection::isClosed);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(anyBoolean());
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Connection proxiedConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxiedConnection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setOnConnectionReset(mock(EventPublisher.class));
        dataSource.onConnectionReset(mock(Connection.class));
        verify(dataSource.getEventPublisher(), times(1)).publishEvent(any());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
    }
}