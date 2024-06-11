package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof DataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);

        // Add more assertions based on specific requirements
    }

    @Test
    public void testCloneableDataSourceFactoryMethods() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory cloneFactory = factory.deepCopy();
        assertNotNull(cloneFactory);
        assertTrue(cloneFactory instanceof OBConsoleDataSourceFactory);

        cloneFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", factory.getUsername());

        cloneFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", factory.getPassword());

        cloneFactory.resetHost(value -> "newHost");
        assertEquals("newHost", factory.getHost());

        cloneFactory.resetPort(value -> 3307);
        assertEquals(3307, factory.getPort());

        cloneFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", factory.getDefaultSchema());

        cloneFactory.resetParameters(value -> new HashMap<>());
        assertTrue(factory.getParameters().isEmpty());

        // Add more assertions based on specific requirements
    }
}