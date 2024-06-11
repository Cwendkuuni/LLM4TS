import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/user";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertNotSame(factory, copy);
        assertEquals(factory.getDataSource().getClass(), copy.getDataSource().getClass());
    }

    @Test
    public void testResetUsername() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        factory.resetUsername(value -> value + "_updated");
        assertEquals("user_updated", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        factory.resetPassword(value -> value + "_updated");
        assertEquals("password_updated", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        factory.resetHost(value -> "newhost");
        assertEquals("newhost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        factory.resetPort(value -> value + 10);
        assertEquals(3316, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        factory.resetSchema(value -> value + "_updated");
        assertEquals("defaultSchema_updated", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig("localhost", 3306, "user", "password", DialectType.MYSQL), true);
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        factory.resetParameters(value -> params);
        assertEquals(params, factory.getParameters());
    }

}