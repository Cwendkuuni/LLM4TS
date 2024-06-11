import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {
    
    @Test
    public void testGetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to get connection");
        }
    }
    
    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.resetConnection();
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Failed to reset connection");
        }
    }
    
    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // do initialization
            }
        };
        dataSource.addInitializer(initializer);
        
        assertEquals(1, dataSource.getInitializerList().size());
    }
    
    // More test cases can be added for other public methods
    
}