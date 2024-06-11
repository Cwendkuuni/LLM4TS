import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    SingleConnectionDataSource dataSource;

    @Mock
    Connection mockConnection;

    @Mock
    EventPublisher mockEventPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource();

        dataSource.setEventPublisher(mockEventPublisher);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {}
        };
        // Attempt to add initializer
        dataSource.addInitializer(initializer);
        // Verifying that it is successful
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testGetConnectionWithNullConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        // Check if connection is not null
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithValidConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        when(mockConnection.isClosed()).thenReturn(true);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testCloseConnection() {
        dataSource.setConnection(mockConnection);
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.setLock(new ReentrantLock());

        dataSource.resetConnection();

        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithDifferentUsernamePassword() throws SQLException {
        dataSource.setUsername("admin");
        dataSource.setPassword("password");

        Connection connection = dataSource.getConnection("user", "pass");
    }

    @Test
    public void testGetConnectionWithSameUsernamePassword() throws SQLException {
        dataSource.setUsername("admin");
        dataSource.setPassword("password");

        Connection connection = dataSource.getConnection("admin", "password");
        assertNotNull(connection);
    }
}