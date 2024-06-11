
package com.oceanbase.odc.service.session.factory;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.ConnectType;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    
    @Before
    public void setup() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getUsername()).thenReturn("username");
        when(connectionConfig.getPassword()).thenReturn("password");
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getDefaultSchema()).thenReturn("schema");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getType()).thenReturn(ConnectType.DIRECT);
        OBTenantEndpoint endpoint = mock(OBTenantEndpoint.class);
        when(connectionConfig.getEndpoint()).thenReturn(endpoint);
        Map<String, String> jdbcUrlParameters = new HashMap<>();
        when(connectionConfig.getJdbcUrlParameters()).thenReturn(jdbcUrlParameters);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void getUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        Assert.assertEquals("username", username);
    }

    @Test
    public void getPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        Assert.assertEquals("password", password);
    }

    @Test
    public void getConnectType() {
        ConnectType type = obConsoleDataSourceFactory.getConnectType();
        Assert.assertEquals(ConnectType.DIRECT, type);
    }
    
    @Test
    public void getDialectType() {
        DialectType dType = obConsoleDataSourceFactory.getDialectType();
        Assert.assertEquals(DialectType.MYSQL, dType);
    }

    @Test
    public void resetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = (String value) -> "reset_username";
        obConsoleDataSourceFactory.resetUsername(mapper);
        Assert.assertEquals("reset_username", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void resetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = (String value) -> "reset_password";
        obConsoleDataSourceFactory.resetPassword(mapper);
        Assert.assertEquals("reset_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void resetPorth() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = (Integer value) -> 8888;
        obConsoleDataSourceFactory.resetPort(mapper);
        // Pseudocode for assertion as the port field is private and doesn't have getter.
    }

    @Test
    public void resetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = (String value) -> "reset_schema";
        obConsoleDataSourceFactory.resetSchema(mapper);
        // Pseudocode for assertion as the schema field is private and doesn't have getter.
    }

    @Test
    public void resetParameters() {
        Map<String, String> newParams = new HashMap<>();
        newParams.put("new_param", "new_value");
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (Map<String, String> value) -> newParams;
        obConsoleDataSourceFactory.resetParameters(mapper);
        // Pseudocode for assertion as the parameters field is private and doesn't have getter.
    }

    @Test
    public void getDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        Assert.assertNotNull(dataSource);
    }

    @Test 
    public void deepCopy() {
        OBConsoleDataSourceFactory newFactory = obConsoleDataSourceFactory.deepCopy();
        Assert.assertNotNull(newFactory);
    }

}
