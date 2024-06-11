import static org.junit.Assert.*;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_pass");
        Connection conn = dataSource.getConnection();
        
        assertNotNull(conn);
        assertTrue(conn instanceof Connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection("wrong_user", "wrong_pass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection conn = dataSource.getConnection();

        assertNotNull(conn);
        assertTrue(conn instanceof Connection);
        
        dataSource.resetConnection();
        
        Connection newConn = dataSource.getConnection();
        
        assertNotNull(newConn);
        assertFalse(conn == newConn);
    }
    //... Continued for remaining methods ...
}