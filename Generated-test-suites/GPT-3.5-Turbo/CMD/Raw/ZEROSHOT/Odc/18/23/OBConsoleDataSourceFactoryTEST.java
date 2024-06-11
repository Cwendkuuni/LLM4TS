import static org.junit.Assert.*;

import javax.sql.DataSource;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory dataSourceFactory;
    
    @Before
    public void setUp() {
        dataSourceFactory = new OBConsoleDataSourceFactory();
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = dataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername((String username) -> "newUsername");
        assertEquals("newUsername", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword((String password) -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost((String host) -> "newHost");
        assertEquals("newHost", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort((Integer port) -> 1234);
        assertEquals(1234, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema((String schema) -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters((Map<String, String> params) -> {
            params.put("key", "value");
            return params;
        });
        assertEquals("value", dataSourceFactory.getParameters().get("key"));
    }
}