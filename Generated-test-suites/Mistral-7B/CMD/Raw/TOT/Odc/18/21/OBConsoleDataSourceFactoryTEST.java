package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    private DataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test_user");
        connectionConfig.setPassword("test_password");
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setJdbcUrlParameters(new Properties());
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("jdbc:mysql://localhost:3306/test_schema"));
    }

    @Test
    public void testGetDataSourceWithSsl() {
        ConnectionConfig sslConfig = new ConnectionConfig();
        sslConfig.setType(ConnectType.OB_MYSQL);
        sslConfig.setDialectType(DialectType.OB_MYSQL);
        sslConfig.setHost("localhost");
        sslConfig.setPort(3306);
        sslConfig.setUsername("test_user_ssl");
        sslConfig.setPassword("test_password_ssl");
        sslConfig.setDefaultSchema("test_schema_ssl");
        sslConfig.setJdbcUrlParameters(new Properties());
        SSLConfig ssl = new SSLConfig();
        ssl.setEnabled(true);
        sslConfig.setSslConfig(ssl);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("/path/to/keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        sslConfig.setSslFileEntry(sslFileEntry);
        DataSourceFactory dataSourceFactoryWithSsl = new OBConsoleDataSourceFactory(sslConfig, false, true);
        DataSource dataSourceWithSsl = dataSourceFactoryWithSsl.getDataSource();
        assertNotNull(dataSourceWithSsl);
        assertTrue(dataSourceWithSsl instanceof SingleConnectionDataSource);
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidConnection() {
        ConnectionConfig invalidConnectionConfig = new ConnectionConfig();
        invalidConnectionConfig.setType(ConnectType.OB_MYSQL);
        invalidConnectionConfig.setDialectType(DialectType.OB_MYSQL);
        invalidConnectionConfig.setHost("localhost");
        invalidConnectionConfig.setPort(-1);
        invalidConnectionConfig.setUsername("test_user");
        invalidConnectionConfig.setPassword("test_password");
        invalidConnectionConfig.setDefaultSchema("test_schema");
        invalidConnectionConfig.setJdbcUrlParameters(new Properties());
        DataSourceFactory dataSourceFactoryWithInvalidConnection = new OBConsoleDataSourceFactory(invalidConnectionConfig, false, true);
        DataSource dataSourceWithInvalidConnection = dataSourceFactoryWithInvalidConnection.getDataSource();
    }

    @After
    public void tearDown() {
        if (dataSourceFactory != null) {
            ((SingleConnectionDataSource) dataSourceFactory.getDataSource()).destroy();
        }
    }
}