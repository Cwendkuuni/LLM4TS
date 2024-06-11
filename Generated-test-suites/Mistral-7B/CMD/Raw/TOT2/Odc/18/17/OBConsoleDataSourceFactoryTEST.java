import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DriverManagerDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private Connection connection;

    @Before
    public void setup() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class.forName("com.oceanbase.jdbc.OceanBaseDriver");
        Properties properties = new Properties();
        properties.setProperty("username", "testUser");
        properties.setProperty("password", "testPassword");
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "3306");
        properties.setProperty("defaultSchema", "test_schema");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setDriverClassName("com.oceanbase.jdbc.OceanBaseDriver");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.oceanbase.jdbc.OceanBaseDriver");
        dataSource.setUrl("jdbc:ob-mysql://localhost:3306/test_schema");
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        PoolingDataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals(dataSourceFactory.getJdbcUrl(), dataSource.getConnectionProperties().get("url"));
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("com.oceanbase.jdbc.OceanBaseDriver", dataSource.getDriverClassName());
        Connection testConnection = dataSource.getConnection();
        assertEquals(connection.getAutoCommit(), testConnection.getAutoCommit());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:ob-mysql://localhost:3306/test_schema", jdbcUrl);
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

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, false, true);
        assertThrows(IllegalArgumentException.class, () -> dataSourceFactory.getDataSource());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithInvalidDialectType() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("username", "testUser");
        properties.setProperty("password", "testPassword");
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "3306");
        properties.setProperty("defaultSchema", "test_schema");
        properties.setProperty("dialectType", "INVALID_DIALECT_TYPE");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setProperties(properties);
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        assertThrows(IllegalStateException.class, () -> dataSourceFactory.getDataSource());
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidDriver() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("username", "testUser");
        properties.setProperty("password", "testPassword");
        properties.setProperty("host", "localhost");
        properties.setProperty("port", "3306");
        properties.setProperty("defaultSchema", "test_schema");
        properties.setProperty("dialectType", DialectType.OB_MYSQL.name());
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setProperties(properties);
        connectionConfig.setDriverClassName("com.oceanbase.jdbc.INVALID_DRIVER_CLASS");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        assertThrows(SQLException.class, () -> dataSourceFactory.getDataSource());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newTestUser";
        dataSourceFactory.resetUsername(Mockito.mock(ClonableDataSourceFactory.ValueMapper.class));
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newTestPassword";
        dataSourceFactory.resetPassword(Mockito.mock(ClonableDataSourceFactory.ValueMapper.class));
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

}