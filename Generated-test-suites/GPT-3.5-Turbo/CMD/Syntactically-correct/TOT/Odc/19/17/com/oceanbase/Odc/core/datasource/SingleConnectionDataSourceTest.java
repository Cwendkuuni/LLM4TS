import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testGetConnection() {
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            Assert.fail("Failed to get connection");
        }
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        try {
            dataSource.getConnection("admin", "password123");
        } catch (SQLException e) {
            Assert.fail("Failed to get connection with valid credentials");
        }
    }

    @Test
    public void testResetConnection() {
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            Assert.fail("Failed to reset connection");
        }
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                // do nothing for testing purposes
            }
        };
        dataSource.addInitializer(initializer);
    }

    @Test
    public void testClose() {
        dataSource.close();
    }

    @Test
    public void testInnerCreateConnection() {
        try {
            dataSource.innerCreateConnection();
        } catch (SQLException e) {
            Assert.fail("Failed to create connection");
        }
    }

    // Add more test cases for other methods as needed

}