
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    private static ConnectionConfig connectionConfig;
    private static OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @BeforeClass
    public static void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setOBTenantName("tenant");
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("pass");

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setProxyHost("localhost");
        endpoint.setProxyPort(9090);
        connectionConfig.setEndpoint(endpoint);

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);

        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("filepath");
        sslFileEntry.setKeyStoreFilePassword("filepass");
        connectionConfig.setSslFileEntry(sslFileEntry);

        Map<String, String> jdbcParameters = new HashMap<>();
        jdbcParameters.put("test_key", "test_value");
        connectionConfig.setJdbcUrlParameters(jdbcParameters);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obConsoleDataSourceFactory.setEventPublisher(new EventPublisher());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        assertTrue(username.contains("user@tenant"));
    }

    @Test
    public void testPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        assertEquals("pass", password);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clone = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clone);
        DataSource dataSource = clone.getDataSource();
        assertNotNull(dataSource);

        // test username after deep copy
        assertEquals(obConsoleDataSourceFactory.getUsername(), ((OBConsoleDataSourceFactory)clone).getUsername());
        // test password after deep copy
        assertEquals(obConsoleDataSourceFactory.getPassword(), ((OBConsoleDataSourceFactory)clone).getPassword());
    }
}
