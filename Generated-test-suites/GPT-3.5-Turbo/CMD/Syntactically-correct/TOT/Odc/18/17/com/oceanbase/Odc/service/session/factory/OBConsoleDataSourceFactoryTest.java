import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("admin");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.MYSQL);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/defaultSchemaName?maxAllowedPacket=64000000&allowMultiQueries=true";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("admin", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clone = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(obConsoleDataSourceFactory, clone);
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(port -> port * 2);
        assertEquals(660, obConsoleDataSourceFactory.getPort().intValue());
    }

    // Add more test cases as needed for edge cases and error scenarios

}