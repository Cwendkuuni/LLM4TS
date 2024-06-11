import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisherMock;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull_shouldCreateNewConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWhenConnectionIsValid_shouldReturnExistingConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection newConnection = dataSource.getConnection();
        assertEquals(connection, newConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionIsClosedAndAutoReconnectFalse_shouldThrowSQLException() throws SQLException {
        dataSource.getConnection();
        dataSource.getConnection().close();
        dataSource.getConnection();
    }

    @Test
    public void testAddInitializer_shouldAddInitializerToList() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testResetConnection_shouldResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
    }

    @Test
    public void testClose_shouldCloseConnection() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnectionWhenConnectionIsNull_shouldCreateNewConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWhenConnectionIsNotNull_shouldThrowIllegalStateException() throws SQLException {
        dataSource.getConnection();
        dataSource.innerCreateConnection();
    }

    @Test
    public void testGetConnectionProxy_shouldReturnProxyConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
    }

    @Test
    public void testOnConnectionResetShouldPublishEventWhenEventPublisherNotNull() throws SQLException {
        dataSource.resetConnection();
        verify(eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

}