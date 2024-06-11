import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Test
    public void testSingleConnectionDataSourceDefaultConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testSingleConnectionDataSourceConstructorWithAutoReconnect() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertTrue(dataSource.isAutoReconnect());
    }

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalid() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        connection.close(); // Simulate closed connection
        dataSource.getConnection(); // Expect SQLException
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection("invalid_username", "invalid_password"); // Expect SQLException
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionNotNull() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setConnection(Mockito.mock(Connection.class)); // Simulate not null connection
        dataSource.innerCreateConnection(); // Expect IllegalStateException
    }

    @Test(expected = SQLException.class)
    public void testInitConnectionFailure() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(connection -> {
            throw new Exception("Initialization failed");
        });
        dataSource.initConnection(Mockito.mock(Connection.class)); // Expect SQLException
    }

    // Add more test cases for other methods

    @Test
    public void testSingleConnectionDataSourceClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close(); // Verify no exceptions
    }

}