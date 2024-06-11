package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringBootTest;
import org.springframework.test.context.junit.runner.JUnitTestClassRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionInitializer.InitializerType;
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

import lombok.NonNull;
import lombok.Setter;

@RunWith(JUnitTestClassRunner.class)
@SpringBootTest
public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        String username = factory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        String password = factory.getPassword();
        assertNull(password); // password should be hidden
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        String defaultSchema = factory.getDefaultSchema();
        assertNotNull(defaultSchema);
    }

    @Test
    public void testGetParameters() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        Map<String, String> parameters = factory.getParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory originalFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        ObConsoleDataSourceFactory copiedFactory = (ObConsoleDataSourceFactory) originalFactory.deepCopy();
        assertNotNull(copiedFactory);
        assertNotSame(originalFactory, copiedFactory);
        assertEquals(originalFactory.getUsername(), copiedFactory.getUsername());
        assertEquals(originalFactory.getPassword(), copiedFactory.getPassword());
        assertEquals(originalFactory.getHost(), copiedFactory.getHost());
        assertEquals(originalFactory.getPort(), copiedFactory.getPort());
        assertEquals(originalFactory.getDefaultSchema(), copiedFactory.getDefaultSchema());
        assertEquals(originalFactory.getParameters(), copiedFactory.getParameters());
        assertEquals(originalFactory.getDialectType(), copiedFactory.getDialectType());
    }

    @Test
    public void testResetValues() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, false);

        String originalUsername = factory.getUsername();
        factory.resetUsername((value) -> originalUsername + "_reset");
        String newUsername = factory.getUsername();
        assertEquals(originalUsername + "_reset", newUsername);

        String originalPassword = factory.getPassword();
        factory.resetPassword((value) -> originalPassword + "_reset");
        String newPassword = factory.getPassword();
        assertNull(newPassword);

        String originalHost = factory.getHost();
        factory.resetHost((value) -> originalHost + "_reset");
        String newHost = factory.getHost();
        assertEquals(originalHost + "_reset", newHost);

        int originalPort = factory.getPort();
        factory.resetPort((value) -> originalPort + 1);
        int newPort = factory.getPort();
        assertEquals(originalPort + 1, newPort);

        String originalSchema = factory.getDefaultSchema();
        factory.resetSchema((value) -> originalSchema + "_reset");
        String newSchema = factory.getDefaultSchema();
        assertEquals(originalSchema + "_reset", newSchema);

        Map<String, String> originalParameters = factory.getParameters();
        factory.resetParameters((map) -> {
            HashMap<String, String> newMap = new HashMap<>(map);
            newMap.put("testKey", "testValue");
            return newMap;
        });
        Map<String, String> newParameters = factory.getParameters();
        assertEquals(originalParameters, newParameters);
        assertEquals("testValue", newParameters.get("testKey"));
    }

    @Test
    public void testCreateDataSourceWithInitializers() {
        ConnectionConfig connectionConfig = getTestConnectionConfig();
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);

        ConnectionInitializer initializer1 = Mockito.mock(ConnectionInitializer.class);
        ConnectionInitializer initializer2 = Mockito.mock(ConnectionInitializer.class);
        Mockito.when(initializer1.getInitializerType()).thenReturn(InitializerType.BEFORE_CREATE);
        Mockito.when(initializer2.getInitializerType()).thenReturn(InitializerType.AFTER_CREATE);

        factory.addInitializer(initializer1);
        factory.addInitializer(initializer2);

        dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    private ConnectionConfig getTestConnectionConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("foo", "bar");
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("testPassword");
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);

        return connectionConfig;
    }
}