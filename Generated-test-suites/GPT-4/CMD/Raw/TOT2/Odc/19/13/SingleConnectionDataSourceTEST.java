import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Test
    public void getConnectionTest() throws SQLException {
        final SingleConnectionDataSource ds = new SingleConnectionDataSource();

        //Test valid connection
        Connection con = ds.getConnection();
        assertNotNull("Connection not established", con);

        //Test exception behavior (SQLException) when invalid
        ds.connection = null;
        try {
            con = ds.getConnection();
            fail("Expected SQLException for invalid connection");
        } 
        catch (SQLException e) {
            assertEquals(e.getMessage(), "Connection was closed or not valid");
        }

        //Below tests would follow a similar structure
    }

    @Test
    public void resetConnectionTest() throws SQLException {
        final SingleConnectionDataSource ds = new SingleConnectionDataSource();

        //Initial setup for connection required before resetting
        ds.getConnection();

        //Test reset Connection behavior
        ds.resetConnection();
        assertNull("Connection not reset", ds.connection);
    }

    @Test
    public void closeTest() throws SQLException {
        final SingleConnectionDataSource ds = new SingleConnectionDataSource();

        //Initial setup for connection required before closing
        ds.getConnection();

        //Test close behavior
        ds.close();
        assertTrue("Connection not closed", ds.connection.isClosed());
    }

    //... More tests for other methods
}