import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("root");
        connectionConfig.setPassword("password");
        
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/default_schema";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copiedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(copiedFactory);
        assertNotSame(obConsoleDataSourceFactory, copiedFactory);
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetUsername(mapper);
        // Add assertion here
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPassword(mapper);
        // Add assertion here
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetHost(mapper);
        // Add assertion here
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetPort(mapper);
        // Add assertion here
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetSchema(mapper);
        // Add assertion here
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = mock(CloneableDataSourceFactory.ValueMapper.class);
        obConsoleDataSourceFactory.resetParameters(mapper);
        // Add assertion here
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.SOME_CONNECT_TYPE, obConsoleDataSourceFactory.getConnectType());
    }
}