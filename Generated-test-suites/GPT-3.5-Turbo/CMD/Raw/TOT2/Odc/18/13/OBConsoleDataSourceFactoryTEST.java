import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;

    @Before
    public void setUp() {
        // Initialize the factory with necessary parameters for testing
        // For example, create a dummy ConnectionConfig object
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        // Test if the getDataSource method returns a non-null DataSource object
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        // Test the generated JDBC URL against an expected value
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_db";
        String actualJdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, actualJdbcUrl);
    }

    // Add more test cases for other public methods in OBConsoleDataSourceFactory

}