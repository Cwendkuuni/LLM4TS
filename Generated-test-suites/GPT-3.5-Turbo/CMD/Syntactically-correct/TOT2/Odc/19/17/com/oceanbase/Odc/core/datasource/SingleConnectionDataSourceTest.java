import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {
    
    @Test
    public void testSingleConnectionDataSourceConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        assertFalse(dataSource.getAutoReconnect());
        assertNull(dataSource.getAutoCommit());
        assertNull(dataSource.getEventPublisher());
        assertNull(dataSource.getConnection());
    }
    
    @Test
    public void testGetConnectionWhenNull() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Mockito.when(dataSource.innerCreateConnection()).thenReturn(Mockito.mock(Connection.class));
        
        assertNotNull(dataSource.getConnection());
    }
    
    @Test
    public void testGetConnectionProxy() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = Mockito.mock(Connection.class);
        Connection proxy = dataSource.getConnectionProxy(connection);
        
        assertNotNull(proxy);
    }
    
    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(Mockito.mock(EventPublisher.class));
        
        assertThrows(SQLException.class, () -> dataSource.resetConnection());
    }
    
    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }
    
    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = Mockito.mock(Connection.class);
        dataSource.setConnection(connection);
        
        dataSource.close();
        Mockito.verify(connection).close();
    }
    
    @Test
    public void testPrepareConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = Mockito.mock(Connection.class);
        
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        Mockito.verify(connection).setAutoCommit(true);
    }
    
    @Test
    public void testTryLock() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        
        assertThrows(SQLException.class, () -> dataSource.innerCreateConnection());
    }
}