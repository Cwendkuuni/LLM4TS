import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), false);
    }

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:oracle:thin:@localhost:1521:xe";
        assertEquals(expected, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expected = "user";
        assertEquals(expected, dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expected = "password";
        assertEquals(expected, dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
        assertNotSame(dataSourceFactory, clonedFactory);
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
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> 1234;
        dataSourceFactory.resetPort(mapper);
        assertEquals(1234, (int) dataSourceFactory.getPort());
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