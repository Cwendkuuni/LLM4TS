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
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }
    
    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testClose() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        
        dataSource.close();
        
        verify(connection, times(1)).close();
    }
    
    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        
        assertNotNull(dataSource.getInitializerList());
        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
    
    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        
        dataSource.prepareConnection(connection);
        
        verify(connection, times(1)).setAutoCommit(true);
    }
    
    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.innerCreateConnection();
        
        assertNotNull(dataSource.getConnection());
    }
}