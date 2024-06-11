import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test?maxAllowedPacket=64000000&allowMultiQueries=true"
                + "&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true"
                + "&jdbcCompliantTruncation=false&useSSL=false";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUserName("user");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("\"user\"@OD_Tenant_1#Cluster_1", factory.getUsername());
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals("password", factory.getPassword());
    }

    // Add more test cases for other methods in the OBConsoleDataSourceFactory class

}