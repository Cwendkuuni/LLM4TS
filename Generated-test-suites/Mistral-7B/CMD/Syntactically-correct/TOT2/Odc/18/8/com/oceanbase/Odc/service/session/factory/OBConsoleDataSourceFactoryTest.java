import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DbUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private SingleConnectionDataSource dataSource;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        connectionConfig = Mockito.spy(new ConnectionConfig());
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionInitializer = Mockito.mock(ConnectionInitializer.class);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:obnew:obcds://localhost:1527/test_schema?useSSL=true&trustServerCertificate=true&trustStore=C:/test/test.truststore&trustStorePassword=testpassword&keyStore=C:/test/test.keystore&keyStorePassword=testpassword";

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("C:/test/test.truststore");
        sslFileEntry.setKeyStoreFilePassword("testpassword");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq("localhost"), Mockito.eq(1527), Mockito.eq("test_schema"), Mockito.anyMap()))
                .thenReturn(expectedJdbcUrl);
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ConnectionInitializer[]{connectionInitializer});

        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
        } catch (SQLException e) {
            fail("Should be able to get a connection");
        } finally {
            DbUtils.closeQuietly(dataSource.getConnection());
        }
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        when(connectionConfig.getDialectType()).thenReturn(expectedDialectType);

        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory cloneableDataSourceFactory = Mockito.spy(dataSourceFactory);

        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        cloneableDataSourceFactory.resetUsername(mapper);
        cloneableDataSourceFactory.resetPassword(mapper);
        cloneableDataSourceFactory.resetHost(mapper);
        cloneableDataSourceFactory.resetPort(mapper);
        cloneableDataSourceFactory.resetSchema(mapper);
        cloneableDataSourceFactory.resetParameters(mapper);

        OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) cloneableDataSourceFactory.deepCopy();

        String originalUsername = dataSourceFactory.getUsername();
        String clonedUsername = clonedDataSourceFactory.getUsername();

        assertEquals(originalUsername, clonedUsername);

        String originalPassword = dataSourceFactory.getPassword();
        String clonedPassword = clonedDataSourceFactory.getPassword();

        assertNotEquals(originalPassword, clonedPassword);

        String originalHost = dataSourceFactory.getHost();
        String clonedHost = clonedDataSourceFactory.getHost();

        assertEquals(originalHost, clonedHost);

        int originalPort = dataSourceFactory.getPort();
        int clonedPort = clonedDataSourceFactory.getPort();

        assertEquals(originalPort, clonedPort);

        String originalDefaultSchema = dataSourceFactory.getDefaultSchema();
        String clonedDefaultSchema = clonedDataSourceFactory.getDefaultSchema();

        assertEquals(originalDefaultSchema, clonedDefaultSchema);

        Map<String, String> originalParameters = dataSourceFactory.getParameters();
        Map<String, String> clonedParameters = clonedDataSourceFactory.getParameters();

        assertEquals(originalParameters, clonedParameters);

        String originalUsername2 = dataSourceFactory.getUsername();
        String clonedUsername2 = clonedDataSourceFactory.getUsername();

        assertNotEquals(originalUsername2, clonedUsername2);
    }

    @Test
    public void testResetUsername() {
        String originalUsername = dataSourceFactory.getUsername();
        String newUsername = "new_username";

        dataSourceFactory.resetUsername(value -> value.replaceAll("old_username", newUsername));

        assertEquals(newUsername, dataSourceFactory.getUsername());
        assertEquals(originalUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String originalPassword = dataSourceFactory.getPassword();
        String newPassword = "new_password";

        dataSourceFactory.resetPassword(value -> value.replaceAll("old_password", newPassword));

        assertEquals(newPassword, dataSourceFactory.getPassword());
        assertEquals(originalPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String originalHost = dataSourceFactory.getHost();
        String newHost = "new_host";

        dataSourceFactory.resetHost(value -> value.replaceAll("old_host", newHost));

        assertEquals(newHost, dataSourceFactory.getHost());
        assertEquals(originalHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int originalPort = dataSourceFactory.getPort();
        int newPort = 1234;

        dataSourceFactory.resetPort(value -> value + 1);

        assertEquals(newPort, dataSourceFactory.getPort());
        assertEquals(originalPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String originalSchema = dataSourceFactory.getDefaultSchema();
        String newSchema = "new_schema";

        dataSourceFactory.resetSchema(value -> value.replaceAll("old_schema", newSchema));

        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
        assertEquals(originalSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> originalParameters = dataSourceFactory.getParameters();
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("new_key", "new_value");

        dataSourceFactory.resetParameters(value -> {
            value.putAll(newParameters);
            return value;
        });

        Map<String, String> actualParameters = dataSourceFactory.getParameters();
        assertEquals(newParameters, actualParameters);
        assertEquals(originalParameters, actualParameters);
    }

    @Test
    public void testConnectionInitializers() throws SQLException {
        Connection initializerConnection = Mockito.mock(Connection.class);
        when(connectionInitializer.initializeDatabase(Mockito.eq(dataSource), Mockito.anyString())).thenReturn(initializerConnection);

        List<ConnectionInitializer> connectionInitializers = new ConnectionInitializer[]{connectionInitializer};
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(connectionInitializers);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.initConnection();

        verify(connectionInitializer).initializeDatabase(dataSource, "initializer");
    }

    @Test(expected = IllegalStateException.class)
    public void testInitConnectionWithNullConnectionExtensionPoint() {
        connectionExtensionPoint = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.initConnection();
    }

    @Test(expected = IllegalStateException.class)
    public void testInitConnectionWithEmptyConnectionInitializers() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[0]);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.initConnection();
    }

    @Test
    public void testEventPublisher() {
        String event = "event";

        when(eventPublisher.publishEvent(Mockito.eq(event))).thenReturn(true);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.setEventPublisher(eventPublisher);
        dataSourceFactory.initConnection();

        verify(eventPublisher).publishEvent(event);
    }

    @After
    public void tearDown() {
        DbUtils.closeQuietly(dataSource.getConnection());
    }
}