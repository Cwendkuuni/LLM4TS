package com.oceanbase.odc.service.session.factory;

import static com.oceanbase.odc.core.shared.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import lombok.NonNull;
import lombok.Setter;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testNewInstance() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
        assertNotNull(obj);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
        DataSource dataSource = obj.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String jdbcUrl = obj.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String username = obj.getUsername();
        assertNull(username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String password = obj.getPassword();
        assertNull(password);
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String defaultSchema = obj.getDefaultSchema();
        assertNull(defaultSchema);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        Map<String, String> jdbcUrlParams = obj.getJdbcParams();
        assertNotNull(jdbcUrlParams);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        DialectType dialectType = obj.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        CloneableDataSourceFactory deepCopy = obj.deepCopy();
        assertNotNull(deepCopy);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String newUsername = "newUsername";
        obj.resetUsername(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newUsername;
            }
        });
        assertEquals(newUsername, obj.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String newPassword = "newPassword";
        obj.resetPassword(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newPassword;
            }
        });
        assertEquals(newPassword, obj.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String newHost = "newHost";
        obj.resetHost(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newHost;
            }
        });
        assertEquals(newHost, obj.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        int newPort = 1234;
        obj.resetPort(new ValueMapper<Integer>() {
            @Override
            public Integer map(Integer value) {
                return newPort;
            }
        });
        assertEquals(newPort, obj.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        String newSchema = "newSchema";
        obj.resetSchema(new ValueMapper<String>() {
            @Override
            public String map(String value) {
                return newSchema;
            }
        });
        assertEquals(newSchema, obj.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        Map<String, String> newParameters = new HashMap<>();
        obj.resetParameters(new ValueMapper<Map<String, String>>() {
            @Override
            public Map<String, String> map(Map<String, String> value) {
                return newParameters;
            }
        });
        assertEquals(newParameters, obj.getParameters());
    }

    @Test
    public void testGetUsernameFromConnectionConfig() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getUsername()).thenReturn("testUsername");
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        assertEquals("testUsername", obj.getUsername());
    }

    @Test
    public void testGetPasswordFromConnectionConfig() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        assertEquals("testPassword", obj.getPassword());
    }

    @Test
    public void testGetDefaultSchemaFromConnectionConfig() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        assertEquals("testSchema", obj.getDefaultSchema());
    }

    @Test
    public void testGetJdbcParamsFromConnectionConfig() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        Map<String, String> jdbcUrlParams = obj.getJdbcParams();
        assertNotNull(jdbcUrlParams);
        assertEquals(0, jdbcUrlParams.size());
    }

    @Test
    public void testGetDialectTypeFromConnectionConfig() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        OBConsoleDataSourceFactory obj = new OBConsoleDataSourceFactory(connectionConfig, null);
        assertEquals(DialectType.OB_ORACLE, obj.getDialectType());
    }

    @Test
    public void testCloneableDataSourceFactory() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory original = new OBConsoleDataSourceFactory(connectionConfig, null);
        CloneableDataSourceFactory cloned = original.deepCopy();
        assertNotNull(cloned);
        assertNotSame(original, cloned);
    }
}

class ValueMapper<T> {
    public abstract T map(T value);
}