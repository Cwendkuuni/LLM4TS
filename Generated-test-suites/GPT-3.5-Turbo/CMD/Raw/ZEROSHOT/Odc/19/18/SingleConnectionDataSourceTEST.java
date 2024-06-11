import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    
    private SingleConnectionDataSource dataSource;
    
    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        dataSource.setAutoCommit(true);
        dataSource.setEventPublisher(mock(EventPublisher.class));
    }
    
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
    
    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
    
    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
    
    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }
    
    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = mock(Connection.class);
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }
    
    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer).init(connection);
    }
    
    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
    }
    
    @Test
    public void testOnConnectionReset() {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(dataSource.getEventPublisher()).publishEvent(any());
    }
    
    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

}