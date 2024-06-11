import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    ConnectionConfig connectionConfig;
    Boolean autoCommit = true;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_MYSQL);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertEquals("jdbc:mysql://localhost:3306/default_schema?maxAllowedPacket=64000000", jdbcUrl);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
        assertEquals(DialectType.OB_MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedFactory);
        assertNotSame(obConsoleDataSourceFactory, clonedFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(s -> s + "_test");
        String username = obConsoleDataSourceFactory.getUsername();
        assertEquals("username_test", username);
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(s -> s + "_test");
        String password = obConsoleDataSourceFactory.getPassword();
        assertEquals("password_test", password);
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(s -> "newhost");
        String host = obConsoleDataSourceFactory.getHost();
        assertEquals("newhost", host);
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(i -> 8080);
        Integer port = obConsoleDataSourceFactory.getPort();
        assertEquals(Integer.valueOf(8080), port);
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(s -> "new_schema");
        String schema = obConsoleDataSourceFactory.getSchema();
        assertEquals("new_schema", schema);
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "value2");
        
        obConsoleDataSourceFactory.resetParameters(map -> new HashMap<>(params));
        Map<String, String> parameters = obConsoleDataSourceFactory.getParameters();
        assertEquals(params, parameters);
    }
}