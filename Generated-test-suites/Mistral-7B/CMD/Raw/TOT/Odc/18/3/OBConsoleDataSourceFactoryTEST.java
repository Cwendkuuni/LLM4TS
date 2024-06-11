package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory.ValueMapper;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionExtensionPoint connectionExtensionPoint;
    private DataSourceFactory underTest;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_username");
        connectionConfig.setPassword("test_password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        underTest = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = underTest.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testDeepCopy() throws CloneNotSupportedException {
        DataSourceFactory clonedDataSourceFactory = underTest.deepCopy();
        assertNotNull(clonedDataSourceFactory);
        assertFalse(underTest.equals(clonedDataSourceFactory));
        assertTrue(underTest.getDialectType().equals(clonedDataSourceFactory.getDialectType()));
    }

    @Test
    public void testResetProperties() throws CloneNotSupportedException {
        underTest.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return "new_username";
            }
        });
        underTest.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return "new_password";
            }
        });
        underTest.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return "new_host";
            }
        });
        underTest.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return 1234;
            }
        });
        underTest.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return "new_schema";
            }
        });
        underTest.resetParameters(new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                HashMap<String, String> map = new HashMap<>();
                map.put("test", "test_value");
                return map;
            }
        });

        ConnectionFactory clonedDataSourceFactory = underTest.deepCopy();
        assertFalse(underTest.equals(clonedDataSourceFactory));
        assertTrue(underTest.getUsername().equals(clonedDataSourceFactory.getUsername()));
        assertTrue(underTest.getPassword().equals(clonedDataSourceFactory.getPassword()));
        assertTrue(underTest.getHost().equals(clonedDataSourceFactory.getHost()));
        assertTrue(underTest.getPort() == clonedDataSourceFactory.getPort());
        assertTrue(underTest.getDefaultSchema().equals(clonedDataSourceFactory.getDefaultSchema()));
        assertTrue(underTest.getParameters().equals(clonedDataSourceFactory.getParameters()));
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = underTest.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false";
        assertTrue(jdbcUrl.equals(expectedJdbcUrl));
    }

    @Test
    public void testGenerateJdbcUrlWithSsl() {
        ConnectionConfig connectionConfigWithSsl = new ConnectionConfig();
        connectionConfigWithSsl.setUsername("test_username_ssl");
        connectionConfigWithSsl.setPassword("test_password_ssl");
        connectionConfigWithSsl.setHost("localhost");
        connectionConfigWithSsl.setPort(3306);
        connectionConfigWithSsl.setDefaultSchema("test_schema_ssl");
        connectionConfigWithSsl.setDialectType(DialectType.MYSQL);
        connectionConfigWithSsl.setType(ConnectType.MYSQL);
        connectionConfigWithSsl.setJdbcUrlParameters(new HashMap<>());
        SSLConfig sslConfig = new SSLConfig();
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("test_keystore_file.jks");
        sslConfig.setEnabled(true);
        sslConfig.setSslFileEntry(sslFileEntry);
        connectionConfigWithSsl.setSslConfig(sslConfig);

        OBConsoleDataSourceFactory sslDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfigWithSsl, true);
        String jdbcUrlWithSsl = sslDataSourceFactory.getJdbcUrl();

        String expectedJdbcUrlWithSsl = "jdbc:mysql://localhost:3306/test_schema_ssl?allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false&useSSL=true&trustServerCertificate=true&trustStore=test_keystore_file.jks&trustStorePassword=test_password_ssl&keyStore=test_keystore_file.jks&keyStorePassword=test_password_ssl";
        assertTrue(jdbcUrlWithSsl.equals(expectedJdbcUrlWithSsl));
    }

    @Test
    public void testGetDataSourceWithConnectionInitializer() throws SQLException {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        when(connectionInitializer.initializeDatabase()).thenReturn(true);
        connectionExtensionPoint.addConnectionInitializer(connectionInitializer);

        underTest.setInitConnection(true);
        DataSource dataSource = underTest.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        Connection connection = ((SingleConnectionDataSource) dataSource).getConnection();
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)");
        connection.close();
    }

    @Test
    public void testGetDataSourceWithoutConnectionInitializer() throws SQLException {
        DataSource dataSource = underTest.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        Connection connection = ((SingleConnectionDataSource) dataSource).getConnection();
        assertNull(connection.createStatement().execute("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)"));
        connection.close();
    }

    @Test
    public void testGetDataSourceWithConnectionPool() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(ConnectionPluginUtil.getDriverClassName(connectionConfig.getDialectType()));
        dataSource.setUrl(underTest.getJdbcUrl());
        dataSource.setUsername(underTest.getUsername());
        dataSource.setPassword(underTest.getPassword());
        dataSource.setInitialSize(1);
        dataSource.setMaxTotal(10);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();

        DataSource dataSourceFromFactory = underTest.getDataSource();
        assertEquals(dataSource, dataSourceFromFactory);
    }

    private void assertNotNull(Object object) {
        if (object == null) {
            fail("Expected object to not be null.");
        }
    }

    private void assertEquals(Object expectedObject, Object actualObject) {
        if (!expectedObject.equals(actualObject)) {
            fail("Expected object to be equal to actual object but they were not.");
        }
    }

    private void assertNull(Object object) {
        if (object != null) {
            fail("Expected object to be null but it was not.");
        }
    }
}