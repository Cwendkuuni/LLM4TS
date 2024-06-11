import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.plugin.connect.api.ConnectionInitializerStub;
import com.oceanbase.odc.plugin.connect.api.ConnectionPluginUtilStub;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private ConnectionInitializerStub connectionInitializerStub;
    @Mock
    private ConnectionPluginUtilStub connectionPluginUtilStub;
    @Mock
    private SessionCreatedInitializer sessionCreatedInitializer;

    private OBConsoleDataSourceFactory testSubject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(
                ConnectionUtils.asList(connectionInitializerStub));
        when(connectionExtensionPoint.getConnectionPluginUtil()).thenReturn(connectionPluginUtilStub);
        when(connectionPluginUtilStub.getDriverClassName()).thenReturn("com.oceanbase.jdbc.Driver");
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(3306);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setDefaultSchema("test_schema");
        config.setDialectType(DialectType.OB_MYSQL);
        config.setType(ConnectType.OB_DEFAULT);
        config.setSslConfig(new SSLConfig(true, null, null));
        testSubject = new OBConsoleDataSourceFactory(config, true, true);
    }

    @After
    public void teardown() {
        testSubject = null;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource dataSource = testSubject.getDataSource();
        Connection connection = dataSource.getConnection();
        assertEquals("testuser", connection.getUserInfo().getUserName());
        assertEquals("localhost:3306/test_schema", dataSource.getUrl());
        verify(eventPublisher, times(1)).publish(any());
        verify(connectionInitializerStub, times(1)).initializeDatabase(any(Connection.class));
        verifyNoMoreInteractions(connectionInitializerStub);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = testSubject.getJdbcUrl();
        assertTrue(jdbcUrl.contains("jdbc:obnew:observer:"));
        assertTrue(jdbcUrl.contains("testuser"));
        assertTrue(jdbcUrl.contains("test_schema"));
        assertTrue(jdbcUrl.contains("3306"));
        assertTrue(jdbcUrl.contains("allowLoadLocalInfile=false"));
        assertTrue(jdbcUrl.contains("useSSL=true"));
        assertTrue(jdbcUrl.contains("trustServerCertificate=true"));
    }

    @Test
    public void testResetValues() {
        ConnectionConfig config = new ConnectionConfig();
        config.setHost("localhost");
        config.setPort(3306);
        config.setUsername("testuser");
        config.setPassword("testpassword");
        config.setDefaultSchema("test_schema");
        config.setDialectType(DialectType.OB_MYSQL);
        config.setType(ConnectType.OB_DEFAULT);
        config.setSslConfig(new SSLConfig(true, null, null));
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, true);

        factory.resetUsername(ck -> ck.map("testuser1"));
        assertEquals("testuser1", factory.getUsername());

        factory.resetPassword(ck -> ck.map("testpassword1"));
        assertEquals("testpassword1", factory.getPassword());

        factory.resetHost(ck -> ck.map("localhost1"));
        assertEquals("localhost1", factory.getHost());

        factory.resetPort(ck -> ck.map(12345));
        assertEquals(12345, factory.getPort());

        factory.resetSchema(ck -> ck.map("test_schema1"));
        assertEquals("test_schema1", factory.getDefaultSchema());

        factory.resetParameters(ck -> ck.map(new HashMap<String, String>() {{
            put("test", "test1");
        }}));
        Map<String, String> params = factory.getParameters();
        assertEquals("test1", params.get("test"));
    }

}