package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
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

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setInitConnection(true);
        connectionConfig.setEventPublisher(new EventPublisher() {
            @Override
            public void publishEvent(String eventName, Object eventData) {
                // empty implementation
            }
        });
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetDataSource() throws SQLException {
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals(connectionConfig.getUsername(), dataSource.getUsername());
        assertEquals(connectionConfig.getPassword(), dataSource.getPassword());
        assertEquals(connectionConfig.getDialectType(), dataSource.getDialectType());
        assertEquals(connectionConfig.getDriverClassName(), dataSource.getDriverClassName());
        assertEquals(connectionConfig.getUrl(), dataSource.getUrl());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?useSSL=true&trustServerCertificate=true&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=" + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals("root", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("test_schema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testGetParameters() {
        Map<String, String> parameters = dataSourceFactory.getParameters();
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("maxAllowedPacket", "64000000");
        expectedParameters.put("allowMultiQueries", "true");
        expectedParameters.put("connectTimeout", "5000");
        expectedParameters.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        expectedParameters.put("noDatetimeStringSync", "true");
        expectedParameters.put("jdbcCompliantTruncation", "false");
        // TODO: set sendConnectionAttributes while upgrade oceanbase-client to v2.2.10
        expectedParameters.put("sendConnectionAttributes", "false");
        assertEquals(expectedParameters, parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConnectionConfig() {
        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        OBConsoleDataSourceFactory invalidDataSourceFactory = new OBConsoleDataSourceFactory(invalidConnectionConfig, true);
        invalidDataSourceFactory.getDataSource();
        fail("An IllegalArgumentException should have been thrown.");
    }

    @Test
    public void testGetDataSourceWithInitConnection() throws SQLException {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSourceFactory.addInitializer(connectionInitializer);
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(10))");
        connection.close();
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
        assertTrue(connection.createStatement().executeQuery("SELECT COUNT(*) FROM test_table").next());
        connection.close();
    }

    @Test
    public void testGetDataSourceWithNoInitConnection() throws SQLException {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, false);
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
        assertFalse(connection.createStatement().executeQuery("SELECT COUNT(*) FROM non_existent_table").next());
        connection.close();
    }

    @Test
    public void testGetDataSourceWithSslConfig() throws SQLException {
        SSLConfig sslConfig = new SSLConfig(true, "/path/to/truststore.p12", "/path/to/keystore.p12");
        connectionConfig.setSslConfig(sslConfig);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
        try {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(10))");
            fail("An SQLException should have been thrown.");
        } catch (SQLException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Unrecognized SSL/TLS message, plaintext connection"));
        }
        connection.close();

        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSource = dataSourceFactory.getDataSource();
        connection = dataSource.getConnection();
        assertTrue(connection.createStatement().executeQuery("SELECT COUNT(*) FROM test_table").next());
        connection.close();
    }

    @Test
    public void testGetDataSourceWithDataSourceFactoryClass() {
        DataSourceFactory dataSourceFactoryInstance = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSourceFactory dataSourceFactoryClass = (DataSourceFactory) dataSourceFactoryInstance.getClass().getDeclaredConstructors()[1].newInstance(connectionConfig, true);
        assertEquals(dataSourceFactoryInstance, dataSourceFactoryClass);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory, clonedDataSourceFactory);
        assertEquals(connectionConfig, clonedDataSourceFactory.getConnectionConfig());
        assertEquals(dataSource, clonedDataSourceFactory.getDataSource());
        assertEquals(connection, clonedDataSourceFactory.getDataSource().getConnection());
    }

    @Test
    public void testResetUsername() throws Exception {
        String updatedUsername = "newUser";
        dataSourceFactory.resetUsername(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return updatedUsername;
            }
        });
        assertEquals(updatedUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() throws Exception {
        String updatedPassword = "newPassword";
        dataSourceFactory.resetPassword(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return updatedPassword;
            }
        });
        assertEquals(updatedPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        String updatedHost = "newHost";
        dataSourceFactory.resetHost(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return updatedHost;
            }
        });
        assertEquals(updatedHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() throws Exception {
        int updatedPort = 3307;
        dataSourceFactory.resetPort(new CloneableDataSourceFactory.ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return updatedPort;
            }
        });
        Validate.isTrue(updatedPort > 0, "Port can not be negative");
        assertEquals(updatedPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() throws Exception {
        String updatedSchema = "newSchema";
        dataSourceFactory.resetSchema(new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return updatedSchema;
            }
        });
        assertEquals(updatedSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> updatedParameters = new HashMap<>();
        updatedParameters.put("maxAllowedPacket", "65535");
        dataSourceFactory.resetParameters(new CloneableDataSourceFactory.ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                return updatedParameters;
            }
        });
        assertEquals(updatedParameters, dataSourceFactory.getParameters());
    }
}