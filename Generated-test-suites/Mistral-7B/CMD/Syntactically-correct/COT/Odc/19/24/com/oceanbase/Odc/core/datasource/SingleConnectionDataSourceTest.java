import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private List<ConnectionInitializer> initializerList;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        initializerList = new ArrayList<>();
        eventPublisher = mock(EventPublisher.class);
    }

    @Test
    public void testGetConnection() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                assertEquals("Test Init", connection.getMetaData().getDatabaseProductName());
            }
        });
        dataSource.eventPublisher = eventPublisher;

        Connection result = dataSource.getConnection();
        assertNotNull(result);
        verify(eventPublisher).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("InvalidUsername", "InvalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.connection = connection;
        dataSource.resetConnection();
        assertNull(dataSource.connection);
    }

    @Test
    public void testTryLockSuccess() {
        dataSource.lock = Mockito.mock(Lock.class);
        ArgumentCaptor<Long> timeOutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> timeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);
        assertTrue(dataSource.tryLock());
        verify(dataSource.lock).tryLock(timeOutCaptor.capture(), timeUnitCaptor.capture());
        assertEquals(10000L, timeOutCaptor.getValue().longValue());
        assertEquals(TimeUnit.MILLISECONDS, timeUnitCaptor.getValue());
    }

    @Test
    public void testTryLockFailure() {
        dataSource.lock = Mockito.mock(Lock.class);
        ArgumentCaptor<Long> timeOutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> timeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);
        assertFalse(dataSource.tryLock());
        verify(dataSource.lock, Mockito.never()).tryLock(timeOutCaptor.capture(), timeUnitCaptor.capture());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithLockedConnection() throws SQLException {
        dataSource.lock = mock(Lock.class);
        dataSource.connection = connection;
        when(dataSource.lock.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(true);
        when(dataSource.lock.unlock()).thenThrow(new RuntimeException());

        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertNotNull(connectionProxy.getMetaData());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        Connection connection = mock(Connection.class);
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, null);
        assertEquals(connection, handler.proxy);
    }

    @After
    public void tearDown() {
        dataSource = null;
        connection = null;
        initializerList = null;
        eventPublisher = null;
    }
}