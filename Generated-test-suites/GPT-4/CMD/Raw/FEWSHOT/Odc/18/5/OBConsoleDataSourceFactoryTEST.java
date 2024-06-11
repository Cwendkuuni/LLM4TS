package com.oceanbase.odc.service.session.factory;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static com.oceanbase.odc.core.shared.constant.DialectType.OB_ORACLE;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;

public class OBConsoleDataSourceFactoryTests {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory factoryUnderTest;
    private OBTenantEndpoint obTenantEndpoint;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        obTenantEndpoint = mock(OBTenantEndpoint.class);
        factoryUnderTest = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        when(connectionConfig.getOBTenantName()).thenReturn("tenantName");
        when(this.connectionConfig.getDialectType()).thenReturn(OB_ORACLE);
        when(connectionConfig.getDbUser()).thenReturn("TestUserName");
        doReturn(obTenantEndpoint).when(connectionConfig).getEndpoint();
        String expectedUsername = "TestUserName@tenantName";
        assertEquals(expectedUsername, factoryUnderTest.getUsername());
    }

    @Test
    public void testGetPassword() {
        when(connectionConfig.getPassword()).thenReturn("TestPassword");
        String expectedPassword = "TestPassword";
        assertEquals(expectedPassword, factoryUnderTest.getPassword());
    }

    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(OB_ORACLE);
        assertEquals(OB_ORACLE, factoryUnderTest.getDialectType());
    }

    // Add more test methods for remaining public methods in OBConsoleDataSourceFactory class.
}