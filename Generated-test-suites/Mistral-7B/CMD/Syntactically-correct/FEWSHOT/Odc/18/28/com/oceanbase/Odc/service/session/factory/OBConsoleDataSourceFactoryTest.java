package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.LONG_CONNECTION);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        this.connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(connectionConfig.getDialectType());
        this.eventPublisher = new EventPublisher();
        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @After
    public void tearDown() {
        this.dataSourceFactory = null;
        this.connectionExtensionPoint = null;
        this.eventPublisher = null;
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = this.dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = this.dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testClone() {
        CloneableDataSourceFactory clonedDataSourceFactory = this.dataSourceFactory.deepCopy();
        assertEquals(this.dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(this.dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(this.dataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(this.dataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(this.dataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(this.dataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        assertEquals(this.dataSourceFactory.isAutoCommit(), clonedDataSourceFactory.isAutoCommit());
        assertEquals(this.dataSourceFactory.isInitConnection(), clonedDataSourceFactory.isInitConnection());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        this.dataSourceFactory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newUsername;
            }
        });
        assertEquals(newUsername, this.dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        this.dataSourceFactory.resetPassword(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newPassword;
            }
        });
        assertEquals(newPassword, this.dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        this.dataSourceFactory.resetHost(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newHost;
            }
        });
        assertEquals(newHost, this.dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer newPort = 1234;
        this.dataSourceFactory.resetPort(new CloneableDataSourceFactory.ValueMapper<Integer>() {
            @Override
            public Integer map(Integer oldValue) {
                return newPort;
            }
        });
        Validate.isTrue(newPort > 0, "Port can not be negative");
        assertEquals(newPort, this.dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        this.dataSourceFactory.resetSchema(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newSchema;
            }
        });
        assertEquals(newSchema, this.dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        this.dataSourceFactory.resetParameters(new CloneableDataSourceFactory.ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> oldValue) {
                return newParameters;
            }
        });
        assertEquals(newParameters, this.dataSourceFactory.getParameters());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = this.dataSourceFactory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        String password = this.dataSourceFactory.getPassword();
        assertNull(password);
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = this.dataSourceFactory.getConnectType();
        assertEquals(ConnectType.LONG_CONNECTION, connectType);
    }

    @Test
    public void testGetDefaultSchema() {
        String defaultSchema = this.dataSourceFactory.getDefaultSchema();
        assertEquals("testSchema", defaultSchema);
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = this.dataSourceFactory.getParameters();
        assertNotNull(parameters);
        assertEquals(0, parameters.size());
    }

    @Test
    public void testGetConnectionConfig() {
        ConnectionConfig connectionConfig = this.dataSourceFactory.getConnectionConfig();
        assertNotNull(connectionConfig);
    }

    @Test
    public void testIsAutoCommit() {
        boolean autoCommit = this.dataSourceFactory.isAutoCommit();
        assertFalse(autoCommit);
    }

    @Test
    public void testIsInitConnection() {
        boolean initConnection = this.dataSourceFactory.isInitConnection();
        assertTrue(initConnection);
    }

    @Test
    public void testGenerateJdbcUrl() {
        String dialectType = this.connectionExtensionPoint.getDialectType();
        String jdbcUrl = this.connectionExtensionPoint.generateJdbcUrl(
                "localhost",
                3306,
                "testSchema",
                this.dataSourceFactory.getParameters());
        assertEquals("jdbc:" + dialectType + "://localhost:3306/testSchema", jdbcUrl);
    }

    @Test
    public void testGetDriverClassName() {
        String driverClassName = this.connectionExtensionPoint.getDriverClassName();
        assertNotNull(driverClassName);
    }

    @Test
    public void testGetConnectionInitializers() {
        List<ConnectionInitializer> connectionInitializers = this.connectionExtensionPoint.getConnectionInitializers();
        assertNotNull(connectionInitializers);
        assertTrue(connectionInitializers.isEmpty());
    }
}