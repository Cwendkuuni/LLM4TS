import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:oracle:thin:@localhost:1521:TESTUSER";
        String actualUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialect = DialectType.OB_ORACLE;
        DialectType actualDialect = dataSourceFactory.getDialectType();
        assertEquals(expectedDialect, actualDialect);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertNotNull(clonedDataSourceFactory);
        assertEquals(dataSourceFactory.getJdbcUrl(), clonedDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newUser";
        dataSourceFactory.resetUsername(mapper);
        assertEquals("newUser", dataSourceFactory.getUsername());
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
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> 8888;
        dataSourceFactory.resetPort(mapper);
        assertEquals(8888, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = value -> "newSchema";
        dataSourceFactory.resetSchema(mapper);
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> {
            Map<String, String> newParams = new HashMap<>();
            newParams.put("key1", "value1");
            newParams.put("key2", "value2");
            return newParams;
        };
        dataSourceFactory.resetParameters(mapper);
        assertEquals("value1", dataSourceFactory.getParameters().get("key1"));
        assertEquals("value2", dataSourceFactory.getParameters().get("key2"));
    }

}