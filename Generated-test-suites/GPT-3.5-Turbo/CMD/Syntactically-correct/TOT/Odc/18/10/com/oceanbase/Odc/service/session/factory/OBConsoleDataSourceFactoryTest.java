import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setup() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getUsername()).thenReturn("testUser");
        when(connectionConfig.getPassword()).thenReturn("testPassword");
        when(connectionConfig.getDefaultSchema()).thenReturn("testSchema");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:3306/testSchema";
        assertEquals(expectedUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory deepCopy = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(obConsoleDataSourceFactory, deepCopy);
        assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), ((OBConsoleDataSourceFactory) deepCopy).getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 5432);
        assertEquals("jdbc:mysql://localhost:5432/testSchema", obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(value -> {
            value.put("newParam", "newValue");
            return value;
        });
        assertTrue(obConsoleDataSourceFactory.getJdbcUrl().contains("newParam=newValue"));
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetConnectionExtensionPoint() {
        assertNotNull(obConsoleDataSourceFactory.getConnectionExtensionPoint());
    }

    @Test
    public void testInitConnection() {
        assertTrue(obConsoleDataSourceFactory.isInitConnection());
    }

    @Test
    public void testGetParameters() {
        assertNotNull(obConsoleDataSourceFactory.getParameters());
    }

    @Test
    public void testNullConnectionConfigUsername() {
        assertThrows(NullPointerException.class, () -> new OBConsoleDataSourceFactory(null, true));
    }

    @Test
    public void testNegativePort() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getPort()).thenReturn(-1);
        assertThrows(IllegalArgumentException.class, () -> new OBConsoleDataSourceFactory(connectionConfig, true));
    }

}