import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.oceanbase.odc.common.constant.OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OBConsoleDataSourceFactoryTest {

    private static OBConsoleDataSourceFactory factory;
    private static DataSource dataSource;
    private static Connection connection;

    @Parameterized.Parameters(name = "test {0} with {1} dialect")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new ConnectionConfig("jdbc:test:ds1", DialectType.OB_ORACLE, null), DialectType.OB_ORACLE},
                {new ConnectionConfig("jdbc:test:ds2", DialectType.MYSQL, null), DialectType.MYSQL},
                {new ConnectionConfig("jdbc:test:ds3", DialectType.ODP_SHARDED_MYSQL, null), DialectType.ODP_SHARDED_MYSQL}
        });
    }

    @BeforeClass
    public static void setup() throws SQLException {
        factory = new OBConsoleDataSourceFactory(Collections.emptyList(), false);
        dataSource = factory.getDataSource();
        connection = dataSource.getConnection();
    }

    @Test
    public void testGetJdbcUrl() throws Exception {
        ConnectionConfig connectionConfig = (ConnectionConfig) getParameters()[0];
        DialectType dialectType = (DialectType) getParameters()[1];
        String expectedJdbcUrl = factory.getConnectionExtensionPoint().generateJdbcUrl(connectionConfig.getHost(), connectionConfig.getPort(), factory.getDefaultSchema(connectionConfig), factory.getParameters());
        String actualJdbcUrl = factory.getJdbcUrl();
        assertTrue("Expected JDBC URL: " + expectedJdbcUrl + " but got: " + actualJdbcUrl, expectedJdbcUrl.equals(actualJdbcUrl));
    }

    @Test
    public void testGetDataSource() throws Exception {
        assertNotNull("DataSource should not be null", dataSource);
    }

    @Test
    public void testGetDialectType() throws Exception {
        DialectType dialectType = factory.getDialectType();
        assertTrue("Incorrect dialect type. Expected: " + getParameters()[1] + " but got: " + dialectType, dialectType.equals(getParameters()[1]));
    }

    @Test
    public void testDeepCopy() throws Exception {
        OBConsoleDataSourceFactory clone = factory.deepCopy();
        assertFalse("Cloned factory instance should not be the same as original", factory == clone);
        assertFalse("Cloned connectionConfig should not be the same as original", factory.getConnectionConfig() == clone.getConnectionConfig());
        assertTrue("Cloned factory instance should have the same properties as original", Arrays.equals(factory.getParameters().keySet().toArray(new String[0]), clone.getParameters().keySet().toArray(new String[0])));
    }

    @Test
    public void testResetUsername() throws Exception {
        String originalUsername = factory.getUsername();
        factory.resetUsername(ConnectionSessionUtil::mockUsername);
        String newUsername = factory.getUsername();
        assertFalse("Reset username should not keep the old value", originalUsername.equals(newUsername));
    }

    @Test
    public void testResetPassword() throws Exception {
        String originalPassword = factory.getPassword();
        factory.resetPassword(ConnectionSessionUtil::mockPassword);
        String newPassword = factory.getPassword();
        assertFalse("Reset password should not keep the old value", originalPassword.equals(newPassword));
    }

    @Test
    public void testResetHost() throws Exception {
        String originalHost = factory.getHost();
        factory.resetHost(ConnectionSessionUtil::mockHost);
        String newHost = factory.getHost();
        assertFalse("Reset host should not keep the old value", originalHost.equals(newHost));
    }

    @Test
    public void testResetPort() throws Exception {
        int originalPort = factory.getPort();
        factory.resetPort(ConnectionSessionUtil::mockPort);
        int newPort = factory.getPort();
        assertFalse("Reset port should not keep the old value", originalPort == newPort);
    }

    @Test
    public void testResetSchema() throws Exception {
        String originalSchema = factory.getDefaultSchema(factory.getConnectionConfig());
        factory.resetSchema(ConnectionSessionUtil::mockSchema);
        String newSchema = factory.getDefaultSchema(factory.getConnectionConfig());
        assertFalse("Reset schema should not keep the old value", originalSchema.equals(newSchema));
    }

    @Test
    public void testResetParameters() throws Exception {
        Map<String, String> originalParameters = factory.getParameters();
        factory.resetParameters(ConnectionSessionUtil::mockParameters);
        Map<String, String> newParameters = factory.getParameters();
        assertFalse("Reset parameters should not keep the old value", originalParameters.equals(newParameters));
    }

    @Test
    public void testGetPassword() throws Exception {
        String originalPassword = factory.getPassword();
        factory.resetPassword(ConnectionSessionUtil::mockPassword);
        String newPassword = factory.getPassword();
        assertFalse("Get password after resetting should not keep the old value", originalPassword.equals(newPassword));
    }

    @Test
    public void testGetUsername() throws Exception {
        String originalUsername = factory.getUsername();
        factory.resetUsername(ConnectionSessionUtil::mockUsername);
        String newUsername = factory.getUsername();
        assertFalse("Get username after resetting should not keep the old value", originalUsername.equals(newUsername));
    }

    @Test
    public void testGetHost() throws Exception {
        String originalHost = factory.getHost();
        factory.resetHost(ConnectionSessionUtil::mockHost);
        String newHost = factory.getHost();
        assertFalse("Get host after resetting should not keep the old value", originalHost.equals(newHost));
    }

    @Test
    public void testGetPort() throws Exception {
        int originalPort = factory.getPort();
        factory.resetPort(ConnectionSessionUtil::mockPort);
        int newPort = factory.getPort();
        assertFalse("Get port after resetting should not keep the old value", originalPort == newPort);
    }

    @Test
    public void testGetDefaultSchema() throws Exception {
        String originalSchema = factory.getDefaultSchema(factory.getConnectionConfig());
        factory.resetSchema(ConnectionSessionUtil::mockSchema);
        String newSchema = factory.getDefaultSchema(factory.getConnectionConfig());
        assertFalse("Get default schema after resetting should not keep the old value", originalSchema.equals(newSchema));
    }

    @Test
    public void testGetParameters() throws Exception {
        Map<String, String> originalParameters = factory.getParameters();
        factory.resetParameters(ConnectionSessionUtil::mockParameters);
        Map<String, String> newParameters = factory.getParameters();
        assertFalse("Get parameters after resetting should not keep the old value", originalParameters.equals(newParameters));
    }

    @Test
    public void testResetUsernameWithNullMapper() throws Exception {
        factory.resetUsername(null);
        assertNull("Reset username with null mapper should return null", factory.getUsername());
    }

    @Test
    public void testResetPasswordWithNullMapper() throws Exception {
        factory.resetPassword(null);
        assertNull("Reset password with null mapper should return null", factory.getPassword());
    }

    @Test
    public void testResetHostWithNullMapper() throws Exception {
        factory.resetHost(null);
        assertNull("Reset host with null mapper should return null", factory.getHost());
    }

    @Test
    public void testResetPortWithNullMapper() throws Exception {
        factory.resetPort(null);
        assertNull("Reset port with null mapper should return null", factory.getPort());
    }

    @Test
    public void testResetSchemaWithNullMapper() throws Exception {
        factory.resetSchema(null);
        assertNull("Reset schema with null mapper should return null", factory.getDefaultSchema(factory.getConnectionConfig()));
    }

    @Test
    public void testResetParametersWithNullMapper() throws Exception {
        factory.resetParameters(null);
        Map<String, String> parameters = factory.getParameters();
        assertTrue("Get parameters after resetting with null mapper should return empty map", parameters.isEmpty());
    }

    @Test
    public void testGetDataSourceWithInitConnection() throws Exception {
        factory = new OBConsoleDataSourceFactory(new ConnectionConfig("jdbc:test:ds1", DialectType.MYSQL, null), true);
        dataSource = factory.getDataSource();
        connection = dataSource.getConnection();
        try {
            connection.createStatement().executeQuery("SELECT 1 FROM DUAL");
        } finally {
            connection.close();
        }
        assertNotNull("DataSource with initConnection should have a created connection", dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetDataSourceWithInvalidConnection() throws Exception {
        factory = new OBConsoleDataSourceFactory(new ConnectionConfig("jdbc:test:ds1", DialectType.MYSQL, null), false);
        DriverManagerDataSource dataSource = (DriverManagerDataSource) factory.getDataSource();
        dataSource.setDriverClassName("com.invalid.JDBC.Driver");
        dataSource.setUrl("jdbc:test:ds1");
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        assertNull("DataSource with invalid connection should not have a connection", dataSource.getConnection());
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        if (connection != null) {
            connection.close();
        }
        if (dataSource != null) {
            dataSource.close();
        }
    }

}