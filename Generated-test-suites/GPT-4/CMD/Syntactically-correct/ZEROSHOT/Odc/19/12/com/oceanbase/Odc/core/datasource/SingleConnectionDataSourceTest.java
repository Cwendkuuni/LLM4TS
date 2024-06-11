
package com.oceanbase.odc.core.datasource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;
    private ConnectionInitializer initializer;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
        initializer = mock(ConnectionInitializer.class);
    }

    @Test(expected= SQLException.class)
    public void testGetConnectionWithUserPassword() throws SQLException{
        singleConnectionDataSource.getConnection("WrongUsername", "WrongPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testClose() {
        singleConnectionDataSource.close();
        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testInvalidUsernameAndPassword() throws SQLException {
        singleConnectionDataSource.getConnection("username","password");
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(initializer);
        Assert.assertFalse(singleConnectionDataSource.getInitializerList().isEmpty());
    }

    @Test
    public void testAutoReconnect() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        Assert.assertTrue(singleConnectionDataSource.isAutoReconnect());
    }

    @Test
    public void testAutoCommitSettings() throws SQLException {
        Connection mockConn = mock(Connection.class);
        when(mockConn.getAutoCommit()).thenReturn(false);
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.prepareConnection(mockConn);
        verify(mockConn).setAutoCommit(true);
    }

    @Test
    public void testConnectionResetEvent() {
        EventPublisher mockPublisher = mock(EventPublisher.class);
        Connection mockConn = mock(Connection.class);
        singleConnectionDataSource.setEventPublisher(mockPublisher);
        singleConnectionDataSource.onConnectionReset(mockConn);
        verify(mockPublisher).publishEvent(any(ConnectionResetEvent.class));
    }
}
