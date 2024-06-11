
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig config;
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        config = Mockito.mock(ConnectionConfig.class);
        
        Mockito.when(config.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        Mockito.when(config.getUsername()).thenReturn("username");
        Mockito.when(config.getPassword()).thenReturn("password");
        Mockito.when(config.getHost()).thenReturn("localhost");

        factory = new OBConsoleDataSourceFactory(config, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/databaseName";
        String actualJdbcUrl = factory.getJdbcUrl();
        
        Assert.assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }
    
    @Test
    public void testGetUsername() {
        String expectedUsername = "username";
        String actualUsername = OBConsoleDataSourceFactory.getUsername(config);
        
        Assert.assertEquals(expectedUsername, actualUsername);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPort_negativeValue() {
        factory.resetPort(newValue -> -10); // This is supporsed to throw IllegalArgumentException
    }

    // More of such test cases to go here...

}
