
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource myDataSource;

    @Before
    public void setUp() {
        myDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnectionWithNullOrNotValid() throws SQLException {
        myDataSource.setEventPublisher(new SimpleEventPublisher());
        // This should throw an exception
        try {
            Connection conn = myDataSource.getConnection();
            fail("Should have thrown SQLException");
        } catch (SQLException e) {
           assertTrue(true);
        }
    }   

    @Test
    public void testGetConnectionWithNullPassword() throws SQLException {
        try {
        Connection conn = myDataSource.getConnection("username", null);
        } catch (SQLException e) {
           assertTrue(true);
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        myDataSource.resetConnection();
        // Assert the connection is reset
        assertNull(myDataSource.getConnection());
    }

    @Test
    public void testAddInitializerWithNonNullParameter() {
        ConnectionInitializer initializer = new SimpleConnectionInitializer();
        myDataSource.addInitializer(initializer);
        // Assert the initializer is added
        assertEquals(1, myDataSource.getInitializerList().size());
        assertTrue(myDataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testSetAutoCommitWithNonNullParameter() {
        myDataSource.setAutoCommit(true);
        // Assert the auto commit is true
        assertTrue(myDataSource.getAutoCommit());
    }

    @Test
    public void testSetTimeoutMillisWithDifferentValues() {
        long timeout = 5000L;
        myDataSource.setTimeoutMillis(timeout);
        // Assert the timeout is 5000
        assertEquals(timeout, myDataSource.getTimeoutMillis());
    }
}
