import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        // Test getConnection method when connection is null
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getConnection());
        
        // Test getConnection method when connection is closed
        dataSource.connection = null;
        dataSource.autoReconnect = true;
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Test getConnection method with username and password
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertEquals(dataSource.getConnection("username", "password"), dataSource.getConnection());

        // Test Invalid username or password scenario
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "invalid"));
    }

    @Test
    public void testResetConnection() {
        // Test resetConnection method
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        // Test addInitializer method
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(connection -> {
            // Custom initialization logic
        });

        assertFalse(dataSource.initializerList.isEmpty());
    }

    // Add more test cases for other methods in SingleConnectionDataSource class

}