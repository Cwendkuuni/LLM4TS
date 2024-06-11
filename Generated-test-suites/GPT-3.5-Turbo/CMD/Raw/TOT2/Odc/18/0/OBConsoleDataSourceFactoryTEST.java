import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("root");

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:mysql://"));
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copiedFactory = dataSourceFactory.deepCopy();
        assertNotNull(copiedFactory);
        assertTrue(copiedFactory instanceof OBConsoleDataSourceFactory);
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

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortWithNegativeValue() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> -3306;
        dataSourceFactory.resetPort(mapper);
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
        assertEquals("value1", dataSourceFactory.getJdbcParams().get("key1"));
        assertEquals("value2", dataSourceFactory.getJdbcParams().get("key2"));
    }

    // Add more test cases for edge cases and error scenarios

}