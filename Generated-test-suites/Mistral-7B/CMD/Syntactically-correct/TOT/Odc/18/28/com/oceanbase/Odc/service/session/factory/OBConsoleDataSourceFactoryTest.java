package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

    private ConnectionExtensionPoint connectionExtensionPoint;
    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_MYSQL);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setInitConnection(true);
        connectionConfig.setEventPublisher(new EventPublisher() {
            @Override
            public void publish(Object event) {
                // do nothing
            }
        });
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetDataSource() {
        Connection connection = dataSource.getConnection();
        assertTrue(connection != null);
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = connectionExtensionPoint.generateJdbcUrl(connectionConfig.getHost(),
                connectionConfig.getPort(), connectionConfig.getDefaultSchema(), dataSourceFactory.getParameters());
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        assertEquals(connectionConfig.getUsername(), dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNull(dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(connectionConfig.getDialectType(), dataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        String newUsername = "new_username";
        dataSourceFactory.resetUsername(new StringMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newUsername;
            }
        });
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "new_password";
        dataSourceFactory.resetPassword(new StringMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newPassword;
            }
        });
        assertNull(dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "new_host";
        dataSourceFactory.resetHost(new StringMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newHost;
            }
        });
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 1234;
        dataSourceFactory.resetPort(new IntegerMapper<Integer>() {
            @Override
            public Integer map(Integer oldValue) {
                return newPort;
            }
        });
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "new_schema";
        dataSourceFactory.resetSchema(new StringMapper<String>() {
            @Override
            public String map(String oldValue) {
                return newSchema;
            }
        });
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key", "value");
        dataSourceFactory.resetParameters(new Mapper<Map<String, String>, Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> oldValue) {
                return newParameters;
            }
        });
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testCloneableDataSourceFactory() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        Connection clonedDataSource = clonedDataSourceFactory.getDataSource().getConnection();
        try {
            assertEquals(dataSource.getConnection().createStatement().executeQuery("SELECT 1").next(),
                    clonedDataSource.getConnection().createStatement().executeQuery("SELECT 1").next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            clonedDataSource.close();
            dataSource.close();
        }
    }

    @Test
    public void testInitialization() throws SQLException {
        Connection connection = dataSource.getConnection();
        ConnectionInitializer initializer = connectionExtensionPoint.getConnectionInitializers().get(0);
        initializer.initializeDatabase(connection);
        connection.close();

        connection = dataSource.getConnection();
        SessionCreatedInitializer sessionInitializer = (SessionCreatedInitializer) dataSource.getInitializers().get(0);
        sessionInitializer.afterPropertiesSet();
        connection.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPassword() {
        ConnectionConfig invalidConfig = new ConnectionConfig();
        invalidConfig.setHost("localhost");
        invalidConfig.setPort(3306);
        invalidConfig.setUsername("root");
        invalidConfig.setPassword("password@");
        invalidConfig.setDefaultSchema("test_schema");
        invalidConfig.setDialectType(DialectType.OB_MYSQL);
        new OBConsoleDataSourceFactory(invalidConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyUsername() {
        ConnectionConfig emptyUsernameConfig = new ConnectionConfig();
        emptyUsernameConfig.setHost("localhost");
        emptyUsernameConfig.setPort(3306);
        emptyUsernameConfig.setUsername("");
        emptyUsernameConfig.setPassword("password");
        emptyUsernameConfig.setDefaultSchema("test_schema");
        emptyUsernameConfig.setDialectType(DialectType.OB_MYSQL);
        new OBConsoleDataSourceFactory(emptyUsernameConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPort() {
        ConnectionConfig invalidPortConfig = new ConnectionConfig();
        invalidPortConfig.setHost("localhost");
        invalidPortConfig.setPort(-1);
        invalidPortConfig.setUsername("root");
        invalidPortConfig.setPassword("password");
        invalidPortConfig.setDefaultSchema("test_schema");
        invalidPortConfig.setDialectType(DialectType.OB_MYSQL);
        new OBConsoleDataSourceFactory(invalidPortConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyHost() {
        ConnectionConfig emptyHostConfig = new ConnectionConfig();
        emptyHostConfig.setHost("");
        emptyHostConfig.setPort(3306);
        emptyHostConfig.setUsername("root");
        emptyHostConfig.setPassword("password");
        emptyHostConfig.setDefaultSchema("test_schema");
        emptyHostConfig.setDialectType(DialectType.OB_MYSQL);
        new OBConsoleDataSourceFactory(emptyHostConfig, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfig() {
        new OBConsoleDataSourceFactory(null, true, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullUsername() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setUsername(null);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullPassword() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setPassword(null);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullHost() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setHost(null);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullPort() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setPort(-1);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullDefaultSchema() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setDefaultSchema(null);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCloneWithNullConnectionConfig() {
        CloneableDataSourceFactory factory = new OBConsoleDataSourceFactory(null, true, true);
        try {
            CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unexpected exception", e);
        } catch (IllegalStateException e) {
            // expected
        }
    }

    public static class StringMapper<T> implements CloneableDataSourceFactory.ValueMapper<T> {
        @Override
        public T map(T oldValue) {
            return (T) "new_value";
        }

        @Override
        public T clone() {
            return this;
        }
    }

    public static class IntegerMapper<T> implements CloneableDataSourceFactory.ValueMapper<T> {
        @Override
        public T map(T oldValue) {
            return (T) 123;
        }

        @Override
        public T clone() {
            return this;
        }
    }

    public static class Mapper<F, T> implements CloneableDataSourceFactory.ValueMapper<T> {
        private F mapper;

        public Mapper(F mapper) {
            this.mapper = mapper;
        }

        @Override
        public T map(T oldValue) {
            return (T) mapper.map(oldValue);
        }

        @Override
        public T clone() {
            return this;
        }
    }

}