package com.oceanbase.odc.service.session.factory.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
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

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
        assertNotNull(obConsoleDataSourceFactory);
    }

    @Test
    public void testGetDataSource_ValidConstruction() throws SQLException {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        DataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher, connectionExtensionPoint);
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        when(connectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.eq(obConsoleDataSourceFactory.getHost()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getPort()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getDefaultSchema()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getParameters())))
                .thenReturn(jdbcUrl);
        DataSource dataSourceReturn = obConsoleDataSourceFactory.getDataSource();
        assertEquals(dataSource, dataSourceReturn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_NullConnectionConfig() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_NullEventPublisher() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, null, mock(ConnectionExtensionPoint.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSource_NullConnectionExtensionPoint() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, eventPublisher, null);
    }

    @Test
    public void testGetJdbcUrl_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        ConnectionExtensionPoint connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        String jdbcUrl = "jdbc:url:test";
        when(connectionExtensionPoint.generateJdbcUrl(ArgumentMatchers.eq(obConsoleDataSourceFactory.getHost()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getPort()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getDefaultSchema()),
                ArgumentMatchers.eq(obConsoleDataSourceFactory.getParameters())))
                .thenReturn(jdbcUrl);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), connectionExtensionPoint);
        String jdbcUrlReturn = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(jdbcUrl, jdbcUrlReturn);
    }

    @Test
    public void testGetUsername_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String username = "testUser";
        when(connectionConfig.getUsername()).thenReturn(username);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        String usernameReturn = obConsoleDataSourceFactory.getUsername();
        assertEquals(username, usernameReturn);
    }

    @Test
    public void testGetPassword_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String password = "testPassword";
        when(connectionConfig.getPassword()).thenReturn(password);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        String passwordReturn = obConsoleDataSourceFactory.getPassword();
        assertEquals(password, passwordReturn);
    }

    @Test
    public void testGetDefaultSchema_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String defaultSchema = "test_schema";
        when(connectionConfig.getDefaultSchema()).thenReturn(defaultSchema);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        String defaultSchemaReturn = obConsoleDataSourceFactory.getDefaultSchema();
        assertEquals(defaultSchema, defaultSchemaReturn);
    }

    @Test
    public void testGetParameters_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("testKey", "testValue");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(parameters);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        Map<String, String> parametersReturn = obConsoleDataSourceFactory.getParameters();
        assertEquals(parameters, parametersReturn);
    }

    @Test
    public void testGetDialectType_ValidConstruction() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        DialectType dialectType = DialectType.OB_ORACLE;
        when(connectionConfig.getDialectType()).thenReturn(dialectType);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        DialectType dialectTypeReturn = obConsoleDataSourceFactory.getDialectType();
        assertEquals(dialectType, dialectTypeReturn);
    }

    @Test
    public void testClone_ValidConstruction() throws CloneNotSupportedException {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true, mock(EventPublisher.class), mock(ConnectionExtensionPoint.class));
        OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) obConsoleDataSourceFactory.deepCopy();
        assertEquals(obConsoleDataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
        assertEquals(obConsoleDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
        assertEquals(obConsoleDataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
        assertEquals(obConsoleDataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
        assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
        assertEquals(obConsoleDataSourceFactory.getParameters(), clonedDataSourceFactory.getParameters());
        assertEquals(obConsoleDataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetUsername_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetUsername((ValueMapper<String>)-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPassword_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetPassword((ValueMapper<String>)-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetHost_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetHost((ValueMapper<String>)-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetPort((ValueMapper<Integer>)-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetSchema_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetSchema((ValueMapper<String>)-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetParameters_NullMapper() {
        OBConsoleDataSourceFactory obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(mock(ConnectionConfig.class), true);
        obConsoleDataSourceFactory.resetParameters((ValueMapper<Map<String, String>>)-1);
    }

}