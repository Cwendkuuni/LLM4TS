import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.TestUtil;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceUtils;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import lombok.SneakyThrows;
import lombok.Setter;

/**
 * Test class for {@link OBConsoleDataSourceFactory}
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory dataSourceFactory;
    private CloneableDataSourceFactory cloneableDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.ORACLE);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionInitializer connectionInitializerMock = Mockito.mock(ConnectionInitializer.class);
        Mockito.when(connectionInitializerMock.initializeDatabase(Mockito.any(Connection.class)))
                .thenReturn(true);
        SessionCreatedInitializer sessionCreatedInitializerMock = Mockito.mock(SessionCreatedInitializer.class);
        Mockito.when(sessionCreatedInitializerMock.initializeDatabase(Mockito.any(Connection.class)))
                .thenReturn(true);
        ConnectionExtensionPoint connectionExtensionPointMock = Mockito.mock(ConnectionExtensionPoint.class);
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);
        ConnectionPluginUtil connectionPluginUtil = Mockito.mock(ConnectionPluginUtil.class);
        Mockito.when(connectionPluginUtil.getConnectionExtension(Mockito.any(DialectType.class)))
                .thenReturn(connectionExtensionPointMock);
        Mockito.when(connectionExtensionPointMock.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
                        Mockito.anyString(), Mockito.anyMap()))
                .thenReturn("jdbc:test:url");
        Mockito.when(connectionExtensionPointMock.getDriverClassName())
                .thenReturn("org.test.jdbc.driver.OracleDriver");
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        this.cloneableDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @After
    public void tearDown() throws Exception {
        TestUtil.cleanTempFiles();
        TestUtil.shutdownJDBC();
    }

    /**
     * Test constructor with null connectionConfig
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOBConsoleDataSourceFactoryWithNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, false);
    }

    /**
     * Test getDataSource method with default parameters
     */
    @Test
    public void testGetDataSourceWithDefaultParams() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(jdbcUrl, dataSource.getURL());
        assertEquals(connectionConfig.getUsername(), connection.createStatement().executeQuery("SELECT USER FROM DUAL")
                .getString(1));
        connection.close();
    }

    /**
     * Test getDataSource method with custom parameters
     */
    @Test
    public void testGetDataSourceWithCustomParams() throws SQLException {
        ConnectionConfig connectionConfigClone = ConnectionMapper.INSTANCE.clone(connectionConfig);
        connectionConfigClone.setUsername("testUserClone");
        connectionConfigClone.setPassword("testPasswordClone");
        cloneableDataSourceFactory.resetUsername((value) -> "testUserClone");
        cloneableDataSourceFactory.resetPassword((value) -> "testPasswordClone");
        DataSource dataSource = cloneableDataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        String jdbcUrl = cloneableDataSourceFactory.getJdbcUrl();
        assertEquals(jdbcUrl, dataSource.getURL());
        assertEquals("testUserClone", connection.createStatement().executeQuery("SELECT USER FROM DUAL")
                .getString(1));
        connection.close();
    }

    /**
     * Test getDataSource method with SSL configuration
     */
    @Test
    @SneakyThrows
    public void testGetDataSourceWithSslConfig() throws SQLException, InterruptedException {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("useSSL", "true");
        connectionProperties.setProperty("trustServerCertificate", "true");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.test.jdbc.driver.OracleDriver");
        basicDataSource.setURL("jdbc:test:url");
        basicDataSource.setUsername(connectionConfig.getUsername());
        basicDataSource.setPassword(connectionConfig.getPassword());
        basicDataSource.setConnectionProperties(connectionProperties);
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertTrue(dataSource.getConnection().isClosed());
        DataSourceUtils.close(basicDataSource.getConnection());
        DataSourceUtils.close(dataSource);
    }

    /**
     * Test getDataSource method with invalid port number
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidPort() {
        ConnectionConfig connectionConfigNegativePort = new ConnectionConfig();
        connectionConfigNegativePort.setHost("localhost");
        connectionConfigNegativePort.setPort(-1);
        new OBConsoleDataSourceFactory(connectionConfigNegativePort, false);
    }

    /**
     * Test getDataSource method with null defaultSchema
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDefaultSchema() {
        ConnectionConfig connectionConfigNullDefaultSchema = new ConnectionConfig();
        connectionConfigNullDefaultSchema.setHost("localhost");
        connectionConfigNullDefaultSchema.setPort(1521);
        connectionConfigNullDefaultSchema.setUsername("testUser");
        connectionConfigNullDefaultSchema.setPassword("testPassword");
        connectionConfigNullDefaultSchema.setDefaultSchema(null);
        new OBConsoleDataSourceFactory(connectionConfigNullDefaultSchema, false);
    }

    /**
     * Test getDataSource method with empty defaultSchema
     */
    @Test
    public void testGetDataSourceWithEmptyDefaultSchema() {
        ConnectionConfig connectionConfigEmptyDefaultSchema = new ConnectionConfig();
        connectionConfigEmptyDefaultSchema.setHost("localhost");
        connectionConfigEmptyDefaultSchema.setPort(1521);
        connectionConfigEmptyDefaultSchema.setUsername("testUser");
        connectionConfigEmptyDefaultSchema.setPassword("testPassword");
        connectionConfigEmptyDefaultSchema.setDefaultSchema("");
        DataSource dataSource = new OBConsoleDataSourceFactory(connectionConfigEmptyDefaultSchema, false).getDataSource();
        Connection connection = dataSource.getConnection();
        assertTrue(connection.createStatement().executeQuery("SELECT COUNT(*) FROM DUAL").getInt(1) > 0);
        connection.close();
        DataSourceUtils.close(dataSource);
    }

    /**
     * Test getDataSource method with invalid dialectType
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidDialectType() {
        ConnectionConfig connectionConfigInvalidDialectType = new ConnectionConfig();
        connectionConfigInvalidDialectType.setHost("localhost");
        connectionConfigInvalidDialectType.setPort(1521);
        connectionConfigInvalidDialectType.setUsername("testUser");
        connectionConfigInvalidDialectType.setPassword("testPassword");
        connectionConfigInvalidDialectType.setType(ConnectType.INVALID);
        new OBConsoleDataSourceFactory(connectionConfigInvalidDialectType, false);
    }

    /**
     * Test getDataSource method with empty JDBCUrlParameters
     */
    @Test
    public void testGetDataSourceWithEmptyJdbcUrlParameters() {
        ConnectionConfig connectionConfigEmptyJdbcUrlParameters = new ConnectionConfig();
        connectionConfigEmptyJdbcUrlParameters.setHost("localhost");
        connectionConfigEmptyJdbcUrlParameters.setPort(1521);
        connectionConfigEmptyJdbcUrlParameters.setUsername("testUser");
        connectionConfigEmptyJdbcUrlParameters.setPassword("testPassword");
        connectionConfigEmptyJdbcUrlParameters.setJdbcUrlParameters(new HashMap<>());
        DataSource dataSource = new OBConsoleDataSourceFactory(connectionConfigEmptyJdbcUrlParameters, false).getDataSource();
        Connection connection = dataSource.getConnection();
        assertTrue(connection.createStatement().executeQuery("SELECT COUNT(*) FROM DUAL").getInt(1) > 0);
        connection.close();
        DataSourceUtils.close(dataSource);
    }

    /**
     * Test getDataSource method with invalid JDBCUrlParameters
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithInvalidJdbcUrlParameters() {
        ConnectionConfig connectionConfigInvalidJdbcUrlParameters = new ConnectionConfig();
        connectionConfigInvalidJdbcUrlParameters.setHost("localhost");
        connectionConfigInvalidJdbcUrlParameters.setPort(1521);
        connectionConfigInvalidJdbcUrlParameters.setUsername("testUser");
        connectionConfigInvalidJdbcUrlParameters.setPassword("testPassword");
        connectionConfigInvalidJdbcUrlParameters.setJdbcUrlParameters(new HashMap<String, Object>() {{
            put("maxAllowedPacket", "invalidValue");
        }});
        new OBConsoleDataSourceFactory(connectionConfigInvalidJdbcUrlParameters, false);
    }
}