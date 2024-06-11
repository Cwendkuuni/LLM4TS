import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
        factory.setConnectionExtensionPoint(connectionExtensionPoint);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mock://localhost:3306/test";
        when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap())).thenReturn(expectedJdbcUrl);

        String jdbcUrl = factory.getJdbcUrl();
        assertEquals(expectedJdbcUrl, jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = DialectType.OB_ORACLE;
        when(connectionConfig.getDialectType()).thenReturn(dialectType);

        assertEquals(dialectType, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = factory.deepCopy();
        assertNotNull(clonedFactory);
    }

    @Test
    public void testResetUsername() {
        String newUsername = "newUsername";
        factory.resetUsername(oldValue -> newUsername);
        assertEquals(newUsername, factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        String newPassword = "newPassword";
        factory.resetPassword(oldValue -> newPassword);
        assertEquals(newPassword, factory.getPassword());
    }

    @Test
    public void testResetHost() {
        String newHost = "newHost";
        factory.resetHost(oldValue -> newHost);
        assertEquals(newHost, factory.getHost());
    }

    @Test
    public void testResetPort() {
        int newPort = 9999;
        factory.resetPort(oldValue -> newPort);
        assertEquals(newPort, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        String newSchema = "newSchema";
        factory.resetSchema(oldValue -> newSchema);
        assertEquals(newSchema, factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("key", "value");
        factory.resetParameters(oldValue -> newParams);
        assertEquals(newParams, factory.getParameters());
    }
}