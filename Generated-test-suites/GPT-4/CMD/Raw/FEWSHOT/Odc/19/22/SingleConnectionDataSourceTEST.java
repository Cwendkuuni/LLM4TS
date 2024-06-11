
package com.oceanbase.odc.core.datasource;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        Assert.assertNotNull(conn);
    }

    @Test
    public void testInvalidGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Assert.assertThrows(SQLException.class, () -> dataSource.getConnection("foo", "bar"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        Assert.assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        doNothing().when(initializer).init(any(Connection.class));

        dataSource.addInitializer(initializer);
        verify(initializer, atLeastOnce()).init(any(Connection.class));
    }

    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        doNothing().when(initializer).init(any(Connection.class));

        dataSource.addInitializer(initializer);
        dataSource.resetConnection();
        verify(initializer, atLeastOnce()).init(any(Connection.class));
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();
        dataSource.close();
        Assert.assertTrue(conn.isClosed());
    }

    @Test
    public void testAutoCommit() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        Assert.assertTrue(dataSource.getAutoCommit());
    }
    
    @Test
    public void testTimeOutMillis() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setTimeOutMillis(20000);
        Assert.assertEquals(20000, dataSource.getTimeOutMillis());
    }
}
