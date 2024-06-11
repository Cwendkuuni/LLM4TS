import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = singleConnectionDataSource.getConnection();
        assertNotNull(conn);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "admin";
        String password = "admin123";
        Connection conn = singleConnectionDataSource.getConnection(username, password);
        assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        String username = "test";
        String password = "test123";
        singleConnectionDataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection conn = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        assertTrue(conn.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertEquals(1, singleConnectionDataSource.getInitializerList().size());
    }

    // Add more test cases for other methods if needed

}