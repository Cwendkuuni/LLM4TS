import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        // Typical use case
        assertNotNull(singleConnectionDataSource.getConnection());

        // Edge case
        singleConnectionDataSource.setAutoReconnect(true);
        assertNotNull(singleConnectionDataSource.getConnection());

        // Error scenario
        singleConnectionDataSource.setConnection(null);
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Typical use case
        assertNotNull(singleConnectionDataSource.getConnection("username", "password"));

        // Edge case
        assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalid", "invalid"));
    }

    @Test
    public void testResetConnection() {
        // Typical use case
        assertDoesNotThrow(() -> singleConnectionDataSource.resetConnection());

        // Edge case
        singleConnectionDataSource.setConnection(null);
        assertDoesNotThrow(() -> singleConnectionDataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        // Typical use case
        singleConnectionDataSource.addInitializer(connection -> {
            // do nothing
        });
    }

    @Test
    public void testClose() {
        // Typical use case
        singleConnectionDataSource.close();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() {
        // Typical use case
        assertNotNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testTryLock() {
        // Typical use case
        assertTrue(singleConnectionDataSource.tryLock());
    }

}