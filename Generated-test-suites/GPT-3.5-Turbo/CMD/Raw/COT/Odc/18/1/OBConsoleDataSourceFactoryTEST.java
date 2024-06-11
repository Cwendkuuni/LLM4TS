import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private Boolean autoCommit;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        autoCommit = true;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testConstructor() {
        assertNotNull(dataSourceFactory);
    }

    @Test
    public void testGetDataSource() {
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        DataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(s -> "test_user");
        assertEquals("test_user", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(s -> "test_password");
        assertEquals("test_password", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(s -> "test_host");
        assertEquals("test_host", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(s -> 9999);
        assertEquals(9999, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(s -> "test_schema");
        assertEquals("test_schema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("test_param", "test_value");
        dataSourceFactory.resetParameters(s -> params);
        assertEquals(params, dataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertNotNull(dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNotNull(dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.DEFAULT, dataSourceFactory.getConnectType());
    }

    // Edge cases and exception handling scenarios to test:
    // 1. Null connectionConfig in constructor
    // 2. Null autoCommit in constructor
    // 3. Null endpoint in getJdbcParams
    // 4. Null sslConfig in getJdbcParams
    // 5. Null sslFileEntry in getJdbcParams
    // 6. Null initializers in getDataSource
    // 7. Negative port in resetPort

}

