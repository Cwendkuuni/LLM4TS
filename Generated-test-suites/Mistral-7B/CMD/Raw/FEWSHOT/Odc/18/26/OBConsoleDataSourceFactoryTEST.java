package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static com.oceanbase.odc.service.connection.model.ConnectType.MYSQL;
import static com.oceanbase.odc.service.connection.model.ConnectType.ORACLE;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_ORACLE;
import static com.oceanbase.odc.service.connection.model.ConnectType.OB_MYSQL;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

/**
 * Test class for OBConsoleDataSourceFactory
 *
 * @author yh263208
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private ConnectionMapper connectionMapper;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private OBConsoleDataSourceFactory testInstance;

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setDialectType(ORACLE);
        String expected = "\"test_user\"";
        assertEquals(expected, testInstance.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("test_password");
        String expected = "test_password";
        assertEquals(expected, testInstance.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> expected = new HashMap<>();
        expected.put("maxAllowedPacket", "64000000");
        expected.put("allowMultiQueries", "true");
        expected.put("connectTimeout", "5000");
        expected.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        expected.put("noDatetimeStringSync", "true");
        expected.put("jdbcCompliantTruncation", "false");
        Map<String, String> actual = testInstance.getJdbcParams(connectionConfig);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String jdbcUrl = testInstance.getJdbcUrl();
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername("test_username");
        dataSource.setPassword("test_password");
        dataSource.setDriverClassName("com.oceanbase.jdbc.Driver");
        Properties properties = new Properties();
        properties.setProperty("allowLoadLocalInfile", "false");
        properties.setProperty("allowUrlInLocalInfile", "false");
        properties.setProperty("allowLoadLocalInfileInPath", "");
        properties.setProperty("autoDeserialize", "false");
        dataSource.setConnectionProperties(properties);
        testInstance.getDataSource().setConnectionProperties(properties);
        assertEquals(dataSource, testInstance.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        DialectType expected = MYSQL;
        connectionConfig.setDialectType(expected);
        assertEquals(expected, testInstance.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String username = "test_username";
        String password = "test_password";
        String host = "test_host";
        int port = 1234;
        String defaultSchema = "test_schema";
        Map<String, String> parameters = new HashMap<>();
        ConnectionConfig cloned = testInstance.deepCopy();
        ConnectionConfig original = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(original.getUsername(), cloned.getUsername());
        assertEquals(original.getPassword(), cloned.getPassword());
        assertEquals(original.getHost(), cloned.getHost());
        assertEquals(original.getPort(), cloned.getPort());
        assertEquals(original.getDefaultSchema(), cloned.getDefaultSchema());
        assertEquals(original.getParameters(), cloned.getParameters());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String oldUsername = "test_username";
        String newUsername = "new_username";
        testInstance.resetUsername((ValueMapper<String>) (s) -> newUsername);
        assertEquals(newUsername, testInstance.getUsername());
        assertEquals(oldUsername, connectionConfig.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String oldPassword = "test_password";
        String newPassword = "new_password";
        testInstance.resetPassword((ValueMapper<String>) (s) -> newPassword);
        assertEquals(newPassword, testInstance.getPassword());
        assertEquals(oldPassword, connectionConfig.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String oldHost = "test_host";
        String newHost = "new_host";
        testInstance.resetHost((ValueMapper<String>) (s) -> newHost);
        assertEquals(newHost, testInstance.getHost());
        assertEquals(oldHost, connectionConfig.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        int oldPort = 1234;
        int newPort = 5678;
        testInstance.resetPort((ValueMapper<Integer>) (i) -> newPort);
        assertEquals(newPort, testInstance.getPort());
        assertEquals(oldPort, connectionConfig.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String oldSchema = "test_schema";
        String newSchema = "new_schema";
        testInstance.resetSchema((ValueMapper<String>) (s) -> newSchema);
        assertEquals(newSchema, testInstance.getDefaultSchema());
        assertEquals(oldSchema, connectionConfig.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> oldParameters = new HashMap<>();
        oldParameters.put("key1", "value1");
        testInstance.resetParameters((ValueMapper<Map<String, String>>) (m) -> oldParameters);
        assertNull(testInstance.getParameters());
        assertEquals(oldParameters, connectionConfig.getJdbcUrlParameters());
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String host = "test_host";
        int port = 1234;
        String defaultSchema = "test_schema";
        Map<String, String> parameters = new HashMap<>();
        String dialectType = OB_MYSQL.toString();
        String expected = "jdbc:oceanspace:mysql://" + host + ":" + port + "/" + defaultSchema + "?" + StringUtils.join(parameters.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList()), "&");
        assertEquals(expected, testInstance.getJdbcUrl());
    }
}