package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration configuration = new BeanConfiguration();
        
        PasswordEncoder encoder = configuration.passwordEncoder();
        
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper()  {
        BeanConfiguration configuration = new BeanConfiguration();
        SensitivePropertyHandler propertyHandler = mock(SensitivePropertyHandler.class);
        
        ObjectMapper mapper = configuration.objectMapper(propertyHandler);
        
        assertNotNull(mapper);
    }

    @Test
    public void testHttpFirewall()  {
        BeanConfiguration configuration = new BeanConfiguration();
        
        HttpFirewall firewall = configuration.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        
        CloudMetadataClient client = configuration.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testSerializer()  {
        BeanConfiguration configuration = new BeanConfiguration();
        
        CookieSerializer serializer = configuration.cookieSerializer();
        
        assertNotNull(serializer);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata()  {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        assertFalse(client.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance()  {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        assertFalse(client.supportsTenantInstance());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        List<OBInstance> instances = client.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        List<OBTenant> tenants = client.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        List<OBDatabaseUser> users = client.listDatabaseUsers("instanceId", "tenantId");
    }    
}