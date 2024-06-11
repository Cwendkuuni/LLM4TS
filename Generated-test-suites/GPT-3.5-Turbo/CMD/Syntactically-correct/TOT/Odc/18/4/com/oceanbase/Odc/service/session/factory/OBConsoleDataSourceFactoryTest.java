import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=false";
        assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertNotSame(dataSourceFactory, clonedFactory);
        assertEquals(dataSourceFactory.getDialectType(), clonedFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newUsername";
        dataSourceFactory.resetUsername(mapper);
        assertEquals("newUsername", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newPassword";
        dataSourceFactory.resetPassword(mapper);
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newHost";
        dataSourceFactory.resetHost(mapper);
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> 1234;
        dataSourceFactory.resetPort(mapper);
        assertEquals(1234, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newSchema";
        dataSourceFactory.resetSchema(mapper);
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParameters = new HashMap<>();
        newParameters.put("key1", "value1");
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newParameters;
        dataSourceFactory.resetParameters(mapper);
        assertEquals(newParameters, dataSourceFactory.getParameters());
    }

}