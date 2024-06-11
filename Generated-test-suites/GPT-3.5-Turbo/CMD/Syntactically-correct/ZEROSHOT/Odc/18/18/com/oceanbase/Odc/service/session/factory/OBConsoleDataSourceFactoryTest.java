package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getHost()).thenReturn("localhost");
        Mockito.when(connectionConfig.getPort()).thenReturn(3306);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(connectionConfig.getUsername()).thenReturn("admin");
        Mockito.when(connectionConfig.getPassword()).thenReturn("password");
        Mockito.when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "newUsername");
        assertEquals("newUsername", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> " newPassword ");
        assertEquals(" newPassword ", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", obConsoleDataSourceFactory.host);
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 8080);
        assertEquals(8080, obConsoleDataSourceFactory.port);
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", obConsoleDataSourceFactory.defaultSchema);
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(value -> new HashMap<>());
        assertTrue(obConsoleDataSourceFactory.parameters.isEmpty());
    }
}