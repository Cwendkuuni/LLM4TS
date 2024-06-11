package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
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
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.ConnectionMapper;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

/**
 * Test class for OBConsoleDataSourceFactory
 */
public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource actualDataSource = dataSourceFactory.getDataSource();
        assertEquals(dataSource.getConnection().getAutoCommit(), dataSourceFactory.isAutoCommit());
        assertEquals(dataSource.getConnection().getUsername(), dataSourceFactory.getUsername());
        assertEquals(dataSource.getConnection().getCatalog(), dataSourceFactory.getDefaultSchema());
        assertTrue(dataSourceFactory.getDialectType() == DialectType.OB_MYSQL);
        assertNotNull(actualDataSource.getConnection());
        assertTrue(actualDataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        assertEquals(dataSourceFactory.getUsername(), deepCopy.getUsername());
        assertEquals(dataSourceFactory.getDefaultSchema(), deepCopy.getDefaultSchema());
        assertEquals(dataSourceFactory.getJdbcUrl(), deepCopy.getJdbcUrl());
        assertEquals(dataSourceFactory.getDialectType(), deepCopy.getDialectType());
        assertEquals(dataSourceFactory.isAutoCommit(), deepCopy.isAutoCommit());
        assertEquals(dataSourceFactory.getInitConnection(), deepCopy.getInitConnection());
    }

    @Test
    public void testResetUsername() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> value + "_modified";
        deepCopy.resetUsername(mapper);
        assertEquals("testUser_modified", deepCopy.getUsername());
    }

    @Test
    public void testResetPassword() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> value + "_modified";
        deepCopy.resetPassword(mapper);
        assertEquals("testPassword_modified", deepCopy.getPassword());
    }

    @Test
    public void testResetHost() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> value + "_modified";
        deepCopy.resetHost(mapper);
        assertEquals("localhost_modified", deepCopy.getHost());
    }

    @Test
    public void testResetPort() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> value * 2;
        deepCopy.resetPort(mapper);
        assertEquals(6732, ((Number) deepCopy.getPort()).intValue());
    }

    @Test
    public void testResetSchema() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> value + "_modified";
        deepCopy.resetSchema(mapper);
        assertEquals("testSchema_modified", deepCopy.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        DataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        Map<String, String> oldParams = deepCopy.getParameters();
        Map<String, String> newParams = new HashMap<>();
        newParams.put("myKey", "myValue");
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newParams;
        deepCopy.resetParameters(mapper);
        assertNotSame(oldParams, deepCopy.getParameters());
        assertEquals("myValue", deepCopy.getParameters().get("myKey"));
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.OB_MYSQL, dataSourceFactory.getConnectType());
    }
}