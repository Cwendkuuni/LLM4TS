import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Test if the generated JDBC URL is correct
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306/dbname?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        // Test if the DataSource is successfully created
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNotNull(factory.getDataSource());
    }

    // Add more test cases for other methods in the OBConsoleDataSourceFactory class

}