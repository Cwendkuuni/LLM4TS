import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.CollectionUtils;
import static org.junit.Assert.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setEventPublisher(Mockito.mock(EventPublisher.class));
        factory.setConnectionExtensionPoint(connectionExtensionPoint);

        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testdb");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setConnectionExtensionPoint(connectionExtensionPoint);

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testdb", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUsername("user1");

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("user1", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password1");

        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals("password1", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);

        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals(9, jdbcParams.size());
        assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
        assertTrue(jdbcParams.containsKey("allowMultiQueries"));
        assertTrue(jdbcParams.containsKey("connectTimeout"));
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetUsername(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyString());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetPassword(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyString());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetHost(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyString());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetPort(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyInt());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetSchema(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyString());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(Mockito.mock(ConnectionConfig.class), true, true);
        factory.resetParameters(valueMapper);
        Mockito.verify(valueMapper, Mockito.times(1)).map(Mockito.anyMap());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertNotSame(factory, clonedFactory);
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructorWithNoInitConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        assertFalse(factory.isInitConnection());
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructorWithInitConnection() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertTrue(factory.isInitConnection());
    }

    @Test
    public void testGetConnectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.PRIMARY);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        assertEquals(ConnectType.PRIMARY, factory.getConnectType());
    }

    @Test
    public void testDefaultSchemaForMySQL() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
        assertEquals("testdb", defaultSchema);
    }

    @Test
    public void testDefaultSchemaForOracle() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);
        assertEquals("\"user1\"", defaultSchema);
    }

    @Test
    public void testGetJdbcUrlParamsForSSLConfigEnabled() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.getSslConfig().setEnabled(true);

        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(jdbcUrlParams.containsKey("useSSL"));
    }

    @Test
    public void testGetJdbcUrlParamsForSslFileEntry() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("path/to/keystore.jks");
        sslFileEntry.setKeyStoreFilePassword("password");
        connectionConfig.setSslFileEntry(sslFileEntry);

        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(jdbcUrlParams.containsKey("trustStore"));
        assertTrue(jdbcUrlParams.containsKey("trustStorePassword"));
    }

    @Test
    public void testGetJdbcUrlParamsForEndpoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("localhost");
        endpoint.setProxyPort(8080);
        connectionConfig.setEndpoint(endpoint);

        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(jdbcUrlParams.containsKey("socksProxyHost"));
        assertTrue(jdbcUrlParams.containsKey("socksProxyPort"));
    }

    @Test
    public void testGetJdbcUrlParamsForJdbcUrlParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.getJdbcUrlParameters().put("testParam", "testValue");

        Map<String, String> jdbcUrlParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(jdbcUrlParams.containsKey("testParam"));
    }

    @Test
    public void testGetUsernameForOracle() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("user1");

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals("\"user1\"@tenant_db#cluster", username);
    }

}