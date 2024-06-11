package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

/**
 * Test class for {@link OBConsoleDataSourceFactory}
 *
 * @author yh263208
 * @date 2023-03-29
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private OBConsoleDataSourceFactory factory;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUsername");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("testHost");
        connectionConfig.setPort(1234);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setInitConnection(true);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.getSslConfig().setEnabled(true);
        connectionConfig.setSslFileEntry(new SSLFileEntry());
        connectionConfig.getSslFileEntry().setKeyStoreFilePath("/path/to/keystore.p12");
    }

    @After
    public void cleanup() {
        factory = null;
        connectionExtensionPoint = null;
        eventPublisher = null;
        connectionConfig = null;
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructor() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getUsername());
        assertNotNull(factory.getPassword());
        assertNotNull(factory.getHost());
        assertNotNull(factory.getPort());
        assertNotNull(factory.getDefaultSchema());
        assertNotNull(factory.getParameters());
        assertNotNull(factory.getConnectionConfig());
        assertFalse(factory.isInitConnection());
    }

    @Test
    public void testGetDataSource() throws Exception {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setUrl(factory.getJdbcUrl());
        dataSource.setUsername(factory.getUsername());
        dataSource.setPassword(factory.getPassword());
        dataSource.setDriverClassName(connectionExtensionPoint.getDriverClassName());
        Properties properties = new Properties();
        properties.setProperty("allowLoadLocalInfile", "false");
        properties.setProperty("allowUrlInLocalInfile", "false");
        properties.setProperty("allowLoadLocalInfileInPath", "");
        properties.setProperty("autoDeserialize", "false");
        dataSource.setConnectionProperties(properties);
        if (factory.isAutoCommit()) {
            dataSource.setAutoCommit(factory.isAutoCommit());
        }
        if (factory.isInitConnection()) {
            List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
            dataSource.addInitializer(new SessionCreatedInitializer(connectionConfig));
            initializers.forEach(dataSource::addInitializer);
        }
        DataSource expectedDataSource = dataSource;
        DataSource actualDataSource = factory.getDataSource();
        assertNotNull(actualDataSource);
        assertTrue(actualDataSource instanceof SingleConnectionDataSource);
        assertSame(expectedDataSource, actualDataSource);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionConfig() {
        factory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        factory = new OBConsoleDataSourceFactory(connectionConfig, null);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(factory.getHost(), factory.getPort(),
                factory.getDefaultSchema(), factory.getParameters());
        String actualJdbcUrl = factory.getJdbcUrl();
        assertSame(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUsername@testTenantName#testClusterName";
        String actualUsername = factory.getUsername();
        assertSame(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        String actualPassword = factory.getPassword();
        assertSame(expectedPassword, actualPassword);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> expectedParameters = new HashMap<>();
        Map<String, String> actualParameters = factory.getParameters();
        assertSame(expectedParameters, actualParameters);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = connectionConfig.getDialectType();
        DialectType actualDialectType = factory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() throws CloneNotSupportedException {
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertFalse(clonedFactory.equals(factory));
        assertNotNull(clonedFactory.getUsername());
        assertNotNull(clonedFactory.getPassword());
        assertNotNull(clonedFactory.getHost());
        assertNotNull(clonedFactory.getPort());
        assertNotNull(clonedFactory.getDefaultSchema());
        assertNotNull(clonedFactory.getParameters());
        assertNotNull(clonedFactory.getConnectionConfig());
        assertFalse(clonedFactory.isInitConnection());
        assertSame(connectionExtensionPoint, clonedFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        factory.resetUsername(mock(CloneableDataSourceFactory.ValueMapper.class));
        String actualUsername = factory.getUsername();
        assertSame(newUsername, actualUsername);
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        factory.resetPassword(mock(CloneableDataSourceFactory.ValueMapper.class));
        String actualPassword = factory.getPassword();
        assertSame(newPassword, actualPassword);
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        factory.resetHost(mock(CloneableDataSourceFactory.ValueMapper.class));
        String actualHost = factory.getHost();
        assertSame(newHost, actualHost);
    }

    @Test
    public void testResetPort() {
        Integer newPort = 5678;
        factory.resetPort(mock(CloneableDataSourceFactory.ValueMapper.class));
        Integer actualPort = factory.getPort();
        assertEquals(newPort, actualPort);
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        factory.resetSchema(mock(CloneableDataSourceFactory.ValueMapper.class));
        String actualSchema = factory.getDefaultSchema();
        assertSame(newSchema, actualSchema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newKey", "newValue");
        factory.resetParameters(mock(CloneableDataSourceFactory.ValueMapper.class));
        Map<String, String> actualParameters = factory.getParameters();
        assertSame(newParameters, actualParameters);
    }

    @Test
    public void testGetConnectType() {
        ConnectType expectedConnectType = connectionConfig.getType();
        ConnectType actualConnectType = factory.getConnectType();
        assertEquals(expectedConnectType, actualConnectType);
    }

    @Test
    public void testGetDefaultSchemaWithDbUser() {
        String expectedDefaultSchema = "testSchema";
        String dbUser = "testUsername";
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualDefaultSchema = factory.getDefaultSchema();
        assertEquals(expectedDefaultSchema, actualDefaultSchema);

        String actualDbUser = factory.getUsername();
        assertEquals(dbUser, actualDbUser);
    }

    @Test
    public void testGetDefaultSchemaWithoutDbUser() {
        String expectedDefaultSchema = "testSchema";
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String actualDefaultSchema = factory.getDefaultSchema();
        assertEquals(expectedDefaultSchema, actualDefaultSchema);

        String actualUsername = factory.getUsername();
        assertNull(actualUsername);
    }

    @Test
    public void testGetJdbcUrlWithSslConfiguration() throws Exception {
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(factory.getHost(), factory.getPort(),
                factory.getDefaultSchema(), factory.getParameters());
        String expectedSslUrl = expectedJdbcUrl + "?useSSL=true&trustStore=" + ConnectionSessionUtil.getTrustStoreFilePath()
                + "&trustStorePassword=" + ConnectionSessionUtil.getTrustStorePassword() + "&keyStore="
                + ConnectionSessionUtil.getKeyStoreFilePath() + "&keyStorePassword="
                + ConnectionSessionUtil.getKeyStorePassword();
        String actualJdbcUrl = factory.getJdbcUrl();
        assertSame(expectedSslUrl, actualJdbcUrl);
    }

    @Test
    public void testGetJdbcUrlWithoutSslConfiguration() throws Exception {
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(factory.getHost(), factory.getPort(),
                factory.getDefaultSchema(), factory.getParameters());
        String actualJdbcUrl = factory.getJdbcUrl();
        assertSame(expectedJdbcUrl, actualJdbcUrl);
    }
}