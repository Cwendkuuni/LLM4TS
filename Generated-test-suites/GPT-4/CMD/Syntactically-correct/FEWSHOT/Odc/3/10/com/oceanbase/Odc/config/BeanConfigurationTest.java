
package com.oceanbase.odc.config;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BeanConfiguration
 *
 * @author Test Author
 */
public class BeanConfigurationTest {
    
    private BeanConfiguration beanConfig = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof PasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfig.objectMapper();
        assertNotNull(objectMapper);
        assertTrue(objectMapper instanceof ObjectMapper);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfig.defaultHttpFirewall();
        assertNotNull(firewall);
        assertEquals(DefaultHttpFirewall.class, firewall.getClass());
    }
    
    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfig.cloudMetadataClient();
        assertNotNull(client);
        assertEquals(BeanConfiguration.NullCloudMetadataClient.class, client.getClass());
    }
    
    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfig.cookieSerializer();
        assertNotNull(serializer);
        assertTrue(serializer instanceof CookieSerializer);
    }
    
    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfig.new NullCloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertNull(client.listInstances());
        assertNull(client.listTenants("id"));
        assertNull(client.getTenant("id", "tenant"));
        assertNull(client.listDatabaseUsers("id", "tenant"));
        assertEquals(OceanBaseAccessMode.DIRECT, client.getTenantEndpoint("id", "tenant").getAccessMode());
        assertNull(client.getSysTenantUser("id"));
    }
}
