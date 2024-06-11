import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    private static OBConsoleDataSourceFactory factory;
    private static ConnectionConfig connectionConfig;

    @BeforeClass
    public static void setUp() throws Exception {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        connectionConfig.setDialectType(DialectType.MYSQL);

        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetDataSource() throws Exception {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
        assertSame(connection.getMetaData().getDriverName(), factory.getConnectionExtensionPoint().getDriverClassName());

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals(jdbcUrl, "jdbc:mysql://localhost:3306/testSchema?useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false");
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals(jdbcUrl, "jdbc:mysql://localhost:3306/testSchema?useSSL=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=&autoDeserialize=false");
    }

    @Test
    public void testGetUsername() throws Exception {
        assertEquals(factory.getUsername(), "testUser");
    }

    @Test
    public void testGetPassword() throws Exception {
        assertNull(factory.getPassword());
    }

    @Test
    public void testGetDefaultSchema() throws Exception {
        assertEquals(factory.getDefaultSchema(), "testSchema");
    }

    @Test
    public void testGetParameters() throws Exception {
        Map<String, String> parameters = factory.getParameters();
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("maxAllowedPacket", "64000000");
        expectedParameters.put("allowMultiQueries", "true");
        expectedParameters.put("connectTimeout", "5000");
        expectedParameters.put("zeroDateTimeBehavior", OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        expectedParameters.put("noDatetimeStringSync", "true");
        expectedParameters.put("jdbcCompliantTruncation", "false");
        expectedParameters.put("sendConnectionAttributes", "false");
        assertEquals(parameters, expectedParameters);
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory newFactory = factory.deepCopy();
        Validate.isInstanceOf(OBConsoleDataSourceFactory.class, newFactory);

        assertEquals(newFactory.getUsername(), "testUser");
        assertEquals(newFactory.getPassword(), null);
        assertEquals(newFactory.getHost(), "localhost");
        assertEquals(newFactory.getPort(), 3306);
        assertEquals(newFactory.getDefaultSchema(), "testSchema");
        assertEquals(newFactory.getParameters(), factory.getParameters());

        String jdbcUrl1 = factory.getJdbcUrl();
        String jdbcUrl2 = newFactory.getJdbcUrl();
        assertEquals(jdbcUrl1, jdbcUrl2);
    }

    @Test
    public void testResetUsername() throws Exception {
        String newUsername = "newUser";
        factory.resetUsername((value) -> newUsername);
        assertEquals(factory.getUsername(), newUsername);
    }

    @Test
    public void testResetPassword() throws Exception {
        String newPassword = "newPassword";
        factory.resetPassword((value) -> newPassword);
        assertNull(factory.getPassword());
    }

    @Test
    public void testResetHost() throws Exception {
        String newHost = "newHost";
        factory.resetHost((value) -> newHost);
        assertEquals(factory.getHost(), newHost);
    }

    @Test
    public void testResetPort() throws Exception {
        int newPort = 3307;
        factory.resetPort((value) -> newPort);
        assertEquals(factory.getPort(), newPort);
    }

    @Test
    public void testResetSchema() throws Exception {
        String newSchema = "newSchema";
        factory.resetSchema((value) -> newSchema);
        assertEquals(factory.getDefaultSchema(), newSchema);
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("maxAllowedPacket", "100000000");
        newParameters.put("connectTimeout", "2000");
        factory.resetParameters((value) -> newParameters);
        Map<String, String> parameters = factory.getParameters();
        assertEquals(parameters, newParameters);
    }

    @Test
    public void testCloneableDataSourceFactoryMethods() throws Exception {
        OBConsoleDataSourceFactory newFactory = factory.deepCopy();
        assertNotNull(newFactory);

        newFactory.resetUsername(Mockito.any());
        newFactory.resetPassword(Mockito.any());
        newFactory.resetHost(Mockito.any());
        newFactory.resetPort(Mockito.any());
        newFactory.resetSchema(Mockito.any());
        newFactory.resetParameters(Mockito.any());

        String jdbcUrl1 = factory.getJdbcUrl();
        String jdbcUrl2 = newFactory.getJdbcUrl();
        assertEquals(jdbcUrl1, jdbcUrl2);

        String username1 = factory.getUsername();
        String username2 = newFactory.getUsername();
        assertEquals(username1, username2);

        String password1 = factory.getPassword();
        String password2 = newFactory.getPassword();
        assertNull(password1);
        assertNull(password2);

        String schema1 = factory.getDefaultSchema();
        String schema2 = newFactory.getDefaultSchema();
        assertEquals(schema1, schema2);

        Map<String, String> parameters1 = factory.getParameters();
        Map<String, String> parameters2 = newFactory.getParameters();
        assertEquals(parameters1, parameters2);

        DataSource dataSource1 = factory.getDataSource();
        DataSource dataSource2 = newFactory.getDataSource();
        assertNotNull(dataSource1);
        assertNotNull(dataSource2);
        assertSame(dataSource1.getClass(), dataSource2.getClass());

        Connection connection1 = dataSource1.getConnection();
        Connection connection2 = dataSource2.getConnection();
        assertNotNull(connection1);
        assertNotNull(connection2);
        assertSame(connection1.getClass(), connection2.getClass());
    }

    @Test
    public void testGetDialectType() throws Exception {
        DialectType dialectType = factory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testToString() throws Exception {
        String expectedToString = new ToStringBuilder(factory, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("username", factory.getUsername())
                .append("password", factory.getPassword())
                .append("host", factory.getHost())
                .append("port", factory.getPort())
                .append("defaultSchema", factory.getDefaultSchema())
                .append("parameters", factory.getParameters())
                .append("autoCommit", factory.isAutoCommit())
                .append("initConnection", factory.isInitConnection())
                .append("eventPublisher", factory.getEventPublisher())
                .append("connectionExtensionPoint", factory.getConnectionExtensionPoint())
                .toString();

        String toString = factory.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testCreateFromConnectionConfig() throws Exception {
        OBConsoleDataSourceFactory newFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> parameters = newFactory.getParameters();
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("maxAllowedPacket", "64000000");
        expectedParameters.put("allowMultiQueries", "true");
        expectedParameters.put("connectTimeout", "5000");
        expectedParameters.put("zeroDateTimeBehavior", OBConsoleDataSourceFactory.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
        expectedParameters.put("noDatetimeStringSync", "true");
        expectedParameters.put("jdbcCompliantTruncation", "false");
        expectedParameters.put("sendConnectionAttributes", "false");
        assertEquals(parameters, expectedParameters);

        DataSource dataSource = newFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection instanceof Connection);
        assertSame(connection.getMetaData().getDriverName(), newFactory.getConnectionExtensionPoint().getDriverClassName());
    }
}