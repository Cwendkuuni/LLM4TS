import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUsername");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("testHost");
        connectionConfig.setPort(12345);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        connectionConfig.setType(ConnectType.OB_MYSQL);

        eventPublisher = mock(EventPublisher.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ConnectionInitializer[0]);
        when(connectionExtensionPoint.getConnectionInitializers())
                .thenReturn(new ConnectionInitializer[0]);
        when(connectionExtensionPoint.getDriverClassName())
                .thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPoint.generateJdbcUrl(eq("testHost"), anyInt(), anyString(), anyMap()))
                .thenReturn("jdbc:mysql://testHost:12345/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                        + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertEquals("com.mysql.jdbc.Driver", dataSource.getConnection().getDriverManager().getDriverName());
        assertEquals("testUsername", dataSource.getUsername());
        assertEquals("jdbc:mysql://testHost:12345/testSchema", dataSource.getUrl());
        assertEquals(connectionConfig.getType(), dataSource.getDialectType());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://testHost:12345/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        assertEquals("testUsername", username);
    }

    @Test
    public void testGetPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
        assertEquals(ConnectType.OB_MYSQL, connectType);
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory clonedDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
        assertEquals(connectionConfig, clonedDataSourceFactory.connectionConfig);
        assertEquals(obConsoleDataSourceFactory.username, clonedDataSourceFactory.username);
        assertEquals(obConsoleDataSourceFactory.password, clonedDataSourceFactory.password);
        assertEquals(obConsoleDataSourceFactory.host, clonedDataSourceFactory.host);
        assertEquals(obConsoleDataSourceFactory.port, clonedDataSourceFactory.port);
        assertEquals(obConsoleDataSourceFactory.defaultSchema, clonedDataSourceFactory.defaultSchema);
        assertEquals(obConsoleDataSourceFactory.parameters, clonedDataSourceFactory.parameters);
        assertEquals(obConsoleDataSourceFactory.connectionExtensionPoint, clonedDataSourceFactory.connectionExtensionPoint);
        assertEquals(obConsoleDataSourceFactory.autoCommit, clonedDataSourceFactory.autoCommit);
        assertEquals(obConsoleDataSourceFactory.initConnection, clonedDataSourceFactory.initConnection);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        obConsoleDataSourceFactory.resetUsername(arg -> newUsername);
        assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        obConsoleDataSourceFactory.resetPassword(arg -> newPassword);
        assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        obConsoleDataSourceFactory.resetHost(arg -> newHost);
        assertEquals(newHost, obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 6789;
        obConsoleDataSourceFactory.resetPort(arg -> newPort);
        assertEquals(newPort, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        obConsoleDataSourceFactory.resetSchema(arg -> newSchema);
        assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        obConsoleDataSourceFactory.resetParameters(arg -> newParameters);
        assertEquals(newParameters, obConsoleDataSourceFactory.getParameters());
    }
}