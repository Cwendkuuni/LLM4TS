
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private EventPublisher mockEventPublisher;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        singleConnectionDataSource = new SingleConnectionDataSource(true);
        singleConnectionDataSource.setEventPublisher(mockEventPublisher);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);
    }

    @Test
    public void testGetConnection() throws SQLException {
        singleConnectionDataSource.connection = mockConnection;
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        singleConnectionDataSource.setUsername("admin");
        singleConnectionDataSource.setPassword("admin123");
        singleConnectionDataSource.connection = mockConnection;
        Connection connection = singleConnectionDataSource.getConnection("admin", "admin123");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        singleConnectionDataSource.setUsername("admin");
        singleConnectionDataSource.setPassword("admin123");
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.getConnection("admin", "wrongPassword");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionNotValid() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.getConnection();
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.connection = mockConnection;
        singleConnectionDataSource.resetConnection();
        verify(mockConnection, times(2)).close();
        assertNull(singleConnectionDataSource.connection);
        assertNull(singleConnectionDataSource.lock);
        verify(mockEventPublisher, times(1)).publishEvent(any());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWhenLockTimeout() {
        singleConnectionDataSource.setTimeOutMillis(1);
        singleConnectionDataSource.lock = mock(ReentrantLock.class);
        when(singleConnectionDataSource.lock.tryLock(anyLong(), any())).thenReturn(false);
        singleConnectionDataSource.getConnectionProxy(mockConnection);
    }
}
