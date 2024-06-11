
package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private Boolean autoCommit;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        autoCommit = true;
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetJdbcUrl() {
        assertEquals("Incorrect JdbcUrl ", obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        when(connectionConfig.getUsername()).thenReturn("testUserName");
        assertEquals("Incorrect UserName", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        assertEquals("Incorrect Password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("testKey", "testValue");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn((Map) jdbcParams);
        assertEquals("Incorrect JdbcParams", OBConsoleDataSourceFactory.getJdbcParams(connectionConfig));
    }

    @Test
    public void testGetDataSource() {
        assertTrue(obConsoleDataSourceFactory.getDataSource() instanceof DataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals("Incorrect Dialect Type ", connectionConfig.getDialectType(), obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        assertEquals("Failed to deeply copy", obConsoleDataSourceFactory.deepCopy(), obConsoleDataSourceFactory.deepCopy());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetUsername(mapper);
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPassword(mapper);
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetHost(mapper);
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPort(mapper);
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetSchema(mapper);
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetParameters(mapper);
    }

    @Test
    public void testGetDefaultSchema() {
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
        assertEquals("Incorrect DefaultSchema", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
    }
}
