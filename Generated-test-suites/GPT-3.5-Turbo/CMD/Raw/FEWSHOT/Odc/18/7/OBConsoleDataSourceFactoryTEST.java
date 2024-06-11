package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig = new ConnectionConfig();
    private Boolean autoCommit = true;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:mysql://localhost:3306/test?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false";
        String actual = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertNotNull(obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clone = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clone);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(String::toUpperCase);
        assertEquals("USERNAME", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(String::toLowerCase);
        assertEquals("", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(s -> s + ".com");
        assertEquals("localhost.com", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(i -> i + 1);
        assertEquals(Integer.valueOf(3307), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(String::toUpperCase);
        assertEquals("TEST", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        obConsoleDataSourceFactory.resetParameters(s -> params);
        assertEquals(params, obConsoleDataSourceFactory.getParameters());
    }
}