import com.oceanbase.odc.core.datasource.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Test class for SingleConnectionDataSource
 */
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private Connection initialConnection;

    @Mock
    private Connection resetConnection;

    @Mock
    private ConnectionInitializer initializerMock;

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    private List<ConnectionInitializer> initializerList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setEventPublisher(eventPublisher);
        initializerList = new LinkedList<>();
        singleConnectionDataSource.setInitializerList(initializerList);
    }

    @Test
    public void testGetConnectionNormalScenario() throws SQLException {
        given(eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .willReturn(null);

        when(initialConnection.isValid(anyLong()))
                .thenReturn(true);

        when(initialConnection.getAutoCommit())
                .thenReturn(null);

        Connection connection = singleConnectionDataSource.getConnection();

        verify(initialConnection).isValid(anyLong());
        verify(initialConnection).getAutoCommit();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidConnection() throws SQLException {
        given(eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .willReturn(null);

        when(initialConnection.isValid(anyLong()))
                .thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWrongCredentials() throws SQLException {
        given(eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .willReturn(null);

        when(initialConnection.isValid(anyLong()))
                .thenReturn(true);

        when(initialConnection.getAutoCommit())
                .thenReturn(false);

        when(initialConnection.isClosed())
                .thenReturn(true);

        singleConnectionDataSource.getConnection("wrong_username", "wrong_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        given(eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .willReturn(null);

        Connection connection = singleConnectionDataSource.getConnectionProxy(resetConnection);

        singleConnectionDataSource.resetConnection();

        verify(resetConnection).close();
        verify(singleConnectionDataSource.getLock()).unlock();
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(initializerMock);

        verify(initializerList)
                .add(initializerMock);
    }

    @Test(expected = ConflictException.class)
    public void testConnectionOccupied() throws SQLException {
        when(singleConnectionDataSource.tryLock())
                .thenReturn(false);

        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        given(eventPublisher.publishEvent(any(ConnectionResetEvent.class)))
                .willReturn(null);

        singleConnectionDataSource.setEventPublisher(eventPublisher);

        Connection connection = singleConnectionDataSource.getConnection();

        singleConnectionDataSource.resetConnection();

        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.innerCreateConnection();

        verify(singleConnectionDataSource, times(1))
                .newConnectionFromDriver(anyString(), anyString());

        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testConnectionCreationFailure() throws SQLException {
        doThrow(new SQLException("test exception"))
                .when(singleConnectionDataSource)
                .newConnectionFromDriver(anyString(), anyString());

        singleConnectionDataSource.getConnection();
    }

    private class ConnectionResetEvent {
        private final Connection connection;

        ConnectionResetEvent(Connection connection) {
            this.connection = connection;
        }

        Connection getConnection() {
            return connection;
        }
    }
}