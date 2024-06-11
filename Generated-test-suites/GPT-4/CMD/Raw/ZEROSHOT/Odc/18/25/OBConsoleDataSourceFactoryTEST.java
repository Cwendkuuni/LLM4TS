
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory dataSourceFactoryUnderTest; 
    private ConnectionConfig mockConfiguration;
    
    @Before
    public void setUp() throws Exception {
        this.mockConfiguration = mock(ConnectionConfig.class);
        this.dataSourceFactoryUnderTest = new OBConsoleDataSourceFactory(mockConfiguration, true);
    }

    @Test
    public void testGetJdbcUrl() {
        Mockito.when(mockConfiguration.getHost()).thenReturn("mysql");
        Mockito.when(mockConfiguration.getPort()).thenReturn(3306);
        Mockito.when(mockConfiguration.getDefaultSchema()).thenReturn("testdb");
        String result = dataSourceFactoryUnderTest.getJdbcUrl();
        assertEquals(result, "mysql://localhost:3306/testdb"); // change expected value as per the correct format of your jdbc url
    }

    @Test
    public void testGetUsername() {
        Mockito.when(mockConfiguration.getUsername()).thenReturn("testuser");
        String result = OBConsoleDataSourceFactory.getUsername(mockConfiguration);
        assertEquals(result, "testuser");
    }

    @Test
    public void testGetPassword() {
        Mockito.when(mockConfiguration.getPassword()).thenReturn("password");
        String result = OBConsoleDataSourceFactory.getPassword(mockConfiguration);
        assertEquals(result, "password");
    }

    @Test
    public void testGetJdbcParams() {
        mockConfiguration = new ConnectionConfig();
        mockConfiguration.setEndpoint(null); // to test the null condition
        mockConfiguration.setSslConfig(null); // to test the null condition
        Map<String, String> result = OBConsoleDataSourceFactory.getJdbcParams(mockConfiguration);
        assertEquals("64000000", result.get("maxAllowedPacket"));
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(dataSourceFactoryUnderTest.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        Mockito.when(mockConfiguration.getDialectType()).thenReturn(DialectType.ORACLE);
        DialectType result = dataSourceFactoryUnderTest.getDialectType();
        assertEquals(DialectType.ORACLE, result);
    }

    @Test
    public void testDeepCopy() {
        assertNotNull(dataSourceFactoryUnderTest.deepCopy());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper mockMapper = value -> value + "updated";
        dataSourceFactoryUnderTest.resetUsername(mockMapper);
        String result = dataSourceFactoryUnderTest.getUsername();
        assertEquals(result, "testuserupdated");
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper mockMapper = value -> value + "updated";
        dataSourceFactoryUnderTest.resetPassword(mockMapper);
        String result = dataSourceFactoryUnderTest.getPassword();
        assertEquals(result, "passwordupdated");
    }

    // Similar tests for resetHost(), resetPort(), resetSchema() and resetParameters() can be added
}
