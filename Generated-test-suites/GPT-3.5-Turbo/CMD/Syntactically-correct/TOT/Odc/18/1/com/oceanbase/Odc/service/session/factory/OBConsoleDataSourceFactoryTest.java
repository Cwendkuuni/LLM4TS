import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(dataSourceFactory.getDialectType(), connectionConfig.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        DataSourceFactory copy = dataSourceFactory.deepCopy();
        assertNotNull(copy);
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUsername");
        assertEquals(dataSourceFactory.getUsername(), "newUsername");
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals(dataSourceFactory.getPassword(), "newPassword");
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals(dataSourceFactory.getHost(), "newHost");
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 1234);
        assertEquals(dataSourceFactory.getPort(), Integer.valueOf(1234));
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals(dataSourceFactory.getDefaultSchema(), "newSchema");
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters(value -> {
            value.put("key", "value");
            return value;
        });
        assertEquals(dataSourceFactory.getParameters().get("key"), "value");
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

}