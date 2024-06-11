import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;

    private SingleConnectionDataSource singleConnectionDataSource;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() {
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        initializerList = new ArrayList<>();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        singleConnectionDataSource.addInitializer(connectionInitializer);
    }

    @Test
    public void testGetConnection() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        Connection connection = singleConnectionDataSource.getConnection();
        Assert.assertNotNull(connection);

        // Test connection reset when connection is closed
        Connection closedConnection = Mockito.spy(connection);
        Mockito.doReturn(true).when(closedConnection).isClosed();
        singleConnectionDataSource.setConnection(closedConnection);
        singleConnectionDataSource.resetConnection();
        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWrongCredentials() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        singleConnectionDataSource.setUsername("wrongUsername");
        singleConnectionDataSource.setPassword("wrongPassword");

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionTimeout() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        Connection connection = Mockito.spy(singleConnectionDataSource.getConnection());
        Mockito.doThrow(new SQLException("timeout")).when(connection).isValid(ArgumentMatchers.anyLong());

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);
        Mockito.verify(connectionInitializer, times(1)).init(ArgumentMatchers.any(Connection.class));
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.setConnection(connection);

        singleConnectionDataSource.close();

        Mockito.verify(connection, times(1)).close();
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = Mockito.mock(Connection.class);

        given(connectionInitializer.init(connection)).willReturn(null);

        singleConnectionDataSource.addInitializer(connectionInitializer);
        singleConnectionDataSource.setConnection(connection);

        singleConnectionDataSource.onConnectionCreated(connection);

        Mockito.verify(eventPublisher, times(1)).publishEvent(ArgumentMatchers.instanceOf(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        Connection connection = singleConnectionDataSource.getConnection();

        Assert.assertNotNull(connection);
        Assert.assertEquals(singleConnectionDataSource.getConnectionProxy(connection), connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        Connection connection = Mockito.spy(singleConnectionDataSource.getConnectionProxy(connection));
        Mockito.doReturn(true).when(connection).isClosed();

        singleConnectionDataSource.resetConnection();

        Assert.assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        given(connectionInitializer.init(ArgumentMatchers.any(Connection.class))).willReturn(null);

        Connection connection = singleConnectionDataSource.innerCreateConnection();

        Assert.assertNotNull(connection);
        Assert.assertEquals(singleConnectionDataSource.getConnectionProxy(connection), connection);
    }
}