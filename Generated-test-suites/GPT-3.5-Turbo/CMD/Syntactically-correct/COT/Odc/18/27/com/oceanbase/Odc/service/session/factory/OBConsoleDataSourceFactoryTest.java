
import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testConstructorWithNonNullConnectionConfigAndAutoCommit() {
        // Testing the constructor with non-null ConnectionConfig and autoCommit value
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory);
    }

    @Test
    public void testGetJdbcUrl() {
        // Testing the getJdbcUrl method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        // Testing the getDataSource method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    // Add more test cases for other public methods

    @Test
    public void testResetUsername() {
        // Testing the resetUsername method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(username -> "newUsername");
        assertEquals(factory.getUsername(), "newUsername");
    }

    // Add test cases for other reset methods

}