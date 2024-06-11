
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

import java.util.Map;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("test");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.MYSQL);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:mysql://localhost:3306/test?useSSL=false";
        String actual = obConsoleDataSourceFactory.getJdbcUrl();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUsername() {
        String expected = "test";
        String actual = OBConsoleDataSourceFactory.getUsername(connectionConfig);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPassword() {
        String expected = "password";
        String actual = OBConsoleDataSourceFactory.getPassword(connectionConfig);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetJdbcParams() {
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(params.containsKey("maxAllowedPacket"));
        assertTrue(params.containsKey("allowMultiQueries"));
        assertTrue(params.containsKey("nullable"));
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(copy);
    }

    // Test edge cases and exception 

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_with_negative_port_numbers() {
        obConsoleDataSourceFactory.resetPort(value -> -1);
    }
    
    @Test
    public void testResetHost_with_empty_string() {
        obConsoleDataSourceFactory.resetHost(value -> "");
        assertEquals("", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetParameters_with_empty_map() {
        obConsoleDataSourceFactory.resetParameters(value -> new HashMap<>());
        assertTrue(obConsoleDataSourceFactory.getParameters().isEmpty());
    }

}
