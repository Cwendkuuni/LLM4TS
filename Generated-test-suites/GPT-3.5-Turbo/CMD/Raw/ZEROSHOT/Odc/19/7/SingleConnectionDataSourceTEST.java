package com.oceanbase.odc.core.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "password"));
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
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(anyBoolean());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotEquals(connection, proxy);
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
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
        // Verify if debug log is created
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setEventPublisher(mock(EventPublisher.class));
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        // Verify event published
    }

}