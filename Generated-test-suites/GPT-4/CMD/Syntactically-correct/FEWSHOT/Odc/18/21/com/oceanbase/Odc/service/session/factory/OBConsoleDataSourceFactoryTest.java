
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import javax.sql.DataSource;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    ConnectionConfig connectionConfig;

    @Test
    public void testGetJdbcUrl() {
        when(connectionConfig.getHost()).thenReturn("hostTest");
        when(connectionConfig.getPort()).thenReturn(1234);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String result = factory.getJdbcUrl();

        assertEquals("result", result);
        verify(connectionConfig, times(1)).getHost();
        verify(connectionConfig, times(1)).getPort();
        verify(connectionConfig, times(1)).getDialectType();
        verifyNoMoreInteractions(connectionConfig);
    }

    @Test
    public void testGetUsername() {
        when(connectionConfig.getUsername()).thenReturn("usernameTest");
        when(connectionConfig.getOBTenantName()).thenReturn("tenantNameTest");
        when(connectionConfig.getClusterName()).thenReturn("clusterNameTest");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        String result = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertEquals("usernameTest@tenantNameTest#clusterNameTest", result);
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("passwordTest");

        String result = OBConsoleDataSourceFactory.getPassword(connectionConfig);

        assertEquals("passwordTest", result);
    }

    @Test
    public void testGetJdbcParams() {
        when(connectionConfig.getEndpoint()).thenReturn(null);
        when(connectionConfig.getSslConfig()).thenReturn(null);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<String, Object>());

        Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        
        assertEquals("64000000", result.get("maxAllowedPacket"));
        assertEquals("false", result.get("jdbcCompliantTruncation"));
        verify(connectionConfig, times(1)).getEndpoint();
        verify(connectionConfig, times(1)).getSslConfig();
        verify(connectionConfig, times(1)).getJdbcUrlParameters();
        verifyNoMoreInteractions(connectionConfig);
    }

    @Test
    public void testGetDataSource() {
        when(connectionConfig.getEndpoint()).thenReturn(null);
        when(connectionConfig.getSslConfig()).thenReturn(null);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource result = factory.getDataSource();

        assertTrue(result instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DialectType result = factory.getDialectType();

        assertEquals(DialectType.OB_MYSQL, result);
    }

    @Test
    public void testDeepCopy() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory result = factory.deepCopy();

        assertTrue(result instanceof CloneableDataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        final String newUsername = "newUsername";
        final CloneableDataSourceFactory.ValueMapper<String> mapper = new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newUsername;
            }
        };

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetUsername(mapper);

        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        final String newPassword = "newPassword";
        final CloneableDataSourceFactory.ValueMapper<String> mapper = new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newPassword;
            }
        };

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPassword(mapper);

        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        final String newHost = "newHost";
        final CloneableDataSourceFactory.ValueMapper<String> mapper = new CloneableDataSourceFactory.ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newHost;
            }
        };

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetHost(mapper);

        assertEquals(newHost, factory.host);
    }

    @Test
    public void testResetPort() {
        final Integer newPort = 2345;
        final CloneableDataSourceFactory.ValueMapper<Integer> mapper = new CloneableDataSourceFactory.ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return newPort;
            }
        };

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(mapper);

        assertEquals(newPort, factory.port);
    }
}
