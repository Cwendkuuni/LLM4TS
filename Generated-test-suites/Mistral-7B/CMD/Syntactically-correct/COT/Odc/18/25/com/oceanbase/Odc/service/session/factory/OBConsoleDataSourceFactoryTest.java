package com.oceanbase.odc.service.session.factory.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
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

import lombok.Getter;
import lombok.Setter;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    @Getter
    @Setter
    private ConnectionConfig connectionConfig;

    @Getter
    @Setter
    private OBConsoleDataSourceFactory objUnderTest;

    @Before
    public void setup() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        this.connectionConfig = new ConnectionConfig(ConnectType.OB_ORACLE, null, "localhost", 1521, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null);
        this.objUnderTest = new OBConsoleDataSourceFactory(this.connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl_default() {
        String expectedJdbcUrl = this.connectionExtensionPoint.generateJdbcUrl("localhost", 1521, null, null);
        String actualJdbcUrl = this.objUnderTest.getJdbcUrl();
        assertTrue(expectedJdbcUrl.equals(actualJdbcUrl));
    }

    @Test
    public void testGetJdbcUrl_withParameters() {
        Map<String, String> jdbcUrlParams = new HashMap<>();
        jdbcUrlParams.put("maxAllowedPacket", "64000000");
        jdbcUrlParams.put("allowMultiQueries", "true");
        jdbcUrlParams.put("connectTimeout", "5000");
        jdbcUrlParams.put("zeroDateTimeBehavior", "CONVERT_TO_NULL");
        jdbcUrlParams.put("noDatetimeStringSync", "true");
        jdbcUrlParams.put("jdbcCompliantTruncation", "false");

        String expectedJdbcUrl = this.connectionExtensionPoint.generateJdbcUrl("localhost", 1521, null, jdbcUrlParams);
        this.objUnderTest.setParameters(jdbcUrlParams);
        String actualJdbcUrl = this.objUnderTest.getJdbcUrl();
        assertTrue(expectedJdbcUrl.equals(actualJdbcUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_nullConnectionConfig() {
        this.objUnderTest = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_nullConnectionExtensionPoint() {
        this.objUnderTest.setConnectionExtensionPoint(null);
        this.objUnderTest.getDataSource();
    }

    @Test
    public void testGetDataSource_initializesConnection() throws SQLException {
        this.objUnderTest.setInitConnection(true);
        this.objUnderTest.setConnectionInitializer(this.connectionInitializer);

        DataSource actualDataSource = this.objUnderTest.getDataSource();
        assertTrue(actualDataSource instanceof SingleConnectionDataSource);
        assertSame(this.connectionExtensionPoint, actualDataSource.getConnectionExtensionPoint());
        assertTrue(this.connectionInitializer.isInitialized(actualDataSource));
    }

    @Test
    public void testGetDataSource_doesNotInitializeConnection() throws SQLException {
        this.objUnderTest.setInitConnection(false);
        DataSource actualDataSource = this.objUnderTest.getDataSource();
        assertFalse(actualDataSource instanceof SingleConnectionDataSource);
        assertNull(actualDataSource.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() {
        String oldUsername = "oldUsername";
        this.objUnderTest.setUsername(oldUsername);

        String newUsername = "newUsername";
        this.objUnderTest.resetUsername(s -> s.replace("oldUsername", "newUsername"));

        assertEquals(newUsername, this.objUnderTest.getUsername());
    }

    @Test
    public void testResetPassword() {
        String oldPassword = "oldPassword";
        this.objUnderTest.setPassword(oldPassword);

        String newPassword = "newPassword";
        this.objUnderTest.resetPassword(s -> s.replace("oldPassword", "newPassword"));

        assertEquals(newPassword, this.objUnderTest.getPassword());
    }

    @Test
    public void testResetHost() {
        String oldHost = "oldHost";
        this.objUnderTest.setHost(oldHost);

        String newHost = "newHost";
        this.objUnderTest.resetHost(s -> s.replace("oldHost", "newHost"));

        assertEquals(newHost, this.objUnderTest.getHost());
    }

    @Test
    public void testResetPort() {
        int oldPort = 1521;
        this.objUnderTest.setPort(oldPort);

        int newPort = 1522;
        this.objUnderTest.resetPort(i -> i + 1);

        assertEquals(newPort, this.objUnderTest.getPort());
    }

    @Test
    public void testResetSchema() {
        String oldSchema = "oldSchema";
        this.objUnderTest.setDefaultSchema(oldSchema);

        String newSchema = "newSchema";
        this.objUnderTest.resetSchema(s -> s.replace("oldSchema", "newSchema"));

        assertEquals(newSchema, this.objUnderTest.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> oldParameters = new HashMap<>();
        oldParameters.put("maxAllowedPacket", "64000000");
        this.objUnderTest.setParameters(oldParameters);

        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("maxAllowedPacket", "128000000");
        this.objUnderTest.resetParameters(m -> m.put("maxAllowedPacket", "128000000"));

        assertEquals(newParameters, this.objUnderTest.getParameters());
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        DialectType actualDialectType = this.objUnderTest.getDialectType();

        assertTrue(expectedDialectType.equals(actualDialectType));
    }

    @Test
    public void testDeepCopy_sameObject() {
        OBConsoleDataSourceFactory original = this.objUnderTest;
        OBConsoleDataSourceFactory clone = original.deepCopy();

        assertEquals(original, clone);
    }

    @Test
    public void testDeepCopy_differentObjects() {
        OBConsoleDataSourceFactory original = this.objUnderTest;
        OBConsoleDataSourceFactory clone = new OBConsoleDataSourceFactory(this.connectionConfig, false);

        OBConsoleDataSourceFactory cloned = original.deepCopy();

        assertNotSame(original, cloned);
        assertNotSame(clone, cloned);
        assertEquals(original, original);
        assertEquals(clone, clone);
    }
}