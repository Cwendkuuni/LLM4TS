import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Mock
    private Connection connection;
    @Mock
    private EventPublisher eventPublisher;
    @Captor
    private ArgumentCaptor<ConnectionResetEvent> connectionResetEventCaptor;

    private SingleConnectionDataSource dataSource;
    private List<ConnectionInitializer> initializerList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        initializerList = new ArrayList<>();
        dataSource = new SingleConnectionDataSource(true) {
            @Override
            protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
                return this.connection;
            }

            @Override
            public void addInitializer(@NonNull ConnectionInitializer connectionInitializer) {
                super.addInitializer(connectionInitializer);
                initializerList.add(connectionInitializer);
            }

            @Override
            protected void initConnection(Connection connection) throws SQLException {
                super.initConnection(connection);
            }

            @Override
            protected void onConnectionReset(Connection connection) {
                super.onConnectionReset(connection);
            }

            @Override
            public void close() throws Exception {
                super.close();
            }
        };
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.getAutoCommit()).thenReturn(null);

        Connection result = dataSource.getConnection();
        assertNotNull(result);
        assertEquals(dataSource.connection, result);

        verify(connection).isValid(anyInt());
        verifyNoMoreInteractions(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ConnectionIsClosed() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_InvalidCredentials() throws SQLException {
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);

        assertThrows(SQLException.class, () -> {
            dataSource.getConnection("testUser2", "testPassword2");
        });
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(mock(ConnectionInitializer.class));

        assertEquals(1, initializerList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInitializer_NullConnectionInitializer() {
        dataSource.addInitializer(null);
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);

        dataSource.resetConnection();

        verify(connection).close();
        verify(eventPublisher).publishEvent(connectionResetEventCaptor.capture());
        ConnectionResetEvent event = connectionResetEventCaptor.getValue();
        assertNotNull(event);
        assertEquals(dataSource.connection, event.getConnection());
    }

    @Test
    public void testClose() throws Exception {
        dataSource.close();

        verify(connection).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);

        dataSource.innerCreateConnection();

        verify(connection).isValid(anyInt());
        verify(connection).close();
        verify(eventPublisher).publishEvent(connectionResetEventCaptor.capture());
        ConnectionResetEvent event = connectionResetEventCaptor.getValue();
        assertNotNull(event);
        assertEquals(dataSource.connection, event.getConnection());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(connection.isClosed()).thenReturn(false);

        Connection result = dataSource.getConnectionProxy(connection);

        assertEquals(connection, result);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLock_Timeout() {
        assertFalse(dataSource.tryLock());
    }
}