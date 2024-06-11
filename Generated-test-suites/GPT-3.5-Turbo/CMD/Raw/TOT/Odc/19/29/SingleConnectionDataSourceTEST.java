import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            fail("Failed to get connection");
        }
    }

    @Test
    public void testGetConnectionWithCredentials() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        try {
            dataSource.getConnection("username", "password");
        } catch (SQLException e) {
            fail("Failed to get connection with credentials");
        }
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            fail("Failed to reset connection");
        }
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        ConnectionInitializer mockInitializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
        dataSource.close();
    }

}