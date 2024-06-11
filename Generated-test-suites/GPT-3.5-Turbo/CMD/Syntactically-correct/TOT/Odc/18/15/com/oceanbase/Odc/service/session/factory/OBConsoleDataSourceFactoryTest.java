import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testDb?zeroDateTimeBehavior=convertToNull";
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
        DialectType expectedDialectType = DialectType.MYSQL;
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertEquals(expectedDialectType, dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(obConsoleDataSourceFactory, clonedFactory);
        assertEquals(obConsoleDataSourceFactory.getDialectType(), clonedFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(str -> StringUtils.reverse(str));
        assertEquals(StringUtils.reverse("username"), obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(str -> StringUtils.reverse(str));
        assertEquals(StringUtils.reverse("password"), obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(str -> StringUtils.reverse(str));
        assertEquals(StringUtils.reverse("localhost"), obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(port -> port + 1);
        assertEquals(Integer.valueOf(3307), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(str -> StringUtils.reverse(str));
        assertEquals(StringUtils.reverse("testSchema"), obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        obConsoleDataSourceFactory.resetParameters(map -> Collections.emptyMap());
        Map<String, String> parameters = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);
        assertTrue(parameters.isEmpty());
    }
}