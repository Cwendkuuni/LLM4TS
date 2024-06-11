import static org.junit.Assert.*;

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
    public void testGetConnectionWithCredentials() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "pass"));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection() {
        assertThrows(IllegalStateException.class, () -> dataSource.innerCreateConnection());
    }

    private static class TestConnectionInitializer implements ConnectionInitializer {
        @Override
        public void init(Connection connection) {
            // Do nothing for testing purposes
        }
    }

}