import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @InjectMocks
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;

    private BasicDataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dataSource = new BasicDataSource();
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("test", "test", "127.0.0.1", 3306, null, null, null), true, true);
        obConsoleDataSourceFactory.setDataSource(dataSource);
        when(connectionExtensionPoint.generateJdbcUrl("127.0.0.1", 3306, null, null)).thenReturn("jdbc:test:test");
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        Assert.assertEquals("jdbc:test:test", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "test_user";
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("test", expectedUsername, "127.0.0.1", 3306, null, null, null), true, true);
        String actualUsername = obConsoleDataSourceFactory.getUsername();
        Assert.assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "test_password";
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null), true, true);
        obConsoleDataSourceFactory.setPassword(expectedPassword);
        String actualPassword = obConsoleDataSourceFactory.getPassword();
        Assert.assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);
        Assert.assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSource_withoutInitializingConnection() throws SQLException {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null), true, false);
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDialectType() {
        Assert.assertEquals(ConnectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null), true, true);
        OBConsoleDataSourceFactory clonedFactory = originalFactory.deepCopy();
        Assert.assertNotSame(originalFactory, clonedFactory);
        Assert.assertEquals(originalFactory.getUsername(), clonedFactory.getUsername());
        Assert.assertEquals(originalFactory.getPassword(), clonedFactory.getPassword());
        Assert.assertEquals(originalFactory.getJdbcUrl(), clonedFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<String> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map("test_username")).thenReturn(newUsername);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetUsername(mapper);
        String resultUsername = obConsoleDataSourceFactory.getUsername();
        Assert.assertEquals(newUsername, resultUsername);
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<String> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map("test_password")).thenReturn(newPassword);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetPassword(mapper);
        String resultPassword = obConsoleDataSourceFactory.getPassword();
        Assert.assertEquals(newPassword, resultPassword);
    }

    @Test
    public void testResetHost() {
        String newHost = "localhost";
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<String> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map("127.0.0.1")).thenReturn(newHost);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetHost(mapper);
        String resultHost = obConsoleDataSourceFactory.getHost();
        Assert.assertEquals(newHost, resultHost);
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<Integer> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map(3306)).thenReturn(newPort);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetPort(mapper);
        int resultPort = obConsoleDataSourceFactory.getPort();
        Assert.assertEquals(newPort, resultPort);
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<String> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map("test_schema")).thenReturn(newSchema);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetSchema(mapper);
        String resultSchema = obConsoleDataSourceFactory.getDefaultSchema();
        Assert.assertEquals(newSchema, resultSchema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key", "value");
        OBConsoleDataSourceFactoryFactory factoryFactory = mock(OBConsoleDataSourceFactoryFactory.class);
        OBConsoleDataSourceFactoryFactory.ValueMapper<Map<String, String>> mapper = mock(OBConsoleDataSourceFactoryFactory.ValueMapper.class);
        when(mapper.map(obConsoleDataSourceFactory.getParameters())).thenReturn(newParameters);
        when(factoryFactory.newInstance(mapper)).thenReturn(obConsoleDataSourceFactory);
        obConsoleDataSourceFactory.setFactory(factoryFactory);
        obConsoleDataSourceFactory.resetParameters(mapper);
        Map<String, String> resultParameters = obConsoleDataSourceFactory.getParameters();
        Assert.assertEquals(newParameters, resultParameters);
    }

    @Test
    public void testGetUsername_withEmptyUsername_returnsDefaultUsernameForOracle() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, DialectType.ORACLE);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String username = obConsoleDataSourceFactory.getUsername();
        Assert.assertEquals("", username);
    }

    @Test
    public void testGetDefaultSchema_whenDialectTypeIsOracle_returnsEmptyString() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, DialectType.ORACLE);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        String defaultSchema = obConsoleDataSourceFactory.getDefaultSchema();
        Assert.assertEquals("", defaultSchema);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withNullConnectionConfig() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(null, true, true);
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        obConsoleDataSourceFactory.setConnectionExtensionPoint(null);
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withNegativePort() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", -1, null, null, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_withEmptyDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        obConsoleDataSourceFactory.setDefaultSchema("");
        obConsoleDataSourceFactory.getDataSource();
    }

    @Test
    public void testGetDataSource_withEmptyParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        Properties expectedProperties = new Properties();
        obConsoleDataSourceFactory.setParameters(expectedProperties);
        DataSource actualDataSource = obConsoleDataSourceFactory.getDataSource();
        Assert.assertEquals(expectedProperties, actualDataSource.getConnectionProperties());
    }

    @Test
    public void testGetDataSource_withAllParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        parameters.put("key2", "value2");
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, null, null, parameters);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        Properties expectedProperties = new Properties();
        expectedProperties.setProperty("key1", "value1");
        expectedProperties.setProperty("key2", "value2");
        obConsoleDataSourceFactory.setParameters(expectedProperties);
        DataSource actualDataSource = obConsoleDataSourceFactory.getDataSource();
        Assert.assertEquals(expectedProperties, actualDataSource.getConnectionProperties());
    }

    @Test
    public void testGetDataSource_withSslConfig() {
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, sslConfig, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource actualDataSource = obConsoleDataSourceFactory.getDataSource();
        boolean useSSL = actualDataSource.getConnectionProperties().getProperty("useSSL").equals("true");
        boolean trustServerCertificate = actualDataSource.getConnectionProperties().getProperty("trustServerCertificate").equals("true");
        Assert.assertTrue(useSSL);
        Assert.assertTrue(trustServerCertificate);
    }

    @Test
    public void testGetDataSource_withSslFileEntry() {
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setSslFileEntry(sslFileEntry);
        ConnectionConfig connectionConfig = new ConnectionConfig("test", null, "127.0.0.1", 3306, null, sslConfig, null);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource actualDataSource = obConsoleDataSourceFactory.getDataSource();
        String trustStore = actualDataSource.getConnectionProperties().getProperty("trustStore");
        String trustStorePassword = actualDataSource.getConnectionProperties().getProperty("trustStorePassword");
        String keyStore = actualDataSource.getConnectionProperties().getProperty("keyStore");
        String keyStorePassword = actualDataSource.getConnectionProperties().getProperty("keyStorePassword");
        Assert.assertEquals("/path/to/keystore.p12", trustStore);
        Assert.assertEquals("password", trustStorePassword);
        Assert.assertEquals("/path/to/keystore.p12", keyStore);
        Assert.assertEquals("password", keyStorePassword);
    }
}