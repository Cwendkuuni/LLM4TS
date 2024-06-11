import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        // Add specific assertions for the generated JDBC URL
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        // Add specific assertions for the generated DataSource
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
        // Add specific assertions for the returned DialectType
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copiedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(copiedFactory);
        // Add specific assertions for the copied factory
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "new_username");
        assertEquals("new_username", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> "new_password");
        assertEquals("new_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "new_host");
        assertEquals("new_host", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 8080);
        assertEquals(8080, obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "new_schema");
        assertEquals("new_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key1", "value1");
        parameters.put("key2", "value2");

        obConsoleDataSourceFactory.resetParameters(value -> parameters);
        assertEquals(parameters, obConsoleDataSourceFactory.getParameters());
    }

    // Add more test cases here if needed

}