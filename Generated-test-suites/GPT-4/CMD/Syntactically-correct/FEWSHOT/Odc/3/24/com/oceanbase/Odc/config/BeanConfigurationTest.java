package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {
    
    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall httpFirewall;
    private CloudMetadataClient cloudMetadataClient;
    private CookieSerializer cookieSerializer;
    
    @Test
    public void testPasswordEncoder() {
        passwordEncoder = new BeanConfiguration().passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }
    
    @Test
    public void testObjectMapper() {
        // Assuming sensitivePropertyHandler instance is already available
        objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
    	httpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
    }
    
    @Test
    public void testCloudMetadataClient() {
        cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint(anyString, anyString);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }
    
    @Test
    public void testCookieSerializer() {
        cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer)cookieSerializer).getCookieName());
    }
}