package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        // Add test cases for objectMapper if needed
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        // Test methods in NullCloudMetadataClient class
        assertNotNull(client);
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
        
        // Add more test cases for other methods in NullCloudMetadataClient if needed
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new DefaultCookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

}