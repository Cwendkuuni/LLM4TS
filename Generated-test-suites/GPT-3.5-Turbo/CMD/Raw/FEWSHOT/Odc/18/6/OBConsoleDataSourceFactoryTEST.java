package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        
        assertEquals("jdbc:mysql://localhost:3306/test_db?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        SingleConnectionDataSource dataSource = (SingleConnectionDataSource) factory.getDataSource();
        
        assertNotNull(dataSource);
        assertEquals("jdbc:mysql://localhost:3306/test_db?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false", dataSource.getUrl());
        assertEquals("root", dataSource.getUsername());
        assertEquals("", dataSource.getPassword());
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();

        assertNotNull(clonedFactory);
        assertEquals(factory.getJdbcUrl(), ((OBConsoleDataSourceFactory) clonedFactory).getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user1");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(String::toUpperCase);

        assertEquals("USER1", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password123");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(String::toUpperCase);

        assertEquals("PASSWORD123", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetHost(String::toUpperCase);

        assertEquals("LOCALHOST", factory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(3306);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(port -> port + 1);

        assertEquals(Integer.valueOf(3307), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("test_db");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(String::toUpperCase);

        assertEquals("TEST_DB", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.addJdbcUrlParameter("key1", "value1");
        connectionConfig.addJdbcUrlParameter("key2", "value2");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetParameters(map -> {
            map.put("key1", "new_value1");
            map.put("key3", "value3");
            return map;
        });

        assertEquals("new_value1", factory.getJdbcParams().get("key1"));
        assertEquals("value2", factory.getJdbcParams().get("key2"));
        assertEquals("value3", factory.getJdbcParams().get("key3"));
    }

}