
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("username");
        connectionConfig.setPassword("password");
        connectionConfig.setPort(3306);
        connectionConfig.setHost("localhost");
        connectionConfig.setDialectType(DialectType.MYSQL);
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        assertNotNull("The jdbc url should not be null", dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("The username should be correct", "username", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("The password should be correct", "password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull("The Data source should not be null", dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals("The dialect type should be correct", DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
        assertEquals("Cloned object should have the same properties as original",
                dataSourceFactory.getJdbcUrl(), clonedDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = v -> "new_" + v;
        dataSourceFactory.resetUsername(valueMapper);
        assertEquals("The username should be reset correctly", "new_username", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = v -> "new_" + v;
        dataSourceFactory.resetPassword(valueMapper);
        assertEquals("The password should be reset correctly", "new_password", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> originalParams = new HashMap<>(dataSourceFactory.getJdbcParams(connectionConfig));
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = v -> {
            Map<String, String> newParameters = new HashMap<>();
            v.forEach((key, value) -> {
                newParameters.put(key, "new_" + value);
            });
            return newParameters;
        };
        dataSourceFactory.resetParameters(valueMapper);
        Map<String, String> newParameters = dataSourceFactory.getJdbcParams(connectionConfig);
        originalParams.forEach((key, value) -> assertEquals("The parameter should be reset correctly",
                "new_" + value, newParameters.get(key)));
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> valueMapper = v -> "new_" + v;
        dataSourceFactory.resetSchema(valueMapper);
        assertEquals("The default schema should be reset correctly",
                "new_" + dataSourceFactory.getDefaultSchema(connectionConfig), dataSourceFactory.getJdbcUrl());
    }
}
