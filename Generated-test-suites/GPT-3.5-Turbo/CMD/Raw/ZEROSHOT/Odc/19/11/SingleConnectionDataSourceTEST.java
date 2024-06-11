import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_password");
        dataSource.setAutoCommit(true);
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithCredentials() throws SQLException {
        Connection connection = dataSource.getConnection("test_user", "test_password");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.getConnection("invalid_user", "invalid_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();
        assertNotEquals(connection1, connection2); // Connection should be reset
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
        assertEquals(initializer, dataSource.getInitializerList().get(0));
    }

    private static class TestConnectionInitializer implements ConnectionInitializer {

        @Override
        public void init(Connection connection) throws SQLException {
            // Do nothing for testing
        }
    }

}