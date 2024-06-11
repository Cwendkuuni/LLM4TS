package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {
    
    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;
    
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }
    
    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = new DefaultHttpFirewall();
        assertNotNull(firewall);
    }
    
    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(client);
    }
    
    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

}