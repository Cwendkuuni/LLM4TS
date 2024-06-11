import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        // Add specific assertion if needed
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        // Add specific assertion if needed
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
        // Add specific assertion if needed
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(clonedDataSourceFactory);
        // Add specific assertion if needed
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(String::toUpperCase);
        assertEquals(obConsoleDataSourceFactory.getUsername(), obConsoleDataSourceFactory.getUsername().toUpperCase());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(String::toLowerCase);
        assertEquals(obConsoleDataSourceFactory.getPassword(), obConsoleDataSourceFactory.getPassword().toLowerCase());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(String::toUpperCase);
        assertEquals(obConsoleDataSourceFactory.getHost(), obConsoleDataSourceFactory.getHost().toUpperCase());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(p -> p + 1);
        assertEquals(obConsoleDataSourceFactory.getPort().intValue(), obConsoleDataSourceFactory.getPort() + 1);
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(String::toLowerCase);
        assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), obConsoleDataSourceFactory.getDefaultSchema().toLowerCase());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(m -> {
            m.put("key1", "value1");
            return m;
        });
        assertEquals(obConsoleDataSourceFactory.getParameters().get("key1"), "value1");
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
        assertNotNull(connectType);
        // Add specific assertion if needed
    }
}