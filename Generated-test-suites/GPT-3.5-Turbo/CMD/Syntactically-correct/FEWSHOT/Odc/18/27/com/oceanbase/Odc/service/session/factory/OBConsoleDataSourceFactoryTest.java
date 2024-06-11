package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        String expectedJdbcUrl = "jdbc:odc:localhost:3306/default_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        assertNotNull(factory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        assertEquals(DialectType.DEFAULT, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetUsername(value -> value + "_updated");
        assertEquals("username_updated", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetPassword(value -> value + "_updated");
        assertEquals("password_updated", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetHost(value -> "localhost");
        assertEquals("localhost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetPort(value -> 3306);
        assertEquals(3306, (int) factory.getPort());
    }

    @Test
    public void testResetSchema() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetSchema(value -> "new_schema");
        assertEquals("new_schema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        factory.resetParameters(value -> {
            Map<String, String> params = new HashMap<>();
            params.put("key1", "value1");
            params.put("key2", "value2");
            return params;
        });
        assertEquals("value1", factory.getParameters().get("key1"));
        assertEquals("value2", factory.getParameters().get("key2"));
    }

}