package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setType(ConnectType.MYSQL); // for test
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        connectionConfig.setEndpoint(new OBTenantEndpoint());

        connectionExtensionPoint = ConnectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("jdbc:oracle:thin:@"));
        assertTrue(jdbcUrl.contains("test_schema"));
        assertTrue(jdbcUrl.contains("testUser"));
    }

    @Test
    public void testGetDataSource() throws SQLException, Exception {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals(dataSource.getUsername(), connectionConfig.getUsername());
        assertEquals(dataSource.getPassword(), connectionConfig.getPassword());
        assertEquals(dataSource.getURL().toString(), dataSourceFactory.getJdbcUrl());

        ConnectionInitializer initializer = connectionExtensionPoint.getConnectionInitializers().get(0);
        List<String> initSqls = initializer.getInitSqls();
        assertFalse(initSqls.isEmpty());
        assertEquals(initSqls.get(0), "SET SESSION time_zone='+00:00';");
    }

    @Test
    public void testGetDialectType() {
        assertEquals(dataSourceFactory.getDialectType(), DialectType.OB_ORACLE);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals(clonedDataSourceFactory.getUsername(), dataSourceFactory.getUsername());
        assertEquals(clonedDataSourceFactory.getPassword(), dataSourceFactory.getPassword());
        assertEquals(clonedDataSourceFactory.getJdbcUrl(), dataSourceFactory.getJdbcUrl());
        assertNotSame(clonedDataSourceFactory, dataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newTestUser";
        dataSourceFactory.resetUsername(new StringMapper<String>() {
            @Override
            public String map(String original) {
                return newUsername;
            }
        });
        assertEquals(dataSourceFactory.getUsername(), newUsername);
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newTestPassword";
        dataSourceFactory.resetPassword(new StringMapper<String>() {
            @Override
            public String map(String original) {
                return newPassword;
            }
        });
        assertEquals(dataSourceFactory.getPassword(), newPassword);
    }
}

class StringMapper<T> implements CloneableDataSourceFactory.ValueMapper<T> {
    private T value;

    @Override
    public T map(T original) {
        value = original;
        return original;
    }

    @Override
    public T getMappedValue() {
        return value;
    }

    @Override
    public void resetMappedValue() {
        value = null;
    }
}