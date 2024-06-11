package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.DialectType;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(8080);
        connectionConfig.setDefaultSchema("defaultSchema");

        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        connectionConfig.setSslConfig(sslConfig);
        connectionConfig.setEndpoint(new OBTenantEndpoint());

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "username";
        String actualUsername = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password";
        String actualPassword = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(params);
        assertEquals("true", params.get("useSSL"));
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copiedFactory = factory.deepCopy();
        assertNotNull(copiedFactory);
        assertEquals(factory.getConnectType(), copiedFactory.getConnectType());
        assertEquals(factory.getDialectType(), copiedFactory.getDialectType());
        assertEquals(factory.getUsername(), copiedFactory.getUsername());
        assertEquals(factory.getPassword(), copiedFactory.getPassword());
    }
}
