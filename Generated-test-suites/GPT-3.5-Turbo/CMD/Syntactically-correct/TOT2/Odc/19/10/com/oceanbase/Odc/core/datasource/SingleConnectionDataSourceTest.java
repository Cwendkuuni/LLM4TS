import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
        dataSource.setAutoReconnect(false);
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testGetConnectionProxyWhenConnectionIsValid() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.isValid(5)).thenReturn(true);
        dataSource.setConnection(mockedConnection);
        Connection connection = dataSource.getConnectionProxy(mockedConnection);
        Assert.assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionProxyWhenConnectionIsClosed() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        when(mockedConnection.isValid(5)).thenReturn(false);
        dataSource.setConnection(mockedConnection);
        dataSource.getConnectionProxy(mockedConnection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        dataSource.setConnection(mockedConnection);
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        Assert.assertNotNull(newConnection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        Assert.assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testCloseConnection() {
        Connection mockedConnection = mock(Connection.class);
        dataSource.setConnection(mockedConnection);
        dataSource.close();
        Assert.assertNull(dataSource.getConnection());
    }

    @Test
    public void testConstructorWithAutoReconnect() {
        SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
        Assert.assertTrue(dataSourceWithAutoReconnect.isAutoReconnect());
    }

}