package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws JsonProcessingException {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
    }
    
    // When exception methods are called, UnsupportedException is expected.
    @Test(expected = UnsupportedException.class)
    public void testListInstances_exception() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        List<OBInstance> instances = client.listInstances();
    }
    
    @Test(expected=UnsupportedException.class)
    public void testListTenants_exception() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        List<OBTenant> tenants = client.listTenants("testInstanceId");
    }
    
    @Test(expected=UnsupportedException.class)
    public void testGetTenant_exception() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        OBTenant tenant = client.getTenant("testInstanceId", "testTenantId");
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers_exception() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        List<OBDatabaseUser> users = client.listDatabaseUsers("testInstanceId", "testTenantId");
    }
    
    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser_exception() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        OBDatabaseUser user = client.getSysTenantUser("testInstanceId");
    }
}