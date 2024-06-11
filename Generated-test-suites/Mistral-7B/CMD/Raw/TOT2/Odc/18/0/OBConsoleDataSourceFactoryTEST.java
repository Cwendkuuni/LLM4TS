import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DataSourceFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.dbcp2.BasicDataSource.DEFAULT_AUTO_COMMIT;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private DataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("testuser");
        connectionConfig.setPassword("testpassword");
        connectionConfig.setDefaultSchema("test_schema");
        Map<String, String> parameters = new HashMap<>();
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        dataSource = dataSourceFactory.getDataSource();
    }

    @Test
    public void testGetInstance() {
        OBConsoleDataSourceFactory newDataSourceFactory = new OBConsoleDataSourceFactory(dataSourceFactory.getConnectionConfig(), dataSourceFactory.isAutoCommit(), dataSourceFactory.isInitConnection());
        Assert.assertNotNull(newDataSourceFactory);
        Assert.assertEquals(dataSourceFactory.getConnectionConfig(), newDataSourceFactory.getConnectionConfig());
        Assert.assertEquals(dataSourceFactory.isAutoCommit(), newDataSourceFactory.isAutoCommit());
        Assert.assertEquals(dataSourceFactory.isInitConnection(), newDataSourceFactory.isInitConnection());
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?autoReconnect=true&autoCommitBatchSize=1&autoCommitBatchTime=1000&allowMultiQueries=true&connectTimeout=5000&maxAllowedPacket=64000000&zeroDateTimeBehavior=0&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=";
        Assert.assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        Assert.assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() throws SQLException {
        OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        Assert.assertNotNull(clonedDataSourceFactory);
        Assert.assertNotSame(dataSourceFactory, clonedDataSourceFactory);
        Assert.assertEquals(dataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        Assert.assertEquals(dataSourceFactory.isAutoCommit(), clonedDataSourceFactory.isAutoCommit());
        Assert.assertEquals(dataSourceFactory.isInitConnection(), clonedDataSourceFactory.isInitConnection());
        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
        Assert.assertNotSame(dataSource, clonedDataSource);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "testuser2";
        dataSourceFactory.resetUsername(s -> s.replace("testuser", newUsername));
        Assert.assertEquals(newUsername, dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "testpassword2";
        dataSourceFactory.resetPassword(s -> s.replace("testpassword", newPassword));
        Assert.assertEquals(newPassword, dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "localhost2";
        dataSourceFactory.resetHost(s -> s.replace("localhost", newHost));
        Assert.assertEquals(newHost, dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 3307;
        dataSourceFactory.resetPort(i -> i + 1);
        Assert.assertEquals(newPort, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "test_schema2";
        dataSourceFactory.resetSchema(s -> s.replace("test_schema", newSchema));
        Assert.assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        String newParameter = "newParameter=newValue";
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put(newParameter, "newValue");
        dataSourceFactory.resetParameters(m -> {
            m.put(newParameter, "newValue");
            return m;
        });
        Map<String, String> parameters = dataSourceFactory.getParameters();
        Assert.assertEquals(newParameters, parameters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(null, false, true);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
        connection.close();
    }

    @Test
    public void testConnectionInitializer() throws SQLException {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), false, true);
        List<ConnectionInitializer> initializers = dataSourceFactory.getConnectionExtensionPoint().getConnectionInitializers();
        PoolingDataSource poolingDataSource = (PoolingDataSource) new BasicDataSourceFactory().createDataSource();
        poolingDataSource.setDriverClassName("org.h2.Driver");
        poolingDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        initializers.forEach(initializer -> poolingDataSource.addInitializer(initializer));
        DataSource dataSource = poolingDataSource;
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testDefaultZeroDateTimeBehavior() {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), false, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?autoReconnect=true&autoCommitBatchSize=1&autoCommitBatchTime=1000&allowMultiQueries=true&connectTimeout=5000&maxAllowedPacket=64000000&zeroDateTimeBehavior=0&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=";
        Assert.assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testSSLConfig() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSslConfig(new SSLConfig(true, true, "test.p12", "test123"));
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?autoReconnect=true&autoCommitBatchSize=1&autoCommitBatchTime=1000&allowMultiQueries=true&connectTimeout=5000&maxAllowedPacket=64000000&zeroDateTimeBehavior=0&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&allowLoadLocalInfile=false&allowUrlInLocalInfile=false&allowLoadLocalInfileInPath=" +
                "&useSSL=true&trustServerCertificate=true&trustStore=test.p12&trustStorePassword=test123";
        Assert.assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDriverClassName() {
        String driverClassName = dataSourceFactory.getConnectionExtensionPoint().getDriverClassName();
        Assert.assertEquals("com.mysql.jdbc.Driver", driverClassName);
    }

    @Test
    public void testClone() throws SQLException, CloneNotSupportedException {
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), false, true);
        OBConsoleDataSourceFactory clonedDataSourceFactory = (OBConsoleDataSourceFactory) dataSourceFactory.clone();
        Assert.assertNotNull(clonedDataSourceFactory);
        Assert.assertNotSame(dataSourceFactory, clonedDataSourceFactory);
        Assert.assertEquals(dataSourceFactory.getConnectionConfig(), clonedDataSourceFactory.getConnectionConfig());
        Assert.assertEquals(dataSourceFactory.isAutoCommit(), clonedDataSourceFactory.isAutoCommit());
        Assert.assertEquals(dataSourceFactory.isInitConnection(), clonedDataSourceFactory.isInitConnection());
        DataSource clonedDataSource = clonedDataSourceFactory.getDataSource();
        Assert.assertNotSame(dataSource, clonedDataSource);
    }
}