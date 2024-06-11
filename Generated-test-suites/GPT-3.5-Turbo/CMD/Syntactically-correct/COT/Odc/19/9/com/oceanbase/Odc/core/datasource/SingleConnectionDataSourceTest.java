
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
        dataSource.getConnection("invalid", "credentials");
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
    
    @Test
    public void testClose() {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }
    
    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(anyBoolean());
    }
    
    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }
}
