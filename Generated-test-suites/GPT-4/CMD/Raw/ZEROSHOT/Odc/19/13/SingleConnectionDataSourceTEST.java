
package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource ds;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        ds = new SingleConnectionDataSource(true);
        connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
    }

    @Test
    public void testAutoReconnectGetterAndSetter() {
        ds.setAutoReconnect(true);
        assertTrue(ds.getAutoReconnect());

        ds.setAutoReconnect(false);
        assertFalse(ds.getAutoReconnect());
    }

    @Test
    public void testEventPublisherGetterAndSetter() {
        EventPublisher ep = new EventPublisher();
        ds.setEventPublisher(ep);
        assertSame(ep, ds.getEventPublisher());
    }

    @Test
    public void testTimeOutMillisGetterAndSetter() {
        ds.setTimeOutMillis(5000);
        assertEquals(5000, ds.getTimeOutMillis());

        ds.setTimeOutMillis(1000);
        assertNotEquals(5000, ds.getTimeOutMillis());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        assertNotEquals(connection, ds.getConnection("test", "test"));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        ds.getConnection("invalid", "invalid");
    }

    @Test
    public void testResetConnection() throws SQLException {
        ds.setConnection(connection);
        ds.resetConnection();
        assertNull(ds.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer ci = new ConnectionInitializer();
        ds.addInitializer(ci);
        assertTrue(ds.getBaselineInitializers().contains(ci));
    }

    @Test(expected = SQLException.class)
    public void testResetConnectionWithException() throws SQLException {
        when(connection.close()).thenThrow(SQLException.class);
        ds.resetConnection();
    }

    @Test(expected = RuntimeException.class)
    public void testGetConnectionWithLockTimeout() throws SQLException, InterruptedException {
        when(connection.isValid(anyInt())).thenThrow(InterruptedException.class);
        ds.getConnection();
    }
}
