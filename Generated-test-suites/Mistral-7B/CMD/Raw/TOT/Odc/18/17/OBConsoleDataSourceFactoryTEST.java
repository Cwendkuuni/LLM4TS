import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionExtensionPoint;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private ConnectionSessionUtil connectionSessionUtil;

    @Before
    public void init() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(6306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("root");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        connectionExtensionPoint = Mockito.mock(ConnectionPluginUtil.class);
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyMap()))
                .thenReturn("jdbc:mock:url");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[]{});

        eventPublisher = Mockito.mock(EventPublisher.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSourceFactory.setEventPublisher(eventPublisher);
        dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

        connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        dataSourceFactory.addInitializer(connectionInitializer);

        connectionSessionUtil = Mockito.mock(ConnectionSessionUtil.class);
        when(connectionSessionUtil.getUserOrSchemaString(Mockito.anyString(), Mockito.any(DialectType.class)))
                .thenReturn("root");
    }

    @Test
    public void testDataSourceFactoryGetDataSource() throws SQLException {
        PoolingDataSource dataSource = dataSourceFactory.getDataSource();
        assertEquals("jdbc:mock:url", dataSource.getConnection().getConnectionURL().toString());
        assertEquals("root", dataSource.getConnection().getUserName());
        assertFalse(dataSource.getConnection().isClosed());
    }

    @Test
    public void testDataSourceFactoryGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDataSourceFactoryCloneableDataSourceFactory() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory, clonedDataSourceFactory);
        clonedDataSourceFactory.resetUsername(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertNull(clonedDataSourceFactory.getUsername());
        assertEquals("root", dataSourceFactory.getUsername());
    }

    @Test
    public void testDataSourceFactoryResetUsername() {
        String newUsername = "new_username";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetUsername(mapper);
        assertEquals(newUsername, mapper.getMapValue());
        assertEquals("root", dataSourceFactory.getUsername());
    }

    @Test
    public void testDataSourceFactoryResetPassword() {
        String newPassword = "new_password";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPassword(mapper);
        assertEquals(newPassword, mapper.getMapValue());
        assertEquals("root", dataSourceFactory.getPassword());
    }

    @Test
    public void testDataSourceFactoryResetHost() {
        String newHost = "new_host";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetHost(mapper);
        assertEquals(newHost, mapper.getMapValue());
        assertEquals("localhost", dataSourceFactory.getHost());
    }

    @Test
    public void testDataSourceFactoryResetPort() {
        int newPort = 3306;
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetPort(mapper);
        assertEquals(newPort, mapper.getMapValue());
        assertEquals(6306, dataSourceFactory.getPort());
    }

    @Test
    public void testDataSourceFactoryResetSchema() {
        String newSchema = "new_schema";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetSchema(mapper);
        assertEquals(newSchema, mapper.getMapValue());
        assertEquals("test_schema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testDataSourceFactoryResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        dataSourceFactory.resetParameters(mapper);
        assertEquals(newParameters, mapper.getMapValue());
        assertEquals(new HashMap<>(), dataSourceFactory.getParameters());
    }

    @Test
    public void testDataSourceFactoryGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mock:url", jdbcUrl);
    }

    @Test
    public void testDataSourceFactoryGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("root", username);
    }

    @Test
    public void testDataSourceFactoryGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("root", password);
    }

    @Test
    public void testDataSourceFactoryGetHost() {
        String host = dataSourceFactory.getHost();
        assertEquals("localhost", host);
    }

    @Test
    public void testDataSourceFactoryGetPort() {
        int port = dataSourceFactory.getPort();
        assertEquals(6306, port);
    }

    @Test
    public void testDataSourceFactoryGetDefaultSchema() {
        String defaultSchema = dataSourceFactory.getDefaultSchema();
        assertEquals("test_schema", defaultSchema);
    }

    @Test
    public void testDataSourceFactoryGetParameters() {
        Map<String, String> parameters = dataSourceFactory.getParameters();
        assertEquals(new HashMap<>(), parameters);
    }

    @Test
    public void testDataSourceFactoryAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSourceFactory.addInitializer(initializer);
        assertTrue(dataSourceFactory.getInitializers().contains(initializer));
    }

    @Test
    public void testDataSourceFactoryRemoveInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSourceFactory.addInitializer(initializer);
        dataSourceFactory.removeInitializer(initializer);
        assertFalse(dataSourceFactory.getInitializers().contains(initializer));
    }
}