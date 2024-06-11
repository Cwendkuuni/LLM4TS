import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() {
        try {
            assertNotNull(singleConnectionDataSource.getConnection());
        } catch (SQLException e) {
            fail("Failed to get connection");
        }
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        try {
            singleConnectionDataSource.getConnection("invaliduser", "invalidpassword");
            fail("Invalid username or password exception not thrown");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() {
        try {
            singleConnectionDataSource.resetConnection();
            assertNotNull(singleConnectionDataSource.getConnection());
        } catch (SQLException e) {
            fail("Failed to reset connection");
        }
    }

    // Add more test cases for other methods...

}