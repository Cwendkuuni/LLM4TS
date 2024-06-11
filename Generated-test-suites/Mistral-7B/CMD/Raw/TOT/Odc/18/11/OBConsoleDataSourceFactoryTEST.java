package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.util.DBUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
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

    private DataSourceFactory dataSourceFactory;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private EventPublisher eventPublisher;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.closeQuietly(dataSourceFactory.getDataSource().getConnection());
        DBUtils.shutdown(dataSourceFactory.getDataSource());
        Mockito.verifyNoMoreInteractions(connectionExtensionPoint);
    }

    @Test
    public void testGetDataSource() throws SQLException {
        DataSource dataSource = dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(jdbcUrl, dataSource.getURL());
        assertEquals(connectionConfig.getUsername(), connection.createStatement().getUserName());
        assertEquals(connectionConfig.getPassword(), connection.createStatement().getPassword());
        connection.close();
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=false";
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        OBConsoleDataSourceFactory deepCopy = dataSourceFactory.deepCopy();
        assertEquals(connectionConfig, deepCopy.getConnectionConfig());
        assertEquals(dataSourceFactory.getUsername(), deepCopy.getUsername());
        assertEquals(dataSourceFactory.getPassword(), deepCopy.getPassword());
        assertEquals(dataSourceFactory.getHost(), deepCopy.getHost());
        assertEquals(dataSourceFactory.getPort(), deepCopy.getPort());
        assertEquals(dataSourceFactory.getDefaultSchema(), deepCopy.getDefaultSchema());
        assertEquals(dataSourceFactory.getParameters(), deepCopy.getParameters());
        assertEquals(dataSourceFactory.getConnectionExtensionPoint(), deepCopy.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newUsername = "newUsername";
        dataSourceFactory.resetUsername(mapper);
        assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newPassword = "newPassword";
        dataSourceFactory.resetPassword(mapper);
        assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newHost = "newHost";
        dataSourceFactory.resetHost(mapper);
        assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        int newPort = 3307;
        dataSourceFactory.resetPort(mapper);
        assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newSchema = "newSchema";
        dataSourceFactory.resetSchema(mapper);
        assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newParameter", "newValue");
        dataSourceFactory.resetParameters(mapper);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

    @Test
    public void testInitConnection() throws SQLException {
        ConnectionInitializer connectionInitializer = Mockito.mock(ConnectionInitializer.class);
        SessionCreatedInitializer sessionCreatedInitializer = Mockito.mock(SessionCreatedInitializer.class);
        when(connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ConnectionInitializer[] { connectionInitializer });
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        dataSourceFactory.getDataSource();
        verify(connectionInitializer).initDatabase();
        verify(sessionCreatedInitializer).executeInitializer(dataSourceFactory);
    }
}