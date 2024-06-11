
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("jdbc:mysql"));
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = factory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("testPass");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = factory.getPassword();
        assertEquals("testPass", password);
    }

    @Test
    public void testGetConnectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.READ_ONLY);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        ConnectType connectType = factory.getConnectType();
        assertEquals(ConnectType.READ_ONLY, connectType);
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    // Add more test cases for the remaining methods
}

