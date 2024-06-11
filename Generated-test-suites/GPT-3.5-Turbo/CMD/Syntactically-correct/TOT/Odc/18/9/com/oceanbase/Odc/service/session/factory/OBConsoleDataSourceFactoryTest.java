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
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, false, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:3306:testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        String actualJdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        assertTrue(dataSource instanceof SingleConnectionDataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType expectedDialectType = DialectType.OB_ORACLE;
        DialectType actualDialectType = dataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copiedFactory = dataSourceFactory.deepCopy();
        assertNotNull(copiedFactory);
        assertTrue(copiedFactory instanceof OBConsoleDataSourceFactory);
    }

    @Test
    public void testResetUser() {
        ValueMapper<String> mapper = new TestValueMapper<>("newUser");
        dataSourceFactory.resetUsername(mapper);
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ValueMapper<String> mapper = new TestValueMapper<>("newPassword");
        dataSourceFactory.resetPassword(mapper);
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetPort() {
        ValueMapper<Integer> mapper = new TestValueMapper<>(8080);
        dataSourceFactory.resetPort(mapper);
        assertEquals(8080, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ValueMapper<String> mapper = new TestValueMapper<>("newSchema");
        dataSourceFactory.resetSchema(mapper);
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ValueMapper<Map<String, String>> mapper = new TestValueMapper<>(new HashMap<>());
        dataSourceFactory.resetParameters(mapper);
        assertEquals(0, dataSourceFactory.getParameters().size());
    }

    private static class TestValueMapper<T> implements ValueMapper<T> {
        private T value;

        public TestValueMapper(T value) {
            this.value = value;
        }

        @Override
        public T map(T current) {
            return value;
        }
    }

}