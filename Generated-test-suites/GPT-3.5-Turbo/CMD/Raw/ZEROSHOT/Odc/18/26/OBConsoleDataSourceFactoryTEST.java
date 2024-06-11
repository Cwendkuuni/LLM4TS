import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertEquals("jdbc:mysql://localhost:3306/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false", jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.MYSQL);

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertEquals("user", username);
    }

    // Add more test methods as needed

}