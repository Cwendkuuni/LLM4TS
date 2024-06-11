import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        // setup other required components, like setting username, password and connection initializer
    }

    @Test
    public void testGetConnection() {
        try {
            Connection conn = dataSource.getConnection();
            assertNotNull("Connection should not be null", conn);
            assertFalse("Connection should not be closed", conn.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException should not be thrown");
        }
    }

    @Test
    public void testGetConnectionInvalidLogin() {
        try {
            Connection conn = dataSource.getConnection("wrongUsername", "wrongPassword");
            fail("SQLException should have been thrown");
        } catch (SQLException e) {
            // SQLException expected
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.resetConnection();
            assertNull("Connection should be null after reset", dataSource.connection);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException should not be thrown");
        }
    }

    @Test
    public void testAddInitializer() {
        // assuming that DefaultConnectionInitializer is a valid class
        ConnectionInitializer initializer = new DefaultConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertTrue("Initializer list should contain the added initializer", 
                   dataSource.initializerList.contains(initializer));
    }

    @Test
    public void testClose() {
        try {
            dataSource.close();
            assertTrue("Connection should be closed after close operation", dataSource.connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQLException should not be thrown");
        }
    }

}