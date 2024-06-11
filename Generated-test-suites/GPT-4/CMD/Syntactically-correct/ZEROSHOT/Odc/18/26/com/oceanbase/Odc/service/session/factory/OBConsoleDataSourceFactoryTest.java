
package com.oceanbase.odc.service.session.factory;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.sql.DataSource;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;
    private ConnectionExtensionPoint connectionExtensionPoint;
    private final Boolean autoCommit = true;
    
    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionConfig.getUsername()).thenReturn("testuser");
        when(connectionConfig.getPassword()).thenReturn("testpass");
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetJdbcUrl() {
        when(connectionExtensionPoint.generateJdbcUrl(
            connectionConfig.getHost(), 
            connectionConfig.getPort(),
            connectionConfig.getDefaultSchema(),
            connectionConfig.getJdbcUrlParameters())).thenReturn("jdbc:mysql://localhost:3306/testDB");
        String url = factory.getJdbcUrl();
        Assert.assertEquals("jdbc:mysql://localhost:3306/testDB", url);
    }

    @Test
    public void testGetUsername() {
        String username = factory.getUsername(connectionConfig);
        Assert.assertEquals("testuser", username);
    }

    @Test
    public void testGetPassword() {
        String password = factory.getPassword(connectionConfig);
        Assert.assertEquals("testpass", password);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = factory.getDataSource();
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = factory.getDialectType();
        Assert.assertEquals(DialectType.MYSQL, dialectType);
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory cloneFactory = factory.deepCopy();
        Assert.assertEquals(factory.getUsername(), cloneFactory.getUsername());
        Assert.assertEquals(factory.getPassword(), cloneFactory.getPassword());
        Assert.assertEquals(factory.getDialectType(), cloneFactory.getDialectType());
    }

    @Test
    public void testResetUsername() {
        factory.resetUsername(oldUsername -> oldUsername + "_new");
        Assert.assertEquals("testuser_new", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        factory.resetPassword(oldPassword -> oldPassword + "_new");
        Assert.assertEquals("testpass_new", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        factory.resetHost(oldHost -> oldHost + "_new");
        Assert.assertEquals("localhost_new", connectionConfig.getHost());
    }

    @Test
    public void testResetPort() {
        factory.resetPort(oldPort -> oldPort + 1);
        Assert.assertEquals(Integer.valueOf(3307), connectionConfig.getPort());
    }

    @Test
    public void testResetSchema() {
        factory.resetSchema(oldSchema -> oldSchema + "_new");
        Assert.assertEquals("testDB_new", connectionConfig.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        factory.resetParameters(params -> {
            params.put("newParam", "newValue");
            return params;
        });
        Assert.assertTrue(factory.parameters.containsKey("newParam"));
        Assert.assertEquals("newValue", factory.parameters.get("newParam"));
    }
}
