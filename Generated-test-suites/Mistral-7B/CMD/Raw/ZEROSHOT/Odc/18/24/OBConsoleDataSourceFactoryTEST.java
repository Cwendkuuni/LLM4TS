package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.plugin.ConnectionExtensionPoint;
import com.oceanbase.odc.common.util.ConnectionMapper;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionPluginUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

/**
 * Test class for OBConsoleDataSourceFactory
 *
 * @author yh263208
 * @date 2023-03-14 17:29
 * @see OBConsoleDataSourceFactory
 */
@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SingleConnectionDataSource singleConnectionDataSource;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @InjectMocks
    private OBConsoleDataSourceFactory testObj;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionConfig cloneConfig = mapper.clone(connectionConfig);
        TestOBConsoleDataSourceFactory testObj = new TestOBConsoleDataSourceFactory(connectionConfig, true);
        testObj.setConnectionExtensionPoint(connectionExtensionPoint);
        testObj.setEventPublisher(eventPublisher);
        testObj.setConnectionInitializer(connectionInitializer);
        testObj.setSessionCreatedInitializer(sessionCreatedInitializer);
        testObj.setConnectionPluginUtil(connectionPluginUtil);
        this.testObj = testObj;
    }

    @After
    public void tearDown() {
        testObj = null;
    }

    @Test
    public void testOBConsoleDataSourceFactory_CreateDataSource() throws SQLException {
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:mysql://localhost:3306/testSchema");
        when(singleConnectionDataSource.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(singleConnectionDataSource.getUrl()).thenReturn("jdbc:mysql://localhost:3306/testSchema");
        when(singleConnectionDataSource.getUsername()).thenReturn("testUser");
        when(singleConnectionDataSource.getPassword()).thenReturn("testPassword");
        when(singleConnectionDataSource.isAutoCommit()).thenReturn(true);

        DataSource dataSource = testObj.getDataSource();

        assertEquals("com.mysql.jdbc.Driver", dataSource.getConnection().getDriverInfoString().split(" ")[0]);
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertEquals("jdbc:mysql://localhost:3306/testSchema", dataSource.getConnection().getConnectionURL());
        assertTrue(dataSource.isAutoCommit());

        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testSchema"),
                anyMap());
        verify(singleConnectionDataSource, times(1)).setDriverClassName("com.mysql.jdbc.Driver");
        verify(singleConnectionDataSource, times(1)).setUrl("jdbc:mysql://localhost:3306/testSchema");
        verify(singleConnectionDataSource, times(1)).setUsername("testUser");
        verify(singleConnectionDataSource, times(1)).setPassword("testPassword");
        verify(singleConnectionDataSource, times(1)).setAutoCommit(true);
    }

    @Test
    public void testOBConsoleDataSourceFactory_GetDialectType() {
        assertEquals(DialectType.OB_MYSQL, testObj.getDialectType());
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetUsername() {
        String oldUsername = "testUser";
        testObj.resetUsername(arg -> arg.map(StringUtils::replace("testUser", "newUser")));
        assertEquals("newUser", testObj.getUsername());
        assertEquals("testUser", oldUsername);
    }

    @Test(expected = NullPointerException.class)
    public void testOBConsoleDataSourceFactory_ResetUsername_Null() {
        testObj.resetUsername(null);
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetPassword() {
        String oldPassword = "testPassword";
        testObj.resetPassword(arg -> arg.map(StringUtils::replace("testPassword", "newPassword")));
        assertEquals("newPassword", testObj.getPassword());
        assertEquals("testPassword", oldPassword);
    }

    @Test(expected = NullPointerException.class)
    public void testOBConsoleDataSourceFactory_ResetPassword_Null() {
        testObj.resetPassword(null);
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetHost() {
        String oldHost = "localhost";
        testObj.resetHost(arg -> arg.map(StringUtils::replace("localhost", "newHost")));
        assertEquals("newHost", testObj.getHost());
        assertEquals("localhost", oldHost);
    }

    @Test(expected = NullPointerException.class)
    public void testOBConsoleDataSourceFactory_ResetHost_Null() {
        testObj.resetHost(null);
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetPort() {
        int oldPort = 3306;
        testObj.resetPort(arg -> arg.map(port -> port + 1));
        assertEquals(3307, testObj.getPort());
        assertEquals(3306, oldPort);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOBConsoleDataSourceFactory_ResetPort_Negative() {
        testObj.resetPort(arg -> arg.map(port -> port - 1));
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetSchema() {
        String oldSchema = "testSchema";
        testObj.resetSchema(arg -> arg.map(schema -> schema + "_new"));
        assertEquals("testSchema_new", testObj.getDefaultSchema());
        assertEquals("testSchema", oldSchema);
    }

    @Test(expected = NullPointerException.class)
    public void testOBConsoleDataSourceFactory_ResetSchema_Null() {
        testObj.resetSchema(null);
    }

    @Test
    public void testOBConsoleDataSourceFactory_ResetParameters() {
        Map<String, String> oldParameters = new HashMap<>();
        oldParameters.put("testKey", "testValue");
        testObj.resetParameters(arg -> arg);
        Map<String, String> newParameters = testObj.getParameters();
        assertEquals("testValue", newParameters.get("testKey"));
        assertNull(newParameters.get("testNewKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testOBConsoleDataSourceFactory_ResetParameters_Null() {
        testObj.resetParameters(null);
    }

    @Test
    public void testOBConsoleDataSourceFactory_DeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        ConnectionConfig originalConfig = new ConnectionConfig();
        originalConfig.setType(ConnectType.OB_MYSQL);
        originalConfig.setUsername("testUser");
        originalConfig.setPassword("testPassword");
        originalConfig.setHost("localhost");
        originalConfig.setPort(3306);
        originalConfig.setDefaultSchema("testSchema");
        originalConfig.setSslConfig(new SSLConfig(true, null, null));
        originalConfig.setDialectType(DialectType.OB_MYSQL);
        originalConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionConfig cloneConfig = mapper.clone(originalConfig);
        CloneableDataSourceFactory original = new OBConsoleDataSourceFactory(originalConfig, true);
        CloneableDataSourceFactory cloned = original.deepCopy();
        assertEquals(original, cloned);
    }
}

class TestOBConsoleDataSourceFactory extends OBConsoleDataSourceFactory {

    protected TestOBConsoleDataSourceFactory(ConnectionConfig connectionConfig, boolean autoCommit) {
        super(connectionConfig, autoCommit, true);
    }
}