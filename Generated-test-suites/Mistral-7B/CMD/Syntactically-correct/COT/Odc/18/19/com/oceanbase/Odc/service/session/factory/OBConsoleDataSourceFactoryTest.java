import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private ConnectionPluginUtil connectionPluginUtil;

    @Mock
    private ConnectionMapper connectionMapper;

    @InjectMocks
    private OBConsoleDataSourceFactory testObject;

    @Before
    public void setUp() {
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.emptyList());
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(mock(ConnectionInitializer.class)));
        when(connectionPluginUtil.getConnectionExtension(Mockito.any(DialectType.class))).thenReturn(connectionExtensionPoint);
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:url:test");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
    }

    @After
    public void tearDown() {
        reset(eventPublisher, connectionExtensionPoint, connectionPluginUtil, connectionMapper);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Properties properties = new Properties();
        SingleConnectionDataSource singleConnectionDataSource = new SingleConnectionDataSource();

        DataSource dataSource = testObject.getDataSource();
        assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
        assertEquals("com.oceanbase.jdbc.Driver", dataSource.getDriverClassName());
        assertNotNull(dataSource.getConnection());
        verify(singleConnectionDataSource).setEventPublisher(eventPublisher);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionConfig() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(null, true);
        factory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, null);
        factory.getDataSource();
    }

    @Test
    public void testGetJdbcUrl() {
        String host = "localhost";
        Integer port = 6308;
        String defaultSchema = "testSchema";
        Map<String, String> parameters = new HashMap<>();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost(host);
        connectionConfig.setPort(port);
        connectionConfig.setDefaultSchema(defaultSchema);
        connectionConfig.setParameters(parameters);

        String jdbcUrl = testObject.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:url:test?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR
                + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String username = "testUsername";
        connectionConfig.setUsername(username);

        String expectedUsername = "testUsername";
        String actualUsername = testObject.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String password = "testPassword";
        connectionConfig.setPassword(password);

        String expectedPassword = "testPassword";
        String actualPassword = testObject.getPassword();

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String defaultSchema = "testSchema";
        connectionConfig.setDefaultSchema(defaultSchema);

        String expectedDefaultSchema = "testSchema";
        String actualDefaultSchema = testObject.getDefaultSchema();

        assertEquals(expectedDefaultSchema, actualDefaultSchema);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSslConfig(new SSLConfig(true, null, null, null, null));
        Map<String, String> jdbcParams = testObject.getJdbcParams(connectionConfig);

        Map<String, String> expectedJdbcParams = new HashMap<>();
        expectedJdbcParams.put("maxAllowedPacket", "64000000");
        expectedJdbcParams.put("allowMultiQueries", "true");
        expectedJdbcParams.put("connectTimeout", "5000");
        expectedJdbcParams.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        expectedJdbcParams.put("noDatetimeStringSync", "true");
        expectedJdbcParams.put("jdbcCompliantTruncation", "false");
        expectedJdbcParams.put("sendConnectionAttributes", "false");
        expectedJdbcParams.put("allowLoadLocalInfile", "false");
        expectedJdbcParams.put("allowUrlInLocalInfile", "false");
        expectedJdbcParams.put("allowLoadLocalInfileInPath", "");
        expectedJdbcParams.put("autoDeserialize", "false");

        if (connectionConfig.getSslConfig() != null) {
            expectedJdbcParams.put("useSSL", "true");
            expectedJdbcParams.put("trustServerCertificate", "true");
            SSLFileEntry sslFileEntry = connectionConfig.getSslFileEntry();
            if (sslFileEntry != null) {
                expectedJdbcParams.put("trustStore", sslFileEntry.getKeyStoreFilePath());
                expectedJdbcParams.put("trustStorePassword", sslFileEntry.getKeyStoreFilePassword());
                expectedJdbcParams.put("keyStore", sslFileEntry.getKeyStoreFilePath());
                expectedJdbcParams.put("keyStorePassword", sslFileEntry.getKeyStoreFilePassword());
                expectedJdbcParams.put("trustServerCertificate", "false");
            } else {
                expectedJdbcParams.put("useSSL", "false");
            }
        }

        assertEquals(expectedJdbcParams, jdbcParams);
    }
}