import static org.junit.Assert.*;
import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVOR;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.DataSourceUtils;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private static final String TEST_USER = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 6306;
    private static final String TEST_SCHEMA = "testSchema";
    private static final String TEST_JDBC_URL = "jdbc:oracle:thin:@" + TEST_HOST + ":" + TEST_PORT + "/" + TEST_SCHEMA;

    private OBConsoleDataSourceFactory dataFactory;
    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername(TEST_USER);
        connectionConfig.setPassword(TEST_PASSWORD);
        connectionConfig.setHost(TEST_HOST);
        connectionConfig.setPort(TEST_PORT);
        connectionConfig.setDefaultSchema(TEST_SCHEMA);
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setInitConnection(true);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));

        dataFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = (SingleConnectionDataSource) dataFactory.getDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);

        EventPublisher mockEventPublisher = new EventPublisher() {};
        dataFactory = new OBConsoleDataSourceFactory(connectionConfig, true, mockEventPublisher);
    }

    @After
    public void teardown() throws SQLException {
        dataSource.destroy();
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataFactory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataFactory.getJdbcUrl();
        assertEquals(TEST_JDBC_URL, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataFactory.getUsername();
        assertEquals(TEST_USER, username);
    }

    @Test
    public void testGetPassword() {
        String password = dataFactory.getPassword();
        assertEquals(TEST_PASSWORD, password);
    }

    @Test
    public void testGetDefaultSchema() {
        String schema = dataFactory.getDefaultSchema();
        assertEquals(TEST_SCHEMA, schema);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullConnectionConfig() {
        new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAutoCommit() {
        new OBConsoleDataSourceFactory(new ConnectionConfig(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullInitConnection() {
        new OBConsoleDataSourceFactory(new ConnectionConfig(), true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEventPublisher() {
        new OBConsoleDataSourceFactory(new ConnectionConfig(), true, new EventPublisher() {});
        new OBConsoleDataSourceFactory(new ConnectionConfig(), true, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceNullConnectionExtensionPoint() {
        ConnectionPluginUtil mockPluginUtil = new ConnectionPluginUtil() {};
        ConnectionExtensionPoint mockExtensionPoint = new ConnectionExtensionPoint() {};
        mockPluginUtil.setConnectionExtension(mockExtensionPoint);
        OBConsoleDataSourceFactory dataFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, mockPluginUtil);

        dataFactory.getDataSource();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSourceInvalidDialectType() {
        ConnectionPluginUtil mockPluginUtil = new ConnectionPluginUtil() {};
        ConnectionExtensionPoint mockExtensionPoint = new ConnectionExtensionPoint() {};
        MockDataSourceFactory dataFactory = new MockDataSourceFactory();
        mockPluginUtil.setConnectionExtension(mockExtensionPoint);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(dataFactory, true, mockPluginUtil);

        factory.getDataSource();
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        dataFactory.resetUsername(s -> s.replaceAll(TEST_USER, newUsername));
        assertEquals(newUsername, dataFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        dataFactory.resetPassword(s -> s.replaceAll(TEST_PASSWORD, newPassword));
        assertEquals(newPassword, dataFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        dataFactory.resetHost(s -> s.replaceAll(TEST_HOST, newHost));
        assertEquals(newHost, dataFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 6307;
        dataFactory.resetPort(i -> i + 1);
        assertEquals(TEST_PORT + 1, dataFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        dataFactory.resetSchema(s -> s.replaceAll(TEST_SCHEMA, newSchema));
        assertEquals(newSchema, dataFactory.getDefaultSchema());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetUsernameNullMapper() {
        dataFactory.resetUsername(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordNullMapper() {
        dataFactory.resetPassword(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHostNullMapper() {
        dataFactory.resetHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortNullMapper() {
        dataFactory.resetPort(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetSchemaNullMapper() {
        dataFactory.resetSchema(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullDataSource() {
        OBConsoleDataSourceFactory dataFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        dataFactory.getDataSource();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceNullConnectionSessionUtil() {
        ConnectionPluginUtil mockPluginUtil = new ConnectionPluginUtil() {};
        ConnectionExtensionPoint mockExtensionPoint = new ConnectionExtensionPoint() {};
        mockPluginUtil.setConnectionExtension(mockExtensionPoint);
        OBConsoleDataSourceFactory dataFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true, mockPluginUtil);

        dataFactory.setConnectionSessionUtil(null);
        dataFactory.getDataSource();
    }

    @Test(expected = NullPointerException.class)
    public void testResetUsernameNullUsername() {
        dataFactory.resetUsername(s -> null);
    }

    @Test(expected = NullPointerException.class)
    public void testResetPasswordNullPassword() {
        dataFactory.resetPassword(s -> null);
    }

    @Test(expected = NullPointerException.class)
    public void testResetHostNullHost() {
        dataFactory.resetHost(s -> null);
    }

    @Test(expected = NullPointerException.class)
    public void testResetPortNullPort() {
        dataFactory.resetPort(i -> Integer.MIN_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testResetSchemaNullSchema() {
        dataFactory.resetSchema(s -> null);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetUsernameInvalidMapper() {
        dataFactory.resetUsername(s -> null);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetPasswordInvalidMapper() {
        dataFactory.resetPassword(s -> null);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetHostInvalidMapper() {
        dataFactory.resetHost(s -> null);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetPortInvalidMapper() {
        dataFactory.resetPort(i -> Integer.MIN_VALUE);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetSchemaInvalidMapper() {
        dataFactory.resetSchema(s -> null);
    }

    @Test
    public void testClone() {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        OBConsoleDataSourceFactory clone = original.deepCopy();

        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getDialectType(), clone.getDialectType());
        assertEquals(original.getAutoCommit(), clone.getAutoCommit());
        assertEquals(original.getInitConnection(), clone.getInitConnection());
    }

    @Test
    public void testCloneWithSameObject() {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        OBConsoleDataSourceFactory clone = original;

        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getDialectType(), clone.getDialectType());
        assertEquals(original.getAutoCommit(), clone.getAutoCommit());
        assertEquals(original.getInitConnection(), clone.getInitConnection());
    }

    @Test
    public void testCloneWithNullObject() {
        OBConsoleDataSourceFactory original = null;
        OBConsoleDataSourceFactory clone = original.deepCopy();

        assertNull(clone);
    }

    @Test
    public void testCloneWithInvalidMapper() {
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        OBConsoleDataSourceFactory clone = new MockDataSourceFactory();
        assertEquals(original.getUsername(), clone.getUsername());
        assertEquals(original.getPassword(), clone.getPassword());
        assertEquals(original.getHost(), clone.getHost());
        assertEquals(original.getPort(), clone.getPort());
        assertEquals(original.getDefaultSchema(), clone.getDefaultSchema());
        assertEquals(original.getDialectType(), clone.getDialectType());
        assertEquals(original.getAutoCommit(), clone.getAutoCommit());
        assertEquals(original.getInitConnection(), clone.getInitConnection());
    }

    static class MockDataSourceFactory implements CloneableDataSourceFactory {
        @Override
        public DataSource getDataSource() {
            throw new UnsupportedOperationException();
        }

        @Override
        public DialectType getDialectType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public CloneableDataSourceFactory deepCopy() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void resetUsername(CloneableDataSourceFactory.ValueMapper<String> mapper) {}

        @Override
        public void resetPassword(CloneableDataSourceFactory.ValueMapper<String> mapper) {}

        @Override
        public void resetHost(CloneableDataSourceFactory.ValueMapper<String> mapper) {}

        @Override
        public void resetPort(CloneableDataSourceFactory.ValueMapper<Integer> mapper) {}

        @Override
        public void resetSchema(CloneableDataSourceFactory.ValueMapper<String> mapper) {}

        @Override
        public void resetUsername(String newUsername) {}

        @Override
        public void resetPassword(String newPassword) {}

        @Override
        public void resetHost(String newHost) {}

        @Override
        public void resetPort(int newPort) {}

        @Override
        public void resetSchema(String newSchema) {}

        @Override
        public String getUsername() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPassword() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getHost() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getPort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getDefaultSchema() {
            throw new UnsupportedOperationException();
        }

        @Override
        public DialectType getDialectType() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Boolean getAutoCommit() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isInitConnection() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ConnectionSessionUtil getConnectionSessionUtil() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setConnectionSessionUtil(ConnectionSessionUtil connectionSessionUtil) {
            throw new UnsupportedOperationException();
        }
    }
}