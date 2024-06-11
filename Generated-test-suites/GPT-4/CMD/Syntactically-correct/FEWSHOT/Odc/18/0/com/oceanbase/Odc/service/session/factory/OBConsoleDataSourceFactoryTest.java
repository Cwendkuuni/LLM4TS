package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import java.util.*;
import javax.sql.DataSource;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class OBConsoleDataSourceFactoryTest {
    @Test
    public void testCloneableDataSourceFactory() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(8080);
        when(connectionConfig.getUsername()).thenReturn("root");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(params);
        
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        assertNotNull(dataSourceFactory.getDataSource());

        assertThrows(IllegalArgumentException.class, () -> {
            dataSourceFactory.resetPort(value -> -1);
        });

        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        List<ConnectionInitializer> initializers = new ArrayList<>();
        initializers.add(mock(ConnectionInitializer.class));
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(initializers);
        dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);

        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);

        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());

        assertNotNull(dataSourceFactory.deepCopy());

        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());

        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());

        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", dataSourceFactory.getHost());

        dataSourceFactory.resetPort(value -> 9000);
        assertEquals(Integer.valueOf(9000), dataSourceFactory.getPort());

        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getSchema());

        Map<String, String> newParams = new HashMap<>();
        newParams.put("param1", "newValue1");
        newParams.put("param2", "newValue2");
        dataSourceFactory.resetParameters(value -> newParams);
        assertEquals(newParams, dataSourceFactory.getParameters());
    }
}