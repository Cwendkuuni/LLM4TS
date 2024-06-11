import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DelegatingDataSource;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private CloneableDataSourceFactory cloneableFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setType(ConnectType.MYSQL);
        connectionConfig.setAutoCommit(true);
        connectionConfig.setSslConfig(new SSLConfig(true, null, null));

        connectionConfig.setJdbcUrlParameters(new HashMap<String, String>() {{
            put("useSSL", "true");
            put("trustStore", "test.keystore");
            put("trustStorePassword", "testPassword");
            put("keyStore", "test.keystore");
            put("keyStorePassword", "testPassword");
        }});

        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        cloneableFactory = Mockito.spy(new OBConsoleDataSourceFactory(connectionConfig, true));
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertTrue(dataSource instanceof SingleConnectionDataSource);
        assertEquals(connectionConfig.getHost(), dataSource.getConnectionProperties().getProperty("url").split(":")[1].split("/")[0]);
        assertSame(connectionConfig.getUsername(), dataSource.getUsername());
        assertSame(connectionConfig.getPassword(), dataSource.getPassword());
        assertSame(connectionConfig.getType(), dataSource.getConnection().getMetaData().getDatabaseProductName());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(connectionConfig.getDialectType(), factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertFalse(clonedFactory.equals(factory));
        assertEquals(connectionConfig, clonedFactory.getConnectionConfig());
        assertSame(factory.getDialectType(), clonedFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newUsername = "newUsername";
        factory.resetUsername(mapper);
        assertEquals(newUsername, factory.getUsername());
        assertEquals("testUser", connectionConfig.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newPassword = "newPassword";
        factory.resetPassword(mapper);
        assertEquals(newPassword, factory.getPassword());
        assertEquals("testPassword", connectionConfig.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newHost = "newHost";
        factory.resetHost(mapper);
        assertEquals(newHost, factory.getHost());
        assertEquals("localhost", connectionConfig.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        int newPort = 1234;
        factory.resetPort(mapper);
        assertEquals(newPort, factory.getPort());
        assertEquals(3306, connectionConfig.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        String newSchema = "newSchema";
        factory.resetSchema(mapper);
        assertEquals(newSchema, factory.getDefaultSchema());
        assertEquals("testSchema", connectionConfig.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionMapper mapper = ConnectionMapper.INSTANCE;
        Map<String, String> newParams = new HashMap<>();
        newParams.put("allowLoadLocalInfile", "true");
        factory.resetParameters(mapper);
        assertEquals(newParams, factory.getParameters());
        assertNull(connectionConfig.getJdbcUrlParameters().get("allowLoadLocalInfile"));
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = factory.getJdbcUrl();
        String expectedUrl = "jdbc:mysql://localhost:3306/testUser@testSchema?useSSL=true&trustStore=test.keystore&trustStorePassword=testPassword&keyStore=test.keystore&keyStorePassword=testPassword";
        assertEquals(expectedUrl, jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = factory.getUsername();
        assertEquals("testUser", username);
    }

    @Test
    public void testGetPassword() {
        String password = factory.getPassword();
        assertEquals("testPassword", password);
    }

    @Test
    public void testGetDefaultSchema() {
        String schema = factory.getDefaultSchema();
        assertEquals("testSchema", schema);
    }

    @Test
    public void testDeepCopyWithNullConnectionConfig() {
        ConnectionConfig nullConnectionConfig = null;
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertFalse(clonedFactory.equals(factory));
        assertNull(clonedFactory.getConnectionConfig());
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullConnectionConfig() {
        ConnectionConfig nullConnectionConfig = null;
        OBConsoleDataSourceFactory nullFactory = new OBConsoleDataSourceFactory(nullConnectionConfig, true);
        nullFactory.getDataSource();
    }

    @Test
    public void testClone() throws Exception {
        Connection connection = DriverManager.getConnection(factory.getJdbcUrl());
        PoolingDataSource poolingDataSource = new DelegatingDataSource(new BasicDataSourceFactory().createDataSource());
        poolingDataSource.setDriverClassName(factory.getConnection().getMetaData().getDriverName());
        poolingDataSource.setUrl(factory.getJdbcUrl());
        poolingDataSource.setUsername(factory.getUsername());
        poolingDataSource.setPassword(factory.getPassword());
        poolingDataSource.setInitialSize(1);
        poolingDataSource.addDataSourceProperty("testProperty", "testValue");

        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        CloneableDataSourceFactory clonedPoolingDataSourceFactory = (CloneableDataSourceFactory) clonedFactory.deepCopy();
        CloneableDataSourceFactory clonedFactory2 = factory.deepCopy();

        assertEquals(factory, clonedFactory);
        assertEquals(factory, clonedFactory2);

        assertEquals(connection, clonedFactory.getDataSource().getConnection());
        assertEquals(poolingDataSource, clonedPoolingDataSourceFactory.getDataSource());

        assertSame(connection, clonedFactory.getDataSource().getConnection());
        assertSame(poolingDataSource, clonedPoolingDataSourceFactory.getDataSource());

        assertFalse(clonedFactory.equals(clonedPoolingDataSourceFactory));
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = factory.getDataSource().getConnection();
        assertSame(connection, factory.getDataSource().getConnection());

        assertSame(connection, factory.getDataSource().getConnection());
        assertSame(connection, factory.getDataSource().getConnection());

        connection.close();
    }
}