import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setAutoCommit(true);
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Typical use case
        assertNotNull(dataSource.getConnection());

        // Edge case: Connection is closed
        dataSource.getConnection().close();
        assertTrue(dataSource.getConnection().isClosed());

        // Error scenario: Connection is not valid
        dataSource.resetConnection();
        assertEquals(dataSource.getConnection(), dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Typical use case
        assertThrows(SQLException.class, () -> dataSource.getConnection("username", "password"));

        // Error scenario: Invalid username or password
        assertThrows(SQLException.class, () -> dataSource.getConnection("admin", "admin"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        // Typical use case
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());

        // Error scenario: Connection is not reset
        dataSource.getConnection().close();
        assertThrows(SQLException.class, () -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        // Typical use case
        ConnectionInitializer initializer = connection -> {/* some initialization logic */};
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());

        // Error scenario: Adding null initializer
        assertThrows(NullPointerException.class, () -> dataSource.addInitializer(null));
    }

    @Test
    public void testClose() throws SQLException {
        // Typical use case
        dataSource.getConnection().close();
        dataSource.close();

        // Edge case: Connection is already closed
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        // Typical use case
        dataSource.getConnection().setAutoCommit(false);
        dataSource.prepareConnection(dataSource.getConnection());
        assertTrue(dataSource.getConnection().getAutoCommit());
    }

}