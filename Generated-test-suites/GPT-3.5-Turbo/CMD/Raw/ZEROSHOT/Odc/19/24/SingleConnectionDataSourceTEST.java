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
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPass");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPass");
        
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        
        dataSource.close();
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
        
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        
        dataSource.prepareConnection(connection);
        
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        
        assertNotNull(connection);
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        
        dataSource.onConnectionReset(mock(Connection.class));
        
        verify(eventPublisher, times(1)).publishEvent(any());
    }
    
}