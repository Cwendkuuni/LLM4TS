package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

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
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("user", "pass");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testClose() {
        dataSource.close();
        // No exception should be thrown
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(anyBoolean());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Lock lock = mock(Lock.class);
        dataSource.setLock(lock);
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer).init(connection);
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertNotNull(dataSource.getLock());
    }

}