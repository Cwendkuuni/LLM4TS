import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig mockConnectionConfig;
    @Mock
    private ConnectionExtensionPoint mockConnectionExtensionPoint;
    @Mock
    private EventPublisher mockEventPublisher;

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obConsoleDataSourceFactory = spy(new OBConsoleDataSourceFactory(mockConnectionConfig, true, true));
        obConsoleDataSourceFactory.setEventPublisher(mockEventPublisher);
    }

    @Test
    public void testGetDataSource() {
        when(mockConnectionConfig.getHost()).thenReturn("localhost");
        when(mockConnectionConfig.getPort()).thenReturn(3306);
        when(mockConnectionConfig.getDefaultSchema()).thenReturn("test");
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        
        assertEquals(ConnectType.CONSOLE, obConsoleDataSourceFactory.getConnectType());
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
        assertEquals("jdbc:mysql://localhost:3306/test", obConsoleDataSourceFactory.getJdbcUrl());
        verify(mockEventPublisher).publishEvent("DataSource created");
    }

    @Test
    public void testGetUsername() {
        when(mockConnectionConfig.getUsername()).thenReturn("user");
        when(mockConnectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        assertEquals("\"user\"", OBConsoleDataSourceFactory.getUsername(mockConnectionConfig));
    }

    @Test
    public void testGetPassword() {
        when(mockConnectionConfig.getPassword()).thenReturn("password");
        assertEquals("password", OBConsoleDataSourceFactory.getPassword(mockConnectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        when(mockConnectionConfig.getHost()).thenReturn("localhost");
        when(mockConnectionConfig.getSslConfig()).thenReturn(null);
        when(mockConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        Map<String, String> expectedParams = new HashMap<>();
        expectedParams.put("maxAllowedPacket", "64000000");
        expectedParams.put("allowMultiQueries", "true");
        expectedParams.put("connectTimeout", "5000");
        expectedParams.put("zeroDateTimeBehavior", "convertToNull");
        expectedParams.put("noDatetimeStringSync", "true");
        expectedParams.put("sendConnectionAttributes", "false");
        expectedParams.put("useSSL", "false");
        expectedParams.put("allowLoadLocalInfile", "false");
        expectedParams.put("allowUrlInLocalInfile", "false");
        expectedParams.put("allowLoadLocalInfileInPath", "");
        expectedParams.put("autoDeserialize", "false");
        assertEquals(expectedParams, OBConsoleDataSourceFactory.getJdbcParams(mockConnectionConfig));
    }

    @Test
    public void testGetDialectType() {
        when(mockConnectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(obConsoleDataSourceFactory, clonedFactory);
        assertEquals(obConsoleDataSourceFactory.getConnectionConfig(), clonedFactory.getConnectionConfig());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 1234);
        assertEquals(1234, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("key1", "value1");
        obConsoleDataSourceFactory.resetParameters(value -> newParams);
        assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
    }
}