package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.sql.DataSource;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void getUsernameTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getUsername()).thenReturn("Username");
        assertEquals("Username", OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    @Test
    public void getPasswordTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getPassword()).thenReturn("Password");
        assertEquals("Password", OBConsoleDataSourceFactory.getPassword(connectionConfig));
    }

    @Test
    public void getJdbcParamsTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Map<String, String> map = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertEquals("false", map.get("useSSL"));
    }

    @Test
    public void getDataSourceTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void getDialectTypeTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void resetHostTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obConsoleDataSourceFactory.resetHost(value -> "localhost");
        assertEquals("localhost", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void getDefaultSchemaTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        assertEquals("mysql", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
    }
}
