package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import java.util.HashMap;
import java.util.Map;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig mockConfig;

    @Before
    public void setup() {
        mockConfig = mock(ConnectionConfig.class);
        when(mockConfig.getHost()).thenReturn("localhost");
        when(mockConfig.getPort()).thenReturn(8080);
        when(mockConfig.getUsername()).thenReturn("alpha");
        when(mockConfig.getPassword()).thenReturn("beta");
        when(mockConfig.getType()).thenReturn(ConnectType.CUSTOM);
        
        factory = new OBConsoleDataSourceFactory(mockConfig, false);
    }

    @Test
    public void getJdbcUrlTest() {
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull("JDBC URL should not be null.", jdbcUrl);
    }

    @Test
    public void getUsernameTest() {
        String username = OBConsoleDataSourceFactory.getUsername(mockConfig);
        assertEquals("Username should match.", "alpha", username);
    }

    @Test
    public void getPasswordTest() {
        String password = OBConsoleDataSourceFactory.getPassword(mockConfig);
        assertEquals("Password should match.", "beta", password);
    }

    @Test
    public void getJdbcParamsTest() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(mockConfig);
        assertNotNull("JDBC parameters should not be null.", params);
    }

    @Test
    public void deepCopyTest() {
        OBConsoleDataSourceFactory clonedFactory = factory.deepCopy();
        assertEquals("Username should match after deep copying.", factory.getUsername(), clonedFactory.getUsername());
        assertEquals("Password should match after deep copying.", factory.getPassword(), clonedFactory.getPassword());
    }

    @Test(expected=IllegalArgumentException.class)
    public void resetPort_ErrorTest() {
        factory.resetPort(value -> -1);
    }
    
    @Test
    public void resetHostTest() {
        factory.resetHost(value -> "new_host");
        assertEquals("Host should match the new value.", "new_host", factory.getHost());
    }

    @Test
    public void getConnectTypeTest() {
        ConnectType type = factory.getConnectType();
        assertEquals("Connect Type should match.", ConnectType.CUSTOM, type);
    }
}
