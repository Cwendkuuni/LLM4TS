
package com.oceanbase.odc.service.session.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;

    private final ConnectionConfig connectionConfig = new ConnectionConfig();
    private final Boolean autoCommit = true;

    @Before
    public void setUp() {
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setEndpoint(new OBTenantEndpoint());

        factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_MYSQL, factory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        DataSourceFactory copy = factory.deepCopy();
        assertEquals(factory.getDataSource().getClass(), copy.getDataSource().getClass());
    }

    @Test
    public void testResetUsername() {
        factory.resetUsername((value) -> "newUsername");
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        factory.resetPassword((value) -> "newPassword");
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        factory.resetHost((value) -> "newHost");
        assertEquals("newHost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        factory.resetPort((value) -> 1234);
        assertEquals(1234, factory.getPort());
    }

    @Test
    public void testResetSchema() {
        factory.resetSchema((value) -> "newSchema");
        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        factory.resetParameters((value) -> {
            value.put("newParameter", "value");
            return value;
        });
        assertEquals("value", factory.getParameters().get("newParameter"));
    }

}

