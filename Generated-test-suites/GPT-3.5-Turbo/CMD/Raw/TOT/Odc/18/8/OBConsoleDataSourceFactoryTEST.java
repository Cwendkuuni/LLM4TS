import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_db");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306/test_db?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetJdbcUrlEmptySchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetJdbcUrlWithProxy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setEndpoint(new OBTenantEndpoint("proxyhost", 8080));
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false&socksProxyHost=proxyhost&socksProxyPort=8080";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetJdbcUrlWithSSL() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setSslConfig(new ConnectionConfig.SSLConfig(true));
        connectionConfig.setSslFileEntry(new ConnectionConfig.SSLFileEntry("keystore_path", "keystore_password"));
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:mysql://localhost:3306?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=exception&noDatetimeStringSync=true&jdbcCompliantTruncation=false&useSSL=true&trustServerCertificate=true&keyStore=keystore_path&keyStorePassword=keystore_password";
        assertEquals(expectedUrl, factory.getJdbcUrl());
    }

    // Add more test cases for other methods...

}