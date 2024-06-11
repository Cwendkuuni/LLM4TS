import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getUsername()).thenReturn("user");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getDefaultSchema()).thenReturn("schema");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("new_user");

        obConsoleDataSourceFactory.resetUsername(mapper);
        assertEquals("new_user", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("new_password");

        obConsoleDataSourceFactory.resetPassword(mapper);
        assertEquals("new_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("new_host");

        obConsoleDataSourceFactory.resetHost(mapper);
        assertEquals("new_host", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        when(mapper.map(anyInt())).thenReturn(9999);

        obConsoleDataSourceFactory.resetPort(mapper);
        assertEquals(new Integer(9999), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("new_schema");

        obConsoleDataSourceFactory.resetSchema(mapper);
        assertEquals("new_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        parameters.put("key2", "value2");
        when(mapper.map(anyMap())).thenReturn(parameters);

        obConsoleDataSourceFactory.resetParameters(mapper);
        assertEquals(parameters, obConsoleDataSourceFactory.getParameters());
    }

    // Add more test methods for edge cases and error scenarios

}