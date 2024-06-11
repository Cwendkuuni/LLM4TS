import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

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
    }

    @After
    public void tearDown() {
        dataSource.close();
    }

    @Test
    public void testConnect() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertEquals(false, connection.isClosed());
        connection.close();
    }

    @Test(expected = SQLException.class)
    public void testInvalidConnection() throws SQLException {
        dataSource.getConnection("Invalid", "credentials");
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());
        connection.close();
    }

    @Test
    public void testNoAutoCommit() throws SQLException {
        dataSource.setAutoCommit(false);
        Connection connection = dataSource.getConnection();
        assertEquals(false, connection.getAutoCommit());
        connection.close();
    }

    @Test
    public void testAutoReconnect() throws SQLException {
        dataSource = new SingleConnectionDataSource(true);
        Connection connection = dataSource.getConnection();
        connection.close();
        connection = dataSource.getConnection();
        assertEquals(false, connection.isClosed());
        connection.close();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection1 = dataSource.getConnection();
        dataSource.resetConnection();
        Connection connection2 = dataSource.getConnection();
        assertTrue(!connection1.equals(connection2));
        connection2.close();
    }
}