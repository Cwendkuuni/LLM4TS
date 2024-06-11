package com.oceanbase.odc.core.datasource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnectionNew() throws SQLException {
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull("The connection should be created", connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionClosed() throws SQLException {
        SingleConnectionDataSource dataSourceSpy = Mockito.spy(dataSource);
        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.isClosed()).thenReturn(true);
        Mockito.when(dataSourceSpy.getConnection()).thenReturn(mockConnection);
        dataSourceSpy.getConnection();
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        Connection connection = dataSource.getConnection("user", "password");
        Assert.assertNotNull("The connection should be created", connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalid_user", "invalid_password");
    }

    @Test
    public void testAddInitializer() throws SQLException {
        Connection connection = dataSource.getConnection("user", "password");
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Mockito.verify(initializer, Mockito.times(1)).init(connection);
    }

    @Test(expected = SQLException.class)
    public void testAddNullInitializer() throws SQLException {
        dataSource.getConnection("user", "password");
        dataSource.addInitializer(null);
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection("user", "password");
        Assert.assertNull("The connection should be null", connection);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection("user", "password");
        dataSource.close();
        Assert.assertNull("The connection should be null", dataSource.getConnection());
    }
}