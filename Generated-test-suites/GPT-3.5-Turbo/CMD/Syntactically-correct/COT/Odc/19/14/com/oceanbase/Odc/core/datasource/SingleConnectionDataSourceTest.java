
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource singleConnectionDataSource;
    
    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }
    
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection("username", "password");
        assertNotNull(connection);
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertTrue(singleConnectionDataSource.getInitializerList().contains(initializer));
    }
    
    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.setConnection(connection);
        singleConnectionDataSource.close();
        verify(connection, times(1)).close();
    }
    
    // Add more test cases for other methods
    
}
