import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("testUser");
        connectionConfig.setPassword("testPassword");
        connectionConfig.setOBTenantName("testOBTenantName");
        connectionConfig.setTenantName("testTenantName");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:1521:\"testUser\"@testOBTenantName";
        assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(dataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copiedFactory = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
        assertNotNull(copiedFactory);
        assertEquals(dataSourceFactory.getJdbcUrl(), copiedFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(oldValue -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(oldValue -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(oldValue -> 3306);
        assertEquals(3306, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(oldValue -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters(oldValue -> {
            oldValue.put("newParam", "newValue");
            return oldValue;
        });
        assertEquals("newValue", dataSourceFactory.getJdbcParams().get("newParam"));
    }

}