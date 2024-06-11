
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;


public class OBConsoleDataSourceFactoryTest {
    ConnectionConfig connectionConfig;
    OBConsoleDataSourceFactory obConsoleDataSourceFactory;


    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        HashMap<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testUsernameAndPassword() {
        assertEquals("username", obConsoleDataSourceFactory.getUsername());
        assertEquals("password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("jdbc:mysql://localhost:3306/testSchema?zeroDateTimeBehavior=convertToNull",
            obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copiedFactory = obConsoleDataSourceFactory.deepCopy();
        assertEquals(obConsoleDataSourceFactory.getUsername(), copiedFactory.getUsername());
        assertEquals(obConsoleDataSourceFactory.getPassword(), copiedFactory.getPassword());
    }
}
