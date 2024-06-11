import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPass");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("testSchema");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory deepCopy = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(deepCopy);
        assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), ((OBConsoleDataSourceFactory) deepCopy).getJdbcUrl());
    }
}