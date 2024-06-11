import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private EventPublisher eventPublisherMock;

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSourceFactory.setEventPublisher(eventPublisherMock);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
        assertNotNull(copy);
        assertEquals(connectionConfig, copy.connectionConfig);
        assertEquals(true, copy.autoCommit);
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 1234);
        assertEquals(Integer.valueOf(1234), dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        dataSourceFactory.resetParameters(value -> params);
        assertEquals(params, dataSourceFactory.getParameters());
    }

}