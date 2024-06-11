package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
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

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        ConnectionPluginUtil connectionPluginUtil = Mockito.mock(ConnectionPluginUtil.class);
        ConnectionMapper connectionMapper = ConnectionMapper.INSTANCE;
        ConnectionSessionUtil connectionSessionUtil = Mockito.mock(ConnectionSessionUtil.class);
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);

        when(connectionPluginUtil.getConnectionExtension(DialectType.OB_ORACLE)).thenReturn(connectionExtensionPoint);
        when(connectionConfig.getUsername()).thenReturn("username");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(1521);
        when(connectionConfig.getDefaultSchema()).thenReturn("schema");
        when(connectionConfig.getType()).thenReturn(ConnectType.OB_ORACLE);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(connectionConfig.getEndpoint()).thenReturn(null);
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());

        SSLConfig sslConfig = Mockito.mock(SSLConfig.class);
        SSLFileEntry sslFileEntry = Mockito.mock(SSLFileEntry.class);
        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
        when(sslConfig.getEnabled()).thenReturn(false);
        when(sslFileEntry.getKeyStoreFilePath()).thenReturn("path/to/keystore.p12");

        Properties jdbcParams = new Properties();
        jdbcParams.put("maxAllowedPacket", "64000000");
        jdbcParams.put("allowMultiQueries", "true");
        jdbcParams.put("connectTimeout", "5000");
        jdbcParams.put("zeroDateTimeBehavior", "CONVERT_TO_NULL");

        when(connectionConfig.getJdbcUrlParameters()).thenReturn(jdbcParams);

        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("allowLoadLocalInfile", "false");
        connectionProperties.setProperty("allowUrlInLocalInfile", "false");
        connectionProperties.setProperty("allowLoadLocalInfileInPath", "");
        connectionProperties.setProperty("autoDeserialize", "false");

        Map<String, String> jdbcUrlParams = new HashMap<>();
        jdbcUrlParams.put("maxAllowedPacket", "64000000");
        jdbcUrlParams.put("allowMultiQueries", "true");
        jdbcUrlParams.put("connectTimeout", "5000");
        jdbcUrlParams.put("zeroDateTimeBehavior", "CONVERT_TO_NULL");

        when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq("localhost"), Mockito.eq(1521), Mockito.eq("schema"), Mockito.eq(jdbcUrlParams)))
                .thenReturn("jdbc:oracle:thin:@localhost:1521:schema");

        when(connectionExtensionPoint.getDriverClassName()).thenReturn("oracle.jdbc.driver.OracleDriver");

        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] {});

        when(connectionExtensionPoint.getConnectionInitializerClass()).thenReturn(SessionCreatedInitializer.class);

        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        when(initializer.initializeDatabase(Mockito.any(Connection.class))).thenReturn(true);

        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { initializer });

        factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        this.eventPublisher = eventPublisher;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = factory.getDataSource();
        assertEquals("username", dataSource.getUsername());
        assertEquals("password", dataSource.getPassword());
        assertEquals("jdbc:oracle:thin:@localhost:1521:schema", dataSource.getUrl());
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:oracle:thin:@localhost:1521:schema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = factory.getUsername();
        assertEquals("username", username);
    }

    @Test
    public void testGetPassword() {
        String password = factory.getPassword();
        assertEquals("password", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.OB_ORACLE, dialectType);
    }

    @Test
    public void testGetDataSourceWithSsl() throws SQLException {
        SSLConfig sslConfig = Mockito.mock(SSLConfig.class);
        SSLFileEntry sslFileEntry = Mockito.mock(SSLFileEntry.class);
        ConnectionExtensionPoint connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);

        when(connectionConfig.getSslConfig()).thenReturn(sslConfig);
        when(sslConfig.getEnabled()).thenReturn(true);
        when(sslFileEntry.getKeyStoreFilePath()).thenReturn("path/to/keystore.p12");
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(new HashMap<>());
        when(connectionExtensionPoint.generateJdbcUrl(Mockito.eq("localhost"), Mockito.eq(1521), Mockito.eq("schema"), Mockito.eq(jdbcUrlParams)))
                .thenReturn("jdbc:oracle:thin:@localhost:1521:schema");
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("oracle.jdbc.driver.OracleDriver");
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] {});

        DataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);

        DataSource dataSource = factory.getDataSource();
        assertEquals("username", dataSource.getUsername());
        assertEquals("password", dataSource.getPassword());
        assertEquals("jdbc:oracle:thin:@localhost:1521:schema", dataSource.getUrl());
        assertNotNull(dataSource.getConnection());
        assertTrue(dataSource.getConnection().isClosed());
        assertTrue(dataSource.getConnection().isValid(1000));
        dataSource.getConnection().close();

        verify(connectionExtensionPoint, times(1)).generateJdbcUrl(Mockito.eq("localhost"), Mockito.eq(1521), Mockito.eq("schema"), Mockito.eq(jdbcUrlParams));
        verify(connectionExtensionPoint, times(1)).getDriverClassName();
        verify(connectionExtensionPoint, times(0)).getConnectionInitializers();
        verify(connectionExtensionPoint, times(0)).getConnectionInitializerClass();
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        DataSourceFactory factory = new OBConsoleDataSourceFactory(null, false, true);
        factory.getDataSource();
    }
}