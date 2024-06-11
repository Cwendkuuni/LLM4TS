import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("test_schema");
        when(connectionConfig.getUsername()).thenReturn("test_user");
        when(connectionConfig.getPassword()).thenReturn("test_password");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema";
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertTrue(clonedFactory instanceof OBConsoleDataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> "new_user";
        obConsoleDataSourceFactory.resetUsername(mapper);
        assertEquals("new_user", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> "new_password";
        obConsoleDataSourceFactory.resetPassword(mapper);
        assertEquals("new_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> "new_host";
        obConsoleDataSourceFactory.resetHost(mapper);
        assertEquals("new_host", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = port -> 5432;
        obConsoleDataSourceFactory.resetPort(mapper);
        assertEquals(5432, obConsoleDataSourceFactory.getPort().intValue());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> "new_schema";
        obConsoleDataSourceFactory.resetSchema(mapper);
        assertEquals("new_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = map -> {
            Map<String, String> newParams = new HashMap<>();
            newParams.put("param1", "value1");
            return newParams;
        };
        obConsoleDataSourceFactory.resetParameters(mapper);
        assertEquals("value1", obConsoleDataSourceFactory.getParameters().get("param1"));
    }
}