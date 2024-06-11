import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private static final String DEFAULT_ZERO_DATE_TIME_BEHAVIOR = "convertToNull";
    private static final String CONNECTION_TYPE = "ObConnectionType";
    private static final String DIALECT_TYPE = "MySQLDialect";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String HOST = "localhost";
    private static final int PORT = 63306;
    private static final String DEFAULT_SCHEMA = "testSchema";
    private static final Map<String, String> PARAMETERS = new HashMap<>();
    static {
        PARAMETERS.put("maxAllowedPacket", "64000000");
        PARAMETERS.put("allowMultiQueries", "true");
        PARAMETERS.put("connectTimeout", "5000");
        PARAMETERS.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        PARAMETERS.put("noDatetimeStringSync", "true");
        PARAMETERS.put("jdbcCompliantTruncation", "false");
        PARAMETERS.put("sendConnectionAttributes", "false");
    }

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSourceFactory dataSourceFactorySpy;
    private EventPublisher eventPublisherMock;
    private ConnectionInitializer connectionInitializerMock;
    private SessionCreatedInitializer sessionCreatedInitializerMock;
    private ConnectionExtensionPoint connectionExtensionPointMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(USERNAME);
        connectionConfig.setPassword(PASSWORD);
        connectionConfig.setHost(HOST);
        connectionConfig.setPort(PORT);
        connectionConfig.setDefaultSchema(DEFAULT_SCHEMA);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.OB_CONNECTION);
        connectionConfig.setJdbcUrlParameters(PARAMETERS);

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test.p12");
        sslFileEntry.setKeyStoreFilePassword("testPassword");
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setSslFileEntry(sslFileEntry);

        eventPublisherMock = mock(EventPublisher.class);
        connectionInitializerMock = mock(ConnectionInitializer.class);
        sessionCreatedInitializerMock = mock(SessionCreatedInitializer.class);
        connectionExtensionPointMock = mock(ConnectionExtensionPoint.class);

        dataSourceFactorySpy = mock(DataSourceFactory.class);

        when(connectionExtensionPointMock.getConnectionInitializers()).thenReturn(Collections.singletonList(connectionInitializerMock));
        when(connectionExtensionPointMock.getConnectionInitializers(DialectType.OB_MYSQL)).thenReturn(Collections.singletonList(connectionInitializerMock));
        when(connectionExtensionPointMock.getConnectionMapper()).thenReturn(ConnectionMapper.INSTANCE);
        when(connectionExtensionPointMock.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPointMock.generateJdbcUrl(ArgumentMatchers.eq(HOST), ArgumentMatchers.eq(PORT), ArgumentMatchers.eq(DEFAULT_SCHEMA), ArgumentMatchers.eq(PARAMETERS))).thenReturn("jdbc:mysql://localhost:63306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false");

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource dataSource = (BasicDataSource) dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertNotNull(dataSource.getConnection());
        assertNotNull(dataSource.getUrl());
        assertNotNull(dataSource.getUsername());
        assertNotNull(dataSource.getPassword());
        assertNotNull(dataSource.getDriverClassName());
        assertNotNull(dataSource.getConnectionProperties());
    }

    @Test
    public void testGetDialectType() throws Exception {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(dialectType, DialectType.OB_MYSQL);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(clonedDataSourceFactory.getUsername(), dataSourceFactory.getUsername());
        assertEquals(clonedDataSourceFactory.getPassword(), dataSourceFactory.getPassword());
        assertEquals(clonedDataSourceFactory.getHost(), dataSourceFactory.getHost());
        assertEquals(clonedDataSourceFactory.getPort(), dataSourceFactory.getPort());
        assertEquals(clonedDataSourceFactory.getDefaultSchema(), dataSourceFactory.getDefaultSchema());
        assertEquals(clonedDataSourceFactory.getParameters(), dataSourceFactory.getParameters());
        assertEquals(clonedDataSourceFactory.getConnectType(), dataSourceFactory.getConnectType());
        assertEquals(clonedDataSourceFactory.getDialectType(), dataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "testNewUsername";
        dataSourceFactory.resetUsername(arg -> newUsername);
        assertEquals(dataSourceFactory.getUsername(), newUsername);
    }

    @Test
    public void testResetPassword() {
        String newPassword = "testNewPassword";
        dataSourceFactory.resetPassword(arg -> newPassword);
        assertEquals(dataSourceFactory.getPassword(), newPassword);
    }

    @Test
    public void testResetHost() {
        String newHost = "testNewHost";
        dataSourceFactory.resetHost(arg -> newHost);
        assertEquals(dataSourceFactory.getHost(), newHost);
    }

    @Test
    public void testResetPort() {
        int newPort = 63307;
        dataSourceFactory.resetPort(arg -> newPort);
        assertEquals(dataSourceFactory.getPort(), newPort);
    }

    @Test
    public void testResetSchema() {
        String newSchema = "testNewSchema";
        dataSourceFactory.resetSchema(arg -> newSchema);
        assertEquals(dataSourceFactory.getDefaultSchema(), newSchema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("maxAllowedPacket", "65000000");
        dataSourceFactory.resetParameters(arg -> newParameters);
        assertEquals(dataSourceFactory.getParameters(), newParameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetWithNullValueMapper() {
        dataSourceFactory.resetUsername(null);
    }

    @Test
    public void testIsInitConnection() {
        assertTrue(dataSourceFactory.isInitConnection());
    }

    @Test
    public void testIsAutoCommit() {
        assertFalse(dataSourceFactory.isAutoCommit());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(jdbcUrl, "jdbc:mysql://localhost:63306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false");
    }

    @Test
    public void testGetUsername() {
        assertEquals(dataSourceFactory.getUsername(), USERNAME);
    }

    @Test
    public void testGetPassword() {
        assertEquals(dataSourceFactory.getPassword(), PASSWORD);
    }

    @Test
    public void testGetConnectType() {
        assertEquals(dataSourceFactory.getConnectType(), ConnectType.OB_CONNECTION);
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        OBConsoleDataSourceFactory clonedDataSourceFactory2 = dataSourceFactory.deepCopy();
        assertNotSame(dataSourceFactory, clonedDataSourceFactory);
        assertNotSame(dataSourceFactory, clonedDataSourceFactory2);
        assertNotSame(clonedDataSourceFactory, clonedDataSourceFactory2);
        assertEquals(clonedDataSourceFactory, clonedDataSourceFactory2);
    }

    @Test
    public void testGetConnectionInitializers() {
        List<ConnectionInitializer> connectionInitializers = dataSourceFactory.getConnectionInitializers();
        assertEquals(connectionInitializers.size(), 1);
        assertEquals(connectionInitializers.get(0), connectionInitializerMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConnectionInitializersUnsupportedOperationException() {
        dataSourceFactory.getConnectionInitializers(DialectType.OB_ORACLE);
    }

    @Test
    public void testGetEventPublisher() {
        assertEquals(dataSourceFactory.getEventPublisher(), eventPublisherMock);
    }

    @Test
    public void testGetConnectionExtensionPoint() {
        assertEquals(dataSourceFactory.getConnectionExtensionPoint(), connectionExtensionPointMock);
    }

    @Test
    public void testGetSessionCreatedInitializer() {
        assertEquals(dataSourceFactory.getSessionCreatedInitializer(), sessionCreatedInitializerMock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertNotNull(dataSourceFactory.getDataSource().getConnection());
    }

    @Test
    public void testGetConnectionNull() throws SQLException {
        when(dataSourceFactorySpy.getDataSource()).thenReturn(null);
        assertNull(dataSourceFactory.getConnection());
    }

    @Test
    public void testGetConnectionException() throws SQLException {
        when(dataSourceFactorySpy.getDataSource()).thenThrow(new SQLException());
        assertNull(dataSourceFactory.getConnection());
    }

}