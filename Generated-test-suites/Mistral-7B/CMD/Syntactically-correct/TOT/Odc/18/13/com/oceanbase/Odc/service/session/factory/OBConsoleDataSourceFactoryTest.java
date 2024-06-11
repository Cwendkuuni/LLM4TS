package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.DataSource;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import com.oceanbase.odc.service.session.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;
    @Mock
    private EventPublisher eventPublisher;
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSourceFactory.setEventPublisher(eventPublisher);
        dataSourceFactory.setConnectionExtensionPoint(connectionExtensionPoint);
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=true&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() throws SQLException, ClassNotFoundException {
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidUrl() throws SQLException, ClassNotFoundException {
        DataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        factory.setConnectionExtensionPoint(connectionExtensionPoint);
        factory.setEventPublisher(eventPublisher);
        String invalidJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?useSSL=true&trustServerCertificate=trust&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
                + OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&trustServerCertificate=none";
        DataSource dataSource = factory.getDataSource();
        fail("Expected SQLException but got DataSource instead");
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(originalFactory.getJdbcUrl(), clonedFactory.getJdbcUrl());
        assertEquals(originalFactory.getUsername(), clonedFactory.getUsername());
        assertEquals(originalFactory.getPassword(), clonedFactory.getPassword());
        assertEquals(originalFactory.getHost(), clonedFactory.getHost());
        assertEquals(originalFactory.getPort(), clonedFactory.getPort());
        assertEquals(originalFactory.getDefaultSchema(), clonedFactory.getDefaultSchema());
        assertEquals(originalFactory.getParameters(), clonedFactory.getParameters());
        assertEquals(originalFactory.getDialectType(), clonedFactory.getDialectType());
        assertEquals(originalFactory.getConnectType(), clonedFactory.getConnectType());
        assertEquals(originalFactory.getEventPublisher(), clonedFactory.getEventPublisher());
        assertEquals(originalFactory.getConnectionExtensionPoint(), clonedFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testResetUsername() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        String newUsername = "newTestUser";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetUsername(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newUsername, clonedFactory.getUsername());
    }

    @Test
    public void testResetPassword() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        String newPassword = "newTestPassword";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetPassword(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newPassword, clonedFactory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        String newHost = "newHost";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetHost(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newHost, clonedFactory.getHost());
    }

    @Test
    public void testResetPort() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        int newPort = 3307;
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetPort(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newPort, clonedFactory.getPort());
    }

    @Test
    public void testResetSchema() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        String newSchema = "newSchema";
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetSchema(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newSchema, clonedFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() throws Exception {
        OBConsoleDataSourceFactory originalFactory = dataSourceFactory;
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("newParameterKey", "newParameterValue");
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        originalFactory.resetParameters(mapper);
        OBConsoleDataSourceFactory clonedFactory = (OBConsoleDataSourceFactory) originalFactory.deepCopy();
        assertEquals(newParameters, clonedFactory.getParameters());
    }
}