package com.oceanbase.odc.service.session.factory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    @Mock
    private ConnectionConfig connectionConfig;
    @Mock
    private ConnectionExtensionPoint connectionExtensionPoint;

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        when(connectionExtensionPoint.getDriverClassName()).thenReturn("com.mysql.jdbc.Driver");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
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
        assertNotNull(dataSourceFactory.deepCopy());
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "new_host");
        assertEquals("new_host", dataSourceFactory.getUsername());        
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 8080);
        assertEquals(Integer.valueOf(8080), dataSourceFactory.getPort());        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortNegativeValue() {
        dataSourceFactory.resetPort(value -> -8080);       
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "new_schema");
        assertEquals("new_schema", dataSourceFactory.getSchema());        
    }

    @Test
    public void testResetParameters() {
        Map<String, String> newParams = Collections.singletonMap("newKey", "newValue");
        dataSourceFactory.resetParameters(value -> newParams);
        assertEquals(newParams, dataSourceFactory.getParameters());        
    }
}