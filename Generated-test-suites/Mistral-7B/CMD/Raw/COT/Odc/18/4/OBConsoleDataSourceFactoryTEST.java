import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionSessionUtil connectionSessionUtil;
    @Mock
    private ConnectionMapper connectionMapper;
    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private OBConsoleDataSourceFactory testSubject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(3306);
        when(spyConnectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        when(connectionExtensionPoint.generateJdbcUrl(eq("localhost"), eq(3306), eq("test_schema"), anyMap()))
                .thenReturn("jdbc:mysql://localhost:3306/test_schema");

        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(connectionExtensionPoint.getConnectionInitializers().get(0)).thenReturn(mock(ConnectionInitializer.class));

        when(eventPublisher.publishEvent(any())).thenReturn(true);

        DataSource dataSource = testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true)
                .getDataSource();

        assertEquals(spyConnectionConfig.getType(), testSubject.getDialectType());
        assertNotNull(dataSource);
        assertEquals("com.mysql.jdbc.Driver", dataSource.getConnection().getDriverInfoString().split(" ")[0]);
        assertSame(spyConnectionConfig.getUsername(), dataSource.getUsername());
        assertSame(spyConnectionConfig.getPassword(), dataSource.getPassword());
        assertSame(spyConnectionConfig.getHost(), dataSource.getConnection().getConnectionProperties().getProperty("url"));
        assertEquals(3306, dataSource.getConnection().getConnectionProperties().getIntProperty("portNumber"));
        assertEquals("test_schema", dataSource.getConnection().getMetaData().getUserName());
        assertEquals("test_schema", dataSource.getConnection().getMetaData().getDatabaseProductName());

        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_ORACLE);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(1521);
        when(spyConnectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        when(connectionExtensionPoint.generateJdbcUrl(eq("localhost"), eq(1521), eq("test_schema"), anyMap()))
                .thenReturn("jdbc:oracle:thin:@localhost:1521:test_schema");

        String jdbcUrl = testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true)
                .getJdbcUrl();

        assertEquals("jdbc:oracle:thin:@localhost:1521:test_schema", jdbcUrl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        testSubject.new OBConsoleDataSourceFactory(null, true, true).getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullDriverClassName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(3306);
        when(spyConnectionConfig.getDefaultSchema())
                .thenReturn(StringUtils.EMPTY);
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        when(connectionExtensionPoint.getDriverClassName()).thenReturn(null);

        testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true).getDataSource();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithFailedConnection() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(3306);
        when(spyConnectionConfig.getDefaultSchema())
                .thenReturn(StringUtils.EMPTY);
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        doThrow(SQLException.class).when(connectionExtensionPoint)
                .getDriverClassName();

        testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true).getDataSource();
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_ORACLE);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(1521);
        when(spyConnectionConfig.getDefaultSchema())
                .thenReturn(StringUtils.EMPTY);
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        String username = testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true)
                .getUsername();

        assertEquals("test_user", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_ORACLE);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(spyConnectionConfig.getHost()).thenReturn("localhost");
        when(spyConnectionConfig.getPort()).thenReturn(1521);
        when(spyConnectionConfig.getDefaultSchema())
                .thenReturn(StringUtils.EMPTY);
        when(spyConnectionConfig.getUsername()).thenReturn("test_user");
        when(spyConnectionConfig.getPassword()).thenReturn("test_password");
        when(spyConnectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(spyConnectionConfig.getSslConfig()).thenReturn(new SSLConfig());

        String password = testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true)
                .getPassword();

        assertEquals("test_password", password);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        ConnectionConfig spyConnectionConfig = spy(connectionConfig);

        when(spyConnectionConfig.getType()).thenReturn(ConnectType.OB_MYSQL);
        when(spyConnectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        DialectType dialectType = testSubject.new OBConsoleDataSourceFactory(spyConnectionConfig, true, true)
                .getDialectType();

        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

}