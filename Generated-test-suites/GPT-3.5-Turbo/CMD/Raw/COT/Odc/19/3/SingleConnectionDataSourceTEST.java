
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
        dataSource = new SingleConnectionDataSource();
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
    
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPass");
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotEquals(connection, newConnection);
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
    
    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }
    
    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionWithExistingConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.innerCreateConnection();
    }
}
