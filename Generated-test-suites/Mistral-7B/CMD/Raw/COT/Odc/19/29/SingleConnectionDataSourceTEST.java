import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    private static final String DATABASE_URL = "jdbc:mock:database";
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;

    @Before
    public void setup() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(mock(EventPublisher.class));

        connection = Mockito.mock(Connection.class);
        Mockito.when(connection.isValid(Mockito.anyInt())).thenReturn(true);
        Mockito.when(connection.isClosed()).thenReturn(false);
        Mockito.when(connection.getAutoCommit()).thenReturn(false);

        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                Mockito.doNothing().when(connection).prepareCall("SELECT 1");
            }
        });
        dataSource.initConnection(connection);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection result = dataSource.getConnection();
        Assert.assertNotNull(result);
        verify(dataSource.getEventPublisher()).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalidUsername");
        dataSource.setPassword("invalidPassword");
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection result = dataSource.getConnectionProxy(connection);
        Assert.assertSame(connection, result);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithTimeout() throws SQLException, InterruptedException {
        dataSource.setTimeOutMillis(1);
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Mockito.verify(dataSource.getEventPublisher()).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(Mockito.mock(ConnectionInitializer.class));
        List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
        Assert.assertEquals(1, initializerList.size());
    }

    @Test
    public void testClose() {
        dataSource.close();
        Assert.assertNull(dataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection result = dataSource.innerCreateConnection();
        Assert.assertNotNull(result);
        Assert.assertEquals(connection, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConnectionWithConnectionNotnull() throws SQLException {
        dataSource.setConnection(connection);
        dataSource.getConnection();
    }
}