import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;
    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @InjectMocks
    private OBConsoleDataSourceFactory objUnderTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbcUrl");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("driverClassName");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(asList(connectionInitializer));
        when(connectionExtensionPoint.getName()).thenReturn("connectionExtensionPointName");
        when(connectionInitializer.afterPropertiesSet(any(DataSource.class))).thenReturn(null);
        when(sessionCreatedInitializer.afterPropertiesSet(any())).thenReturn(null);
        when(connectionConfig.getHost()).thenReturn("host");
        when(connectionConfig.getPort()).thenReturn(1234);
        when(connectionConfig.getUsername()).thenReturn("username");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getDefaultSchema()).thenReturn("defaultSchema");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getType()).thenReturn(ConnectType.SHARDING);
        when(connectionConfig.getSslConfig()).thenReturn(new SSLConfig(true, null, null));
        when(connectionConfig.getSslFileEntry()).thenReturn(new SSLFileEntry("filePath", "password"));
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), DialectType.OB_ORACLE)).thenReturn("userOrSchemaString");
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), DialectType.OB_MYSQL)).thenReturn("userOrSchemaStringMySQL");
        when(connectionSessionUtil.getUserOrSchemaString(anyString(), DialectType.ORACLE)).thenReturn("userOrSchemaStringOracle");
    }

    @After
    public void tearDown() {
        reset(connectionExtensionPoint, eventPublisher, connectionInitializer, sessionCreatedInitializer, connectionConfig, connectionSessionUtil);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = objUnderTest.getDataSource();
        assertEquals("jdbcUrl", dataSource.getConnection().getURL());
        assertEquals("driverClassName", dataSource.getDriverClassName());
        assertEquals("username", dataSource.getUsername());
        assertEquals("password", dataSource.getPassword());
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("host"), eq(1234), eq("defaultSchema"), anyMap());
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verify(connectionInitializer, times(1)).afterPropertiesSet(eq(dataSource));
        verify(sessionCreatedInitializer, times(1)).afterPropertiesSet(eq(objUnderTest));
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = objUnderTest.getJdbcUrl();
        assertEquals("jdbcUrl", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = objUnderTest.getUsername();
        assertEquals("username", username);
    }

    @Test
    public void testGetPassword() {
        String password = objUnderTest.getPassword();
        assertEquals("password", password);
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = objUnderTest.getConnectType();
        assertEquals(ConnectType.SHARDING, connectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory clonedObject = objUnderTest.deepCopy();
        assertEquals("connectionExtensionPointName", clonedObject.getConnectionExtensionPoint().getName());
        assertEquals(eventPublisher, clonedObject.getEventPublisher());
        assertEquals(connectionConfig, clonedObject.getConnectionConfig());
        assertEquals(autoCommit, clonedObject.isAutoCommit());
        assertEquals(true, clonedObject.isInitConnection());
        assertEquals("username", clonedObject.getUsername());
        assertEquals("password", clonedObject.getPassword());
        assertEquals("host", clonedObject.getHost());
        assertEquals(1234, clonedObject.getPort());
        assertEquals("defaultSchema", clonedObject.getDefaultSchema());
        assertEquals(new HashMap<>(), clonedObject.getParameters());
        assertEquals(DialectType.OB_MYSQL, clonedObject.getDialectType());
        assertNotSame(objUnderTest, clonedObject);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        objUnderTest.resetUsername(argThat(mapper -> mapper.map("username").equals(newUsername)));
        assertEquals(newUsername, objUnderTest.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        objUnderTest.resetPassword(argThat(mapper -> mapper.map("password").equals(newPassword)));
        assertEquals(newPassword, objUnderTest.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        objUnderTest.resetHost(argThat(mapper -> mapper.map("host").equals(newHost)));
        assertEquals(newHost, objUnderTest.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 5678;
        objUnderTest.resetPort(argThat(mapper -> mapper.map(1234).equals(newPort)));
        assertEquals(newPort, objUnderTest.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        objUnderTest.resetSchema(argThat(mapper -> mapper.map("defaultSchema").equals(newSchema)));
        assertEquals(newSchema, objUnderTest.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        objUnderTest.resetParameters(argThat(mapper -> mapper.map(new HashMap<>()).equals(newParameters)));
        assertEquals(newParameters, objUnderTest.getParameters());
    }

}