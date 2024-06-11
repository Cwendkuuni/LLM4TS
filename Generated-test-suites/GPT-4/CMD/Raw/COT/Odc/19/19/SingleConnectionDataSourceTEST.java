
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testSingleConnectionDataSource() {
        assertNotNull(dataSource);
        assertFalse(dataSource.isAutoReconnect());
        assertNull(dataSource.getAutoCommit());
    }

    @Test
    public void testSingleConnectionDataSourceAutoReconnect() {
        SingleConnectionDataSource dataSourceAutoReconnect = new SingleConnectionDataSource(true);
        assertNotNull(dataSourceAutoReconnect);
        assertTrue(dataSourceAutoReconnect.isAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWhenConnectionNotInitialized() throws SQLException {
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test(expected = SQLException.class)
    public void testresetConnectionWhenConnectionNotInitialized() throws SQLException {
        dataSource.resetConnection();
    }

    @Test(expected = NullPointerException.class)
    public void testAddInitializerWithNull() {
        dataSource.addInitializer(null);
    }

    @Test
    public void testCloseWhenConnectionNotInitialized() {
        dataSource.close(); // No exception thrown
    }

    // Write similar test cases for all methods present in the class and their edge cases 
}

