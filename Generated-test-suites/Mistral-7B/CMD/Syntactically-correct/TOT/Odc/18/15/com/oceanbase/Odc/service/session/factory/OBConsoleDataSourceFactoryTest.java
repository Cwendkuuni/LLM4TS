package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.sql.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
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

    private EventPublisher eventPublisher;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setType(ConnectType.JDBC);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));
        connectionConfig.setEndpoint(null);
        connectionConfig.setJdbcUrlParameters(new HashMap<>());

        this.eventPublisher = Mockito.mock(EventPublisher.class);
        this.connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
        when(this.connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenReturn("jdbc:mysql://localhost:3306/testSchema");
        when(this.connectionExtensionPoint.getConnectionInitializers()).thenReturn(new ArrayList<>());
        when(this.connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");

        this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @After
    public void tearDown() {
        this.dataSourceFactory = null;
        this.eventPublisher = null;
        this.connectionExtensionPoint = null;
    }

    @Test
    public void testGetDataSource() throws SQLException {
        BasicDataSource dataSource = (BasicDataSource) this.dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        assertEquals("testUser", connection.getUserInfo().getUserName());
        assertEquals("testSchema", connection.getCatalog());
        connection.close();
    }

    @Test(expected = SQLException.class)
    public void testGetDataSource_ConnectionFailed() throws SQLException {
        when(this.connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyMap())).thenThrow(SQLException.class);

        this.dataSourceFactory.getDataSource();
        fail("SQLException expected but not thrown");
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = this.dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/testSchema", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = this.dataSourceFactory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        String password = this.dataSourceFactory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = this.dataSourceFactory.getDialectType();
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testGetDataSourceWithConnectionInitializers() throws SQLException {
        when(this.connectionExtensionPoint.getConnectionInitializers()).thenReturn(List.of(new ConnectionInitializer() {
            @Override
            public void initializeDatabase(@NonNull Connection connection) throws SQLException {
                SQLUtils.closeQuietly(connection.createStatement().execute("CREATE TABLE test (id INT)"));
            }
        }));

        BasicDataSource dataSource = (BasicDataSource) this.dataSourceFactory.getDataSource();
        Connection connection = dataSource.getConnection();
        try {
            connection.createStatement().executeUpdate("INSERT INTO test VALUES (1)");
            fail("SQLException expected but not thrown");
        } catch (SQLException e) {
            // pass
        }

        connection.close();
    }

    @Test
    public void testDeepCopy() throws SQLException {
        BasicDataSource originalDataSource = (BasicDataSource) this.dataSourceFactory.getDataSource();
        OBConsoleDataSourceFactory clonedDataSourceFactory = this.dataSourceFactory.deepCopy();
        BasicDataSource clonedDataSource = (BasicDataSource) clonedDataSourceFactory.getDataSource();

        assertEquals(originalDataSource, clonedDataSource);
    }

    @Test
    public void testResetValues() throws SQLException {
        String newUsername = "newTestUser";
        String newPassword = "newTestPassword";
        String newHost = "newHost";
        int newPort = 3307;
        String newDefaultSchema = "newDefaultSchema";
        Map<String, String> newParameters = new HashMap<>();

        this.dataSourceFactory.resetUsername(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        this.dataSourceFactory.resetPassword(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        this.dataSourceFactory.resetHost(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        this.dataSourceFactory.resetPort(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        this.dataSourceFactory.resetSchema(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));
        this.dataSourceFactory.resetParameters(Mockito.mock(CloneableDataSourceFactory.ValueMapper.class));

        assertEquals("testUser", this.dataSourceFactory.getUsername());
        this.dataSourceFactory.resetUsername(Mockito.ofInstance(new UsernameMapper()));
        assertEquals(newUsername, this.dataSourceFactory.getUsername());

        assertEquals("testPassword", this.dataSourceFactory.getPassword());
        this.dataSourceFactory.resetPassword(Mockito.ofInstance(new PasswordMapper()));
        assertEquals(newPassword, this.dataSourceFactory.getPassword());

        assertEquals("localhost", this.dataSourceFactory.getHost());
        this.dataSourceFactory.resetHost(Mockito.ofInstance(new HostMapper()));
        assertEquals(newHost, this.dataSourceFactory.getHost());

        assertEquals(3306, this.dataSourceFactory.getPort());
        this.dataSourceFactory.resetPort(Mockito.ofInstance(new PortMapper()));
        assertEquals(newPort, this.dataSourceFactory.getPort());

        assertEquals("testSchema", this.dataSourceFactory.getDefaultSchema());
        this.dataSourceFactory.resetSchema(Mockito.ofInstance(new SchemaMapper()));
        assertEquals(newDefaultSchema, this.dataSourceFactory.getDefaultSchema());

        assertEquals(this.dataSourceFactory.getParameters(), this.dataSourceFactory.getParameters());
        this.dataSourceFactory.resetParameters(Mockito.ofInstance(new ParametersMapper()));
        assertNotEquals(this.dataSourceFactory.getParameters(), this.dataSourceFactory.getParameters());
    }

    private static class UsernameMapper implements CloneableDataSourceFactory.ValueMapper<String> {
        @Override
        public String map(String value) {
            return newUsername;
        }
    }

    private static class PasswordMapper implements CloneableDataSourceFactory.ValueMapper<String> {
        @Override
        public String map(String value) {
            return newPassword;
        }
    }

    private static class HostMapper implements CloneableDataSourceFactory.ValueMapper<String> {
        @Override
        public String map(String value) {
            return newHost;
        }
    }

    private static class PortMapper implements CloneableDataSourceFactory.ValueMapper<Integer> {
        @Override
        public Integer map(Integer value) {
            return newPort;
        }
    }

    private static class SchemaMapper implements CloneableDataSourceFactory.ValueMapper<String> {
        @Override
        public String map(String value) {
            return newDefaultSchema;
        }
    }

    private static class ParametersMapper implements CloneableDataSourceFactory.ValueMapper<Map<String, String>> {
        @Override
        public Map<String, String> map(Map<String, String> value) {
            Map<String, String> newParameters = new HashMap<>();
            newParameters.put("newKey", "newValue");
            return newParameters;
        }
    }

}