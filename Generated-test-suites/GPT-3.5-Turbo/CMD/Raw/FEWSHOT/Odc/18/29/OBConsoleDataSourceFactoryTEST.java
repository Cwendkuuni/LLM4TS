package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    @Test
    public void testGetJdbcUrl() {
        // Test for valid connection configurations
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        String expectedUrl = "jdbc:mysql://localhost:3306/test?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }
    
    @Test
    public void testGetDataSource() {
        // Test for getting DataSource
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        assertNotNull(factory.getDataSource());
    }
    
    @Test
    public void testResetPort() {
        // Test for resetting port
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        factory.resetPort(value -> value + 1);
        assertEquals(Integer.valueOf(3307), factory.getPort());
    }
    
    @Test
    public void testResetSchema() {
        // Test for resetting default schema
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        factory.resetSchema(value -> "new_schema");
        assertEquals("new_schema", factory.getDefaultSchema());
    }
    
    @Test
    public void testResetParameters() {
        // Test for resetting parameters
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        factory.resetParameters(value -> new HashMap<>());
        assertTrue(factory.getJdbcParams().isEmpty());
    }
    
    @Test
    public void testGetDialectType() {
        // Test for getting dialect type
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(
            new ConnectionConfig("jdbc:mysql://localhost:3306/test", "username", "password", "localhost", 3306, DialectType.MYSQL),
            true
        );
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }
}