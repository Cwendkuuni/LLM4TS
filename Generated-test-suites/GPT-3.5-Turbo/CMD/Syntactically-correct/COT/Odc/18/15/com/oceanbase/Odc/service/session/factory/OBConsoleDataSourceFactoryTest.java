import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;

    @Test
    public void testOBConsoleDataSourceFactoryConstructor() {
        try {
            OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
            assertNotNull(factory);
        } catch (Exception e) {
            fail("Constructor test failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, false);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.contains("jdbc:mysql"));
        assertTrue(jdbcUrl.contains("zeroDateTimeBehavior"));
    }

    // Add more basic test cases for other public methods in the OBConsoleDataSourceFactory class

}