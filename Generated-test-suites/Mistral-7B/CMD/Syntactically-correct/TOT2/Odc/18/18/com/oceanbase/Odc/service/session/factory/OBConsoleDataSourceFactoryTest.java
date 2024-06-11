import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private BasicDataSource dataSource;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setSslConfig(new SSLConfig(false, null));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = (BasicDataSource) dataSourceFactory.getDataSource();
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetDataSource() {
        Connection connection = dataSource.getConnection();
        assertEquals("testUser", connection.getMetaData().getUserName());
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=false&trustServerCertificate=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertEquals("testSchema", defaultSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
        fail("Expecting an IllegalArgumentException when passed a null ConnectionConfig");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(null);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        fail("Expecting an IllegalArgumentException when passed a null host");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(-1);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        fail("Expecting an IllegalArgumentException when passed a negative port");
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.INVALID);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        fail("Expecting an IllegalStateException when passed an invalid dialectType");
    }

    @Test
    public void testEmptyParameterMap() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setParameters(new HashMap<>());
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(dataSourceFactory.getParameters());
    }

    @Test
    public void testNonEmptyParameterMap() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("test.key", "test.value");
        connectionConfig.setParameters(parameters);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(parameters, dataSourceFactory.getParameters());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParameterMapValueMapper() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, null);
        fail("Expecting an IllegalArgumentException when passed a null ValueMapper");
    }

    @Test
    public void testResetUsername() {
        String oldUsername = dataSourceFactory.getUsername();
        String newUsername = "newTestUser";
        dataSourceFactory.resetUsername(c -> c.replace("testUser", newUsername));
        assertEquals(newUsername, dataSourceFactory.getUsername());
        assertEquals(oldUsername, oldUsername);
    }

    @Test
    public void testResetPassword() {
        String oldPassword = dataSourceFactory.getPassword();
        String newPassword = "newTestPassword";
        dataSourceFactory.resetPassword(c -> c.replace("testPassword", newPassword));
        assertEquals(newPassword, dataSourceFactory.getPassword());
        assertEquals(oldPassword, oldPassword);
    }

    @Test
    public void testResetHost() {
        String oldHost = dataSourceFactory.getHost();
        String newHost = "newTestHost";
        dataSourceFactory.resetHost(c -> c.replace("localhost", newHost));
        assertEquals(newHost, dataSourceFactory.getHost());
        assertEquals(oldHost, oldHost);
    }

    @Test
    public void testResetPort() {
        int oldPort = dataSourceFactory.getPort();
        int newPort = 3307;
        dataSourceFactory.resetPort(i -> i + 1);
        assertEquals(newPort, dataSourceFactory.getPort());
        assertEquals(oldPort, oldPort);
    }

    @Test
    public void testResetSchema() {
        String oldSchema = dataSourceFactory.getDefaultSchema();
        String newSchema = "newTestSchema";
        dataSourceFactory.resetSchema(c -> c.replace("testSchema", newSchema));
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
        assertEquals(oldSchema, oldSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueMapper() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), null);
        dataSourceFactory.resetUsername(null);
        fail("Expecting an IllegalArgumentException when passed a null ValueMapper");
    }
}