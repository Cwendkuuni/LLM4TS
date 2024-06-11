import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource singleConnectionDataSource;
    
    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }
    
    @Test
    public void testGetConnectionReturnsConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testConnectionIsAutoCommit() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        Connection connection = singleConnectionDataSource.getConnection();
        assertTrue(connection.getAutoCommit());
    }
    
    @Test
    public void testResetConnectionResetsAndReinitializesConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }
    
    @Test(expected = SQLException.class)
    public void testInvalidUsernameOrPasswordThrowsException() throws SQLException {
        singleConnectionDataSource.getConnection("username", "password");
    }
    
    @Test
    public void testCloseConnectionClosesConnection() {
        singleConnectionDataSource.close();
        Field connectionField = SingleConnectionDataSource.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        assertNull(connectionField.get(singleConnectionDataSource));
    }
}