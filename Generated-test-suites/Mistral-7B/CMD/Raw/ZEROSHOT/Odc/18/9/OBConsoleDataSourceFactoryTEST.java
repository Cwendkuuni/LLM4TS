import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private Connection connection;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        this.connection = connection;
        assertTrue(connection.isClosed());

        connection.createStatement().execute("CREATE TABLE testTable (id INT PRIMARY KEY, name VARCHAR(50))");
        assertFalse(connection.isClosed());

        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=false";
        assertEquals(expectedUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("root", username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("password", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testCloneableDataSourceFactory() throws SQLException, CloneNotSupportedException {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(dataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());

        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
        assertNotNull(clonedDataSource);
        Connection clonedConnection = clonedDataSource.getConnection();
        assertFalse(clonedConnection.isClosed());
        clonedConnection.close();
    }

    @Test
    public void testResetValues() throws SQLException {
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String newHost = "newHost";
        int newPort = 3307;
        String newSchema = "newSchema";
        Map<String, String> newParameters = new HashMap<>();
        dataSourceFactory.resetUsername((valueMapper) -> newUsername);
        dataSourceFactory.resetPassword((valueMapper) -> newPassword);
        dataSourceFactory.resetHost((valueMapper) -> newHost);
        dataSourceFactory.resetPort((valueMapper) -> newPort);
        dataSourceFactory.resetSchema((valueMapper) -> newSchema);
        dataSourceFactory.resetParameters((valueMapper) -> newParameters);

        String newJdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedNewJdbcUrl = "jdbc:mysql://newHost:3307/newSchema?useSSL=false";
        assertEquals(expectedNewJdbcUrl, newJdbcUrl);

        assertEquals(newUsername, dataSourceFactory.getUsername());
        assertEquals(newPassword, dataSourceFactory.getPassword());
        assertEquals(newHost, dataSourceFactory.getHost());
        assertEquals(newPort, dataSourceFactory.getPort());
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertEquals("testSchema", defaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = dataSourceFactory.getParameters();
        assertEquals(11, parameters.size());
        assertTrue(parameters.containsKey("maxAllowedPacket"));
        assertTrue(parameters.containsKey("allowMultiQueries"));
        assertTrue(parameters.containsKey("connectTimeout"));
        assertTrue(parameters.containsKey("zeroDateTimeBehavior"));
        assertTrue(parameters.containsKey("noDatetimeStringSync"));
        assertTrue(parameters.containsKey("jdbcCompliantTruncation"));
        assertTrue(parameters.containsKey("sendConnectionAttributes"));
        assertTrue(parameters.containsKey("disableSslHostnameVerification"));
        assertTrue(parameters.containsKey("trustServerCertificate"));
        assertTrue(parameters.containsKey("keyStore"));
        assertTrue(parameters.containsKey("keyStorePassword"));
        assertNull(parameters.get("allowLoadLocalInfile"));
    }
}