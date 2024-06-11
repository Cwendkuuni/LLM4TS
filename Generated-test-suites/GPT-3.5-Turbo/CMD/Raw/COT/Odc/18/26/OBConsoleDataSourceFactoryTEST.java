
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Test functionality of getJdbcUrl method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expected = "jdbc:mysql://localhost:3306/test_db?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expected, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        // Test functionality of getUsername method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        String expected = "\"test_user\"";
        assertEquals(expected, OBConsoleDataSourceFactory.getUsername(connectionConfig));
    }

    // Add more test cases for other public methods in OBConsoleDataSourceFactory class

}
