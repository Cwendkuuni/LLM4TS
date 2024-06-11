package com.oceanbase.odc.core.datasource;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.setTimeOutMillis(5000);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("username", "password"));
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(connectionInitializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
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
        singleConnectionDataSource.setOnConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.innerCreateConnection();
        assertNotNull(connection);
    }
    
}