import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithInvalidUsernamePassword() {
        Exception exception = assertThrows(SQLException.class, () -> {
            dataSource.getConnection("invalidUser", "invalidPassword");
        });

        String expectedMessage = "Invalid username or password";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
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
        ConnectionInitializer mockInitializer = connection -> {
            // Mock implementation
        };

        dataSource.addInitializer(mockInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testGetConnectionProxy() {
        Lock mockLock = new ReentrantLock();
        Connection proxy = dataSource.getConnectionProxy(mockConnection, mockLock);

        assertNotNull(proxy);
    }
    
    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

}