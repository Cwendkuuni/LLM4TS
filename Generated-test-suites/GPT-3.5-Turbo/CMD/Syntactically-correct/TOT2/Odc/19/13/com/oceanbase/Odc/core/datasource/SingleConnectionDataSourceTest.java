import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
    }
    
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        
        assertNotNull(connection);
    }
    
    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("username", "password");
        
        assertNotNull(connection);
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        
        assertNull(dataSource.getConnection());
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        
        dataSource.addInitializer(initializer);
        
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
    
    @Test
    public void testClose() {
        dataSource.close();
        
        // Test passes if no exception is thrown
        assertTrue(true);
    }
    
    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.getInnerCreateConnection();
        
        assertNotNull(connection);
    }
    
}