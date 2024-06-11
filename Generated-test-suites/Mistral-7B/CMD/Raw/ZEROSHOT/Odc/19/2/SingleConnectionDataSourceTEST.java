import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link SingleConnectionDataSource}.
 *
 * @author yh263208
 * @since ODC_release_3.2.2
 */
public class SingleConnectionDataSourceTest {
    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";
    private static final long TIME_OUT_MILLIS = 10 * 1000;
    private static final boolean AUTO_COMMIT = true;
    private static final boolean AUTO_RECONNECT = true;

    private EventPublisher eventPublisher;
    private List<ConnectionInitializer> initializerList;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.eventPublisher = mock(EventPublisher.class);
        this.initializerList = new ArrayList<>();
        this.dataSource = new SingleConnectionDataSource(AUTO_RECONNECT);
        this.dataSource.setAutoCommit(AUTO_COMMIT);
        this.dataSource.setEventPublisher(eventPublisher);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        Connection result = dataSource.getConnection();
        assertEquals(connection, result);

        verify(dataSource, times(1)).prepareConnection(connection);
        verify(dataSource, times(1)).getConnectionProxy(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
        when(dataSource.getUsername()).thenReturn(USERNAME);
        when(dataSource.getPassword()).thenReturn(PASSWORD);

        String wrongUsername = "wrongUsername";
        String wrongPassword = "wrongPassword";

        try {
            dataSource.getConnection(wrongUsername, wrongPassword);
            fail();
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test(expected = ConflictException.class)
    public void testResetConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        when(dataSource.innerCreateConnection()).thenThrow(SQLException.class);
        when(dataSource.getConnection()).thenReturn(connection);

        dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> eventArgumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventArgumentCaptor.capture());

        ConnectionResetEvent event = eventArgumentCaptor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializer);

        assertEquals(initializer, dataSource.getInitializerList().get(0));
    }

    @Test
    public void testClose() throws Exception {
        Connection connection = mock(Connection.class);

        dataSource.setConnection(connection);
        dataSource.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = newConnectionFromDriver(USERNAME, PASSWORD);

        when(dataSource.newConnectionFromDriver(USERNAME, PASSWORD)).thenReturn(connection);

        Connection result = dataSource.innerCreateConnection();

        assertEquals(connection, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConnectionWhenConnectionIsNotNull() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        try {
            dataSource.getConnection();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Connection is not null", e.getMessage());
        }
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionIsClosedOrInvalid() throws SQLException {
        Connection connection = mock(Connection.class);
        doReturn(true).when(connection).isClosed();
        doReturn(false).when(connection).isValid(TIME_OUT_MILLIS);
        dataSource.setConnection(connection);

        try {
            dataSource.getConnection();
            fail();
        } catch (SQLException e) {
            assertEquals("Connection was closed or not valid", e.getMessage());
        }
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = mock(Connection.class);
        Lock lock = new ReentrantLock();

        Connection result = dataSource.getConnectionProxy(connection);

        assertTrue(result instanceof Proxy);
        assertEquals(Connection.class, result.getClass().getInterfaces()[0]);
        assertEquals(Proxy.getInvocationHandler(result), new CloseIgnoreInvocationHandler(connection, lock));
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        doReturn(AUTO_COMMIT).when(dataSource).getAutoCommit();

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(AUTO_COMMIT);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        ConnectionInitializer initializer1 = mock(ConnectionInitializer.class);
        ConnectionInitializer initializer2 = mock(ConnectionInitializer.class);

        when(dataSource.getInitializerList()).thenReturn(initializerList);
        dataSource.addInitializer(initializer1);
        dataSource.addInitializer(initializer2);

        dataSource.initConnection(connection);

        inOrder(initializer1, initializer2).verify(connection, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = mock(Connection.class);

        dataSource.onConnectionCreated(connection);

        verify(dataSource, times(1)).log.debug("Connection is created");
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);

        dataSource.onConnectionReset(connection);

        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    // Other test methods for various methods in the class can be added here

}