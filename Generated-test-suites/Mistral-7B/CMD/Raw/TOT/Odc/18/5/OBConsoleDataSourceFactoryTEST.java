package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

    private static ConnectionExtensionPoint connectionExtensionPoint;
    private static EventPublisher eventPublisher;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private BasicDataSource basicDataSource;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE);
        eventPublisher = new EventPublisher();
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("scott");
        connectionConfig.setPassword("tiger");
        connectionConfig.setType(ConnectType.OB_ORACLE);
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("scott_schema");
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        SSLFileEntry sslFileEntry = new SSLFileEntry();
        sslFileEntry.setKeyStoreFilePath("path/to/keystore.p12");
        sslFileEntry.setKeyStoreFilePassword("password");
        connectionConfig.setSslFileEntry(sslFileEntry);
        connectionConfig.setSslConfig(sslConfig);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        basicDataSource = new BasicDataSource();
        BasicDataSourceFactory.setDataSourceFactoryInstance(DataSources.getDataSourceFactoryClass());
        basicDataSource.setDriverClassName(connectionExtensionPoint.getDriverClassName());
    }

    @After
    public void tearDown() throws SQLException {
        if (basicDataSource != null) {
            basicDataSource.close();
        }
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionSessionDataSource) dataSource;
        assertTrue(singleConnectionDataSource.getEventPublisher() != null);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:oracle:thin:@//localhost:1521/scott_schema", jdbcUrl);
        String username = dataSourceFactory.getUsername();
        assertEquals("scott", username);
        String password = dataSourceFactory.getPassword();
        assertEquals("tiger", password);
        String dialectType = dataSourceFactory.getDialectType().toString();
        assertEquals(DialectType.OB_ORACLE.toString(), dialectType);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:oracle:thin:@//localhost:1521/scott_schema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = dataSourceFactory.getUsername();
        assertEquals("scott", username);
    }

    @Test
    public void testGetPassword() {
        String password = dataSourceFactory.getPassword();
        assertEquals("tiger", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullConnectionConfig() {
        dataSourceFactory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithEmptyConnectionConfig() {
        ConnectionConfig emptyConnectionConfig = new ConnectionConfig();
        dataSourceFactory = new OBConsoleDataSourceFactory(emptyConnectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullUsername() {
        connectionConfig.setUsername(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithEmptyPassword() {
        connectionConfig.setPassword(StringUtils.EMPTY);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithEmptyHost() {
        connectionConfig.setHost(StringUtils.EMPTY);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNegativePort() {
        connectionConfig.setPort(-1);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullDefaultSchema() {
        connectionConfig.setDefaultSchema(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithEmptyDefaultSchema() {
        connectionConfig.setDefaultSchema(StringUtils.EMPTY);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullConnectionExtensionPoint() {
        connectionExtensionPoint = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullEventPublisher() {
        eventPublisher = null;
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullAutoCommit() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullInitConnection() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithNullConnectionInitializer() {
        List<ConnectionInitializer> initializers = connectionExtensionPoint.getConnectionInitializers();
        initializers.add(null);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, initializers.toArray(new ConnectionInitializer[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDataSourceWithEmptyConnectionInitializerList() {
        List<ConnectionInitializer> emptyInitializers = ConnectionMapper.INSTANCE.createEmptyList();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, emptyInitializers.toArray(new ConnectionInitializer[0]));
    }
}