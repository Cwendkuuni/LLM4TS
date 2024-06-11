package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import org.apache.commons.lang.validate;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test");
        connectionConfig.setOBTenantName("tenant");
        connectionConfig.setClusterName("cluster");

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertEquals("Expected and actual username do not match", "test@tenant#cluster", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");

        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);

        assertEquals("Password do not match", "password", password);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        Boolean autoCommit = null;

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);

        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDataSourceProperties() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");
        connectionConfig.setUsername("username");
        Boolean autoCommit = null;

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);

        DataSource dataSource = dataSourceFactory.getDataSource();

        Properties properties = dataSource.getConnectionProperties();

        assertEquals("Property not match", "false", properties.getProperty("allowLoadLocalInfile"));
        assertEquals("Property not match", "false", properties.getProperty("allowUrlInLocalInfile"));
        assertEquals("Property not match", "", properties.getProperty("allowLoadLocalInfileInPath"));
        assertEquals("Property not match", "false", properties.getProperty("autoDeserialize"));
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setJdbcUrlParameters(Map.<String, String>of());

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);

        connectionConfig.setSslConfig(sslConfig);

        Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);

        assertFalse("Property not found", jdbcParams.containsKey("noDatetimeStringSync"));
        assertTrue("SSL not enabled", jdbcParams.containsKey("useSSL"));
    }

    // Add more tests here to validate every methods in the provided OBConsoleDataSourceFactory class.
}