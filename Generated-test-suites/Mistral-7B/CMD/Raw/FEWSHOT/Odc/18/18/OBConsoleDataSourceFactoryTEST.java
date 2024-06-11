package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

/**
 * Test class for OBConsoleDataSourceFactory
 *
 * @author yh263208
 */
public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.LONG);
        connectionConfig.setSslConfig(new SSLConfig());
        connectionConfig.getSslConfig().setEnabled(true);
        connectionConfig.setSslFileEntry(new SSLFileEntry());
        connectionConfig.getSslFileEntry().setKeyStoreFilePath("test.p12");
        connectionConfig.getSslFileEntry().setKeyStoreFilePassword("testPassword");
        connectionConfig.setEventPublisher(Mockito.mock(EventPublisher.class));
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.cj.jdbc.Driver");
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn("jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=true&trustStore="
                        + "test.p12&trustStorePassword=testPassword&keyStore=" + "test.p12&keyStorePassword=testPassword");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(Collections.singletonList(Mockito.mock(ConnectionInitializer.class)));

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertEquals("com.mysql.cj.jdbc.Driver", dataSource.getDriverClassName());
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals("jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=true&trustStore=test.p12&trustStorePassword=testPassword&keyStore=test.p12&keyStorePassword=testPassword", dataSource.getUrl());
        assertEquals("testUser", dataSource.getUsername());
        assertEquals("testPassword", dataSource.getPassword());
        assertNotNull(factory.getEventPublisher());
        verify(factory.getEventPublisher()).publishEvent(Mockito.any());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory copiedFactory = factory.deepCopy();
        assertEquals(connectionConfig, mapper.clone(factory.connectionConfig));
        assertEquals(factory.autoCommit, copiedFactory.autoCommit);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        factory.resetUsername(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        factory.resetPassword(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        factory.resetHost(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        Integer newPort = 3307;
        factory.resetPort(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        factory.resetSchema(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("test1", "testValue1");
        newParams.put("test2", "testValue2");
        factory.resetParameters(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        assertEquals(newParams, factory.getParameters());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser@testTenant", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", factory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.LONG, factory.getConnectType());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("testSchema", factory.getDefaultSchema());
    }
}