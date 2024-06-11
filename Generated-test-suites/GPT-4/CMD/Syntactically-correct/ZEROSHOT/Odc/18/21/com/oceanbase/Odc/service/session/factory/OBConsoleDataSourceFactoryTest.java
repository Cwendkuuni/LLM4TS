package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import javax.sql.DataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import java.util.Properties;
import java.util.HashMap;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void getJdbcUrlTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, false);
        assertNotNull(factory.getJdbcUrl());
    }

    @Test
    public void getUsernameTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getUsername()).thenReturn("user");
        String result = OBConsoleDataSourceFactory.getUsername(config);
        assertEquals("user", result);
    }

    @Test
    public void getPasswordTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getPassword()).thenReturn("password");
        String result = OBConsoleDataSourceFactory.getPassword(config);
        assertEquals("password", result);
    }

    @Test
    public void getDataSourceTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, false);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }
    
    @Test
    public void deepCopyTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, false);
        CloneableDataSourceFactory result = factory.deepCopy();
        assertNotNull(result);
    }
    
    @Test
    public void resetUsernameTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, false);
        factory.resetUsername(String::toUpperCase);
        assertEquals("USER", factory.getUsername());
    }

    @Test
    public void getJdbcParamsTest() {
        ConnectionConfig config = new ConnectionConfig();
        SSLConfig sslConfig = new SSLConfig();
        sslConfig.setEnabled(true);
        config.setSslConfig(sslConfig);
        config.setJdbcUrlParameters(new HashMap<>());
        Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(config);
        assertNotNull(result);
        assertEquals("false", result.get("allowLoadLocalInfile"));
    }

    @Test
    public void getDefaultSchemaTest() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        String result = OBConsoleDataSourceFactory.getDefaultSchema(config);
        assertEquals(OdcConstants.MYSQL_DEFAULT_SCHEMA, result);
    }
}
