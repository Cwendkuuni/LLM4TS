import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "testUser";
        String password = "testPassword";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(connection -> {});
        assertEquals(1, dataSource.getInitializerList().size());
    }
}