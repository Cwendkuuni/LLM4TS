
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:"));
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = factory.getUsername();
        assertEquals("user", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("pass123");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = factory.getPassword();
        assertEquals("pass123", password);
    }

    // Add more test cases for other public methods...

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        // Add more assertions to check DataSource properties
    }

}
