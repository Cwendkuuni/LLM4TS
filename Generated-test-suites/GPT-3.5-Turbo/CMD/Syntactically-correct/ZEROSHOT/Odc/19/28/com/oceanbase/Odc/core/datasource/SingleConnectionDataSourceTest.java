import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("username", "password"));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        assertNotNull(dataSource.getInitializerList());
        assertEquals(0, dataSource.getInitializerList().size());
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // Mock initialization logic
            }
        });
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        Lock lock = dataSource.getLock();
        assertTrue(dataSource.tryLock());
        lock.lock();
        assertFalse(dataSource.tryLock());
        lock.unlock();
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = dataSource.getConnection();
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInitConnection() {
        Connection connection = dataSource.getConnection();
        assertDoesNotThrow(() -> dataSource.initConnection(connection));
    }

    @Test
    public void testOnConnectionCreated() {
        Connection connection = dataSource.getConnection();
        dataSource.onConnectionCreated(connection);
        // Assertion based on logging output
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = dataSource.getConnection();
        dataSource.setOnConnectionReset(new EventPublisher() {
            @Override
            public void publishEvent(Object event) {
                // Mock publish event logic
            }
        });
        dataSource.onConnectionReset(connection);
        // Assertion based on logging output
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        assertDoesNotThrow(() -> dataSource.getConnection());
    }

}