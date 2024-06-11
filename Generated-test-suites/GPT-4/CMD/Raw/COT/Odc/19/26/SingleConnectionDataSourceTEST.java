
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testAutoCommitGetterAndSetter() {
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getAutoCommit());
    }

    @Test
    public void testAutoReconnect() {
        assertFalse(dataSource.isAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void testGetConnection() throws SQLException {
        assertNull(dataSource.getConnection());
        fail("An SQLException was expected as connection was not initialized");
        // Note: This test assumes there is no pre-configured connection
        // In a real-world scenario a mocked connection would probably be used.
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        dataSource.getConnection("username", "password");
        fail("An SQLException was expected as connection was not initialized and usernames/passwords do not match");
        // As above, this test assumes there is no pre-configured connection.
    }

    @Test(expected = SQLException.class)
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        fail("An SQLException was expected as connection was not initialized");
        // As above, this test assumes there is no pre-configured connection.
    }

    @Test
    public void testAddInitializer() {
        assertEquals(0, dataSource.getInitializerList().size());
        ConnectionInitializer initializer = new ConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        dataSource.close();
        assertNull(dataSource.getConnection());
    }
}
