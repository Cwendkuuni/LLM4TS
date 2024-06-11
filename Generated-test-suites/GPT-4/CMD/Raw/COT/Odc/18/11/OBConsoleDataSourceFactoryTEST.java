
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

import javax.sql.DataSource;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testOBConsoleDataSourceFactory() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        assertNotNull(oBConsoleDataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        String jdbcUrl = oBConsoleDataSource.getJdbcUrl();
    
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("testuser");
    
        String username = OBConsoleDataSourceFactory.getUsername(config);
    
        assertEquals("testuser", username);
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        config.setPassword("password");
    
        String password = OBConsoleDataSourceFactory.getPassword(config);
    
        assertEquals("password", password);
    }

    @Test
    public void testGetJdbcParams() {
        ConnectionConfig config = new ConnectionConfig();
        Map<String, String> params = OBConsoleDataSourceFactory.getJdbcParams(config);
    
        assertNotNull(params);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        DataSource ds = oBConsoleDataSource.getDataSource();
    
        assertNotNull(ds);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setDialectType(DialectType.ORACLE);
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        DialectType dt = oBConsoleDataSource.getDialectType();
    
        assertEquals(DialectType.ORACLE, dt);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        CloneableDataSourceFactory clone = oBConsoleDataSource.deepCopy();
    
        assertNotNull(clone);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setUsername("testuser");
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        oBConsoleDataSource.resetUsername(s -> s + "new");
    
        assertEquals("testusernew", oBConsoleDataSource.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setPassword("password");
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        oBConsoleDataSource.resetPassword(s -> s + "new");
    
        assertEquals("passwordnew", oBConsoleDataSource.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setHost("localhost");
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        oBConsoleDataSource.resetHost(s -> s + ".com");
    
        assertEquals("localhost.com", oBConsoleDataSource.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setPort(8080);
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        oBConsoleDataSource.resetPort(i -> i + 1000);
    
        assertEquals(new Integer(9080), oBConsoleDataSource.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setDefaultSchema("testschema");
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        oBConsoleDataSource.resetSchema(s -> s + "new");
    
        assertEquals("testschemasnew", oBConsoleDataSource.getSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig config = new ConnectionConfig();
        Boolean autoCommit = true;
        config.setParameters(new HashMap<>());
        OBConsoleDataSourceFactory oBConsoleDataSource = new OBConsoleDataSourceFactory(config, autoCommit);
    
        HashMap<String, String> newParams = new HashMap<>();
        newParams.put("key", "value");
        oBConsoleDataSource.resetParameters(map -> newParams);
    
        assertEquals("value", oBConsoleDataSource.getParameters().get("key"));
    }

    @Test
    public void testGetDefaultSchema() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDefaultSchema("testschema");
        config.setDialectType(DialectType.ORACLE);
        String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(config);
    
        assertEquals("\"testschema\"", defaultSchema);
    }
}
