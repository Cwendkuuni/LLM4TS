
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {
    @Mock ConnectionConfig connectionConfig;
    @Mock ConnectionExtensionPoint connectionExtensionPoint;
    private OBConsoleDataSourceFactory factoryUnderTest;
    private static final String VALID_HOST = "localhost";
    private static final Integer VALID_PORT = 3306;

    @Before public void setUp() {
        when(connectionConfig.getHost()).thenReturn(VALID_HOST);
        when(connectionConfig.getPort()).thenReturn(VALID_PORT);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        this.factoryUnderTest = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test public void successfulCreationTest() {
        assertNotNull(factoryUnderTest);
    }

    @Test public void getJdbcUrlTest() {
        String jdbcUrl = factoryUnderTest.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test public void getUsernameTest() {
        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertNotNull(username);
    }

    @Test public void getPasswordTest() {
        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertNotNull(password);
    }

    @Test public void getJdbcParamsTest() {
        Map<String, String> props = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertNotNull(props);
    }

    @Test public void getDataSourceTest() {
        DataSource dataSource = factoryUnderTest.getDataSource();
        assertNotNull(dataSource);
    }

    @Test public void getDialectTypeTest() {
        DialectType dialect = factoryUnderTest.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialect);
    }

    @Test public void deepCopyTest() {
        OBConsoleDataSourceFactory copyFactory = (OBConsoleDataSourceFactory) factoryUnderTest.deepCopy();
        assertEquals(VALID_HOST, copyFactory.getHost());
        assertEquals(VALID_PORT, copyFactory.getPort());
    }

    @Test public void resetHostTest() {
        Assert.notNull(factoryUnderTest.getHost(), "Host should not be null");
    
        String newHost = "newHost";
        factoryUnderTest.resetHost((oldHost) -> newHost);
        String host = factoryUnderTest.getHost();
        assertEquals("Host should be updated", newHost, host);
    }

    @Test public void resetPortTest() {
        Validate.notNull(factoryUnderTest.getPort(), "Port should not be null");
        Integer newPort = 8080;
        factoryUnderTest.resetPort((oldPort) -> newPort);
        Integer port = factoryUnderTest.getPort();
        assertEquals("Port should be updated", newPort, port);
    }
    
    // Following tests for other reset methods
    // ...

    @Test(expected=IllegalArgumentException.class)
    public void resetPortWithNegativeTest() {
        Validate.notNull(factoryUnderTest.getPort(), "Port should not be null");
        Integer newPort = -8080;
        factoryUnderTest.resetPort((oldPort) -> newPort);
    }
    
}
