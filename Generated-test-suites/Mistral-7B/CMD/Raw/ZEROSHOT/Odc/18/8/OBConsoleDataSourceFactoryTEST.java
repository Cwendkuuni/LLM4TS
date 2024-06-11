package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
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

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private ConnectionInitializer connectionInitializer;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private ConnectionPluginUtil connectionPluginUtil;
    @Mock
    private ConnectionMapper connectionMapper;
    @InjectMocks
    private OBConsoleDataSourceFactory objUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE)).thenReturn(connectionExtensionPoint);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        String jdbcUrl = objUnderTest.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testUser@testSchema", jdbcUrl);

        DataSource dataSource = objUnderTest.getDataSource();
        assertNotNull(dataSource);
        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testSchema"), anyMap());
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verify(connectionExtensionPoint, atLeastOnce()).getConnectionInitializers();
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionInitializer);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig().setEnabled(true)
                .setKeyStoreFilePath("/path/to/keystore")
                .setKeyStoreFilePassword("password"));
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        String jdbcUrl = objUnderTest.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testUser@testSchema", jdbcUrl);

        DataSource dataSource = objUnderTest.getDataSource();
        assertNotNull(dataSource);
        verify(connectionExtensionPoint, atLeastOnce()).generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap());
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verify(connectionExtensionPoint, atLeastOnce()).getConnectionInitializers();
        verify(connectionExtensionPoint, times(1)).getCertificateTrustManager();
        verify(connectionExtensionPoint, times(1)).getTrustStore();
        verify(connectionExtensionPoint, times(1)).getKeyManagerFactory();
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionInitializer);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithInvalidPort() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(-1);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        try {
            objUnderTest.getDataSource();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("Port can not be negative", e.getMessage());
        }

        verifyNoMoreInteractions(connectionExtensionPoint);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionInitializer);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithNullParameters() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(null);

        try {
            objUnderTest.getDataSource();
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals("java.util.HashMap cannot be cast to java.util.Map", e.getCause().getMessage());
        }

        verifyNoMoreInteractions(connectionExtensionPoint);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionInitializer);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithEmptyParameters() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        DataSource dataSource = objUnderTest.getDataSource();
        assertNotNull(dataSource);
        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testUser"), anyMap());
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verify(connectionExtensionPoint, atLeastOnce()).getConnectionInitializers();
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionInitializer);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }

    @Test
    public void testGetDataSourceWithInitConnectionFalse() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.OB_MYSQL);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        DataSource dataSource = objUnderTest.getDataSource(false);
        assertNotNull(dataSource);
        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(eq("localhost"), eq(3306), eq("testUser"), anyMap());
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verifyNoInteractions(connectionInitializer);
        verifyNoMoreInteractions(eventPublisher);
        verifyNoMoreInteractions(connectionMapper);
        verifyNoMoreInteractions(connectionPluginUtil);
    }
}