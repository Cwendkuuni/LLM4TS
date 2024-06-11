package com.oceanbase.odc.core.datasource;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.sql.Connection;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource ds;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        ds = new SingleConnectionDataSource();
        conn = mock(Connection.class);
        ds.setConnection(conn);
    }

    @Test
    public void testSetAndGetAutoCommit() {
        ds.setAutoCommit(true);
        assertTrue(ds.getAutoCommit());
    }

    @Test
    public void testSetAndGetEventPublisher() {
        EventPublisher publisher = mock(EventPublisher.class);
        ds.setEventPublisher(publisher);
        assertEquals(publisher, ds.getEventPublisher());
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(conn.isValid(anyInt())).thenReturn(true);
        Connection obtainedConnection = ds.getConnection("username", "password");
        assertNotNull(obtainedConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        ds.getConnection("wrong_username", "password");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithClosedConnection() throws SQLException {
        when(conn.isValid(anyInt())).thenReturn(false);
        ds.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        // Before reset
        assertNotNull(ds.getConnection("username", "password"));

        ds.resetConnection();

        // After reset
        assertNull(ds.getConnection("username", "password"));
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        ds.addInitializer(initializer);
        
        // Assuming there is a getter for initializerList
        assertEquals(1, ds.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        ds.close();
        verify(conn, times(1)).close();
    }
}