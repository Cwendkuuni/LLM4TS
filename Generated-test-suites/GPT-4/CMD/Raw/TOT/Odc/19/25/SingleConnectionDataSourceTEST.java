import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource scds;
    private String username = "testUser";
    private String password = "testPass";

    @Before
    public void setUp() throws Exception {
        scds = new SingleConnectionDataSource();
        scds.setUsername(username);
        scds.setPassword(password);
    }

    @After
    public void tearDown() throws Exception {
        scds.close();
    }

    @Test
    public void testGetConnection_NoConnection() {
        Connection conn = scds.getConnection();
        assertNotNull("Connection should not be null", conn);
    }

    /**
     * Test when a closed connection is requested
     */
    @Test(expected = SQLException.class)
    public void testGetConnection_ClosedConnection() throws Exception {
        Connection conn = scds.getConnection();
        conn.close();
        scds.getConnection();
    }

    @Test
    public void testGetConnection_WithUsernameAndPassword() throws Exception {
        Connection conn = scds.getConnection(username, password);
        assertNotNull("Connection should not be null", conn);
    }

    @Test(expected = SQLException.class) 
    public void testGetConnection_InvalidUsernameAndPassword() throws Exception {
        String wrongPassword = "wrongPassword";
        scds.getConnection(username, wrongPassword);
    }

    /**
     * Test the resetConnection method
     */
    @Test
    public void testResetConnection() throws Exception {
        scds.getConnection();
        scds.resetConnection();       
        Connection conn = scds.getConnection();
        assertNotNull("Connection should not be null", conn);
    }
}