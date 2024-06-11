import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oceansbase:obs://localhost:3306/user@localhost:3306";
        String actualJdbcUrl = factory.getJdbcUrl();
        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource expectedDataSource = new DriverManagerDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/user", "password");
        DataSource actualDataSource = factory.getDataSource();
        Assert.assertEquals(expectedDataSource, actualDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_MYSQL;
        DialectType actualDialectType = factory.getDialectType();
        Assert.assertEquals(expectedDialectType, actualDialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionConfig() {
        factory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, null);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newUsername).when(mapper).map(factory.getUsername());
        factory.resetUsername(mapper);
        Assert.assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newPassword).when(mapper).map(factory.getPassword());
        factory.resetPassword(mapper);
        Assert.assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newHost).when(mapper).map(factory.getHost());
        factory.resetHost(mapper);
        Assert.assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newPort).when(mapper).map(factory.getPort());
        factory.resetPort(mapper);
        Assert.assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newSchema).when(mapper).map(factory.getDefaultSchema());
        factory.resetSchema(mapper);
        Assert.assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        Mockito.doReturn(newParameters).when(mapper).map(factory.getParameters());
        factory.resetParameters(mapper);
        Assert.assertEquals(newParameters, factory.getParameters());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "user@localhost:3306";
        Assert.assertEquals(expectedUsername, factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";
        Assert.assertEquals(expectedPassword, factory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        ConnectType expectedConnectType = ConnectType.OB;
        Assert.assertEquals(expectedConnectType, factory.getConnectType());
    }

    @Test
    public void testGetDefaultSchema() {
        Assert.assertNull(factory.getDefaultSchema());
    }

    @Test
    public void testGetDataSourceWithInitConnectionTrue() throws SQLException {
        DataSource expectedDataSource = new SingleConnectionDataSource(true);
        DataSource actualDataSource = factory.getDataSource();
        Assert.assertEquals(expectedDataSource, actualDataSource);
        verify(connectionExtensionPoint).getConnectionInitializers();
    }

    @Test
    public void testGetDataSourceWithInitConnectionFalse() throws SQLException {
        DataSource expectedDataSource = new BasicDataSource();
        DataSource actualDataSource = factory.getDataSource();
        Assert.assertEquals(expectedDataSource, actualDataSource);
        verify(connectionExtensionPoint, Mockito.never()).getConnectionInitializers();
    }
}