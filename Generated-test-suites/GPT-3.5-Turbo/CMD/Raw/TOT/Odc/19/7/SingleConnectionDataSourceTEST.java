import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPass");
    }

    @Test
    public void testGetConnection() throws SQLException {
        // Typical use case
        assertNotNull(dataSource.getConnection());

        // Edge case
        dataSource.setConnection(null);
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Typical use case
        assertNotNull(dataSource.getConnection("testUser", "testPass"));

        // Error scenario
        assertThrows(SQLException.class, () -> dataSource.getConnection("testUser", "wrongPass"));
    }

    @Test
    public void testResetConnection() {
        // Typical use case
        dataSource.setConnection(dataSource.getConnection());
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        // Typical use case
        dataSource.addInitializer(connection -> System.out.println("Initializing connection"));

        // Edge case
        assertDoesNotThrow(() -> dataSource.addInitializer(null));
    }

    @Test
    public void testClose() {
        // Typical use case
        dataSource.setConnection(dataSource.getConnection());
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testInitConnection() {
        // Typical use case
        assertDoesNotThrow(() -> dataSource.initConnection(dataSource.getConnection()));

        // Error scenario
        dataSource.addInitializer(connection -> {throw new Exception();});
        assertThrows(SQLException.class, () -> dataSource.initConnection(dataSource.getConnection()));
    }

    @Test
    public void testOnConnectionReset() {
        // Typical use case
        dataSource.resetConnection();

        // Error scenario
        dataSource.setEventPublisher(null);
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testInnerCreateConnection() {
        // Typical use case
        assertNotNull(dataSource.innerCreateConnection());

        // Error scenario
        dataSource.setUsername(null);
        assertThrows(SQLException.class, () -> dataSource.innerCreateConnection());
    }
}