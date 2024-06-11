import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer mockInitializer;
    private EventPublisher mockEventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        mockInitializer = mock(ConnectionInitializer.class);
        mockEventPublisher = mock(EventPublisher.class);
        dataSource.addInitializer(mockInitializer);
        dataSource.setEventPublisher(mockEventPublisher);
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(mockConnection);

        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
        verify(dataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosed() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(dataSource.innerCreateConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenReturn(true);

        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
        verify(dataSource, times(1)).resetConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUsernameAndPasswordMismatch() throws Exception {
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpass");

        dataSource.getConnection("user", "pass");
    }

    @Test(expected = RuntimeException.class)
    public void testTryLockTimeout() {
        dataSource.setLock(new ReentrantLock());
        dataSource.setTimeOutMillis(100);

        dataSource.tryLock();
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        dataSource.prepareConnection(mockConnection);

        verify(mockConnection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(mockInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(mockInitializer));
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        dataSource.setConnection(mockConnection);

        dataSource.close();

        verify(mockConnection, times(1)).close();
    }

    // Add more test cases for edge cases and error scenarios as needed

}