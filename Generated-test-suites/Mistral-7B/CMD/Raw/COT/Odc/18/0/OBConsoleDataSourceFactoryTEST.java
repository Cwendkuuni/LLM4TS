import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionExtensionPoint;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory factory;
    private ConnectionExtensionPoint extensionPoint;
    private ConnectionConfig config;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.extensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        this.config = new ConnectionConfig();
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource dataSource = (BasicDataSource) this.factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.factory.getJdbcUrl();
        assertTrue(jdbcUrl.contains(this.config.getHost()));
        assertTrue(jdbcUrl.contains(this.config.getPort() + ""));
        assertTrue(jdbcUrl.contains(this.factory.getUsername()));
        assertTrue(jdbcUrl.contains(this.factory.getDefaultSchema()));
    }

    @Test
    public void testGetUsername() {
        String username = this.factory.getUsername();
        assertTrue(username.contains(getDbUser(this.config)));
        if (this.config.getDialectType().equals(DialectType.OB_ORACLE)) {
            assertTrue(username.contains("\""));
        }
    }

    @Test
    public void testGetPassword() {
        String password = this.factory.getPassword();
        assertTrue(password.length() > 0);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = this.factory.getDialectType();
        assertTrue(dialectType != null);
    }

    @Test
    public void testGetDataSourceWithInitConnection() throws SQLException {
        DataSource dataSource = this.factory.getDataSource();
        SingleConnectionDataSource singleDataSource = (SingleConnectionDataSource) dataSource;
        assertTrue(singleDataSource.isInitConnection());
    }

    @Test
    public void testGetDataSourceWithoutInitConnection() throws SQLException {
        this.factory = new OBConsoleDataSourceFactory(this.config, true, false);
        DataSource dataSource = this.factory.getDataSource();
        SingleConnectionDataSource singleDataSource = (SingleConnectionDataSource) dataSource;
        assertFalse(singleDataSource.isInitConnection());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNegativePort() {
        this.config.setPort(-1);
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullUsername() {
        this.config.setUsername(null);
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyUsername() {
        this.config.setUsername("");
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullHost() {
        this.config.setHost(null);
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyHost() {
        this.config.setHost("");
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullPort() {
        this.config.setPort(null);
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithEmptyDefaultSchema() {
        this.config.setDefaultSchema("");
        this.factory = new OBConsoleDataSourceFactory(this.config, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        this.factory = new OBConsoleDataSourceFactory(null, true, true);
    }

    @After
    public void cleanup() {
        Mockito.reset(this.eventPublisher, this.extensionPoint);
    }

    private static String getDbUser(@NonNull ConnectionConfig connectionConfig) {
        String username = connectionConfig.getUsername();
        if (DialectType.OB_ORACLE.equals(connectionConfig.getDialectType())) {
            return ConnectionSessionUtil.getUserOrSchemaString(username, DialectType.OB_ORACLE);
        }
        return username;
    }
}