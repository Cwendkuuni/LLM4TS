
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection_Success() {
        // Set up connection
        Connection conn = dataSource.getConnection();

        // Assert that the connection is not null
        Assert.assertNotNull(conn);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_Invalid() throws SQLException{
        // Invalidate connection
        dataSource.close();

        // Assert that exception is thrown if connection is invalid
        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_WrongUsernamePassword() throws SQLException{
        // Pass wrong username and password
        dataSource.getConnection("wrongUsername", "wrongPassword");
    }

    @Test
    public void testResetConnection_Success() throws SQLException {
        // Reset the connection
        dataSource.resetConnection();

        // Get connection again
        Connection conn = dataSource.getConnection();
        
        // Assert the connection is not null
        Assert.assertNotNull(conn);
    }

    @Test
    public void testAddInitializer() {
        // Create connection initializer
        ConnectionInitializer initializer = new SimpleConnectionInitializer();

        // Add initializer and check if it's in the list
        dataSource.addInitializer(initializer);

        List<ConnectionInitializer> list = new LinkedList<>();
        list.add(initializer);

        Assert.assertEquals(list, dataSource.initializerList());
    }

    @Test(expected = SQLException.class)
    public void testClose_ConnectionClosed() throws SQLException {
        dataSource.close();
        dataSource.getConnection();
    }

    @Test
    public void testAutoCloseable_Success() throws Exception {
        try (SingleConnectionDataSource dataSource = new SingleConnectionDataSource()) {
            Connection conn = dataSource.getConnection();
            Assert.assertNotNull(conn);
        }
    }

    @After
    public void tearDown() {
        dataSource = null;
    }
}
