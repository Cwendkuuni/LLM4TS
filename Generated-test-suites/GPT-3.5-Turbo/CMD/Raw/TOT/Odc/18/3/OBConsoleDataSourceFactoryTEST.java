import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.HashMap;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDefaultSchema("testSchema");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("param1", "value1");
        parameters.put("param2", "value2");
        connectionConfig.setJdbcUrlParameters(parameters);

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?param1=value1&param2=value2";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedFactory = obConsoleDataSourceFactory.deepCopy();
        assertEquals(clonedFactory.getClass(), OBConsoleDataSourceFactory.class);
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
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 1234);
        assertEquals(1234, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        HashMap<String, String> newParams = new HashMap<>();
        newParams.put("newParam1", "newValue1");
        obConsoleDataSourceFactory.resetParameters(value -> newParams);
        assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
    }
}