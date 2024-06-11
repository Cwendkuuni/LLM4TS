package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeanConfigurationTest {
    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertEquals(encoder.getClass(), BCryptPasswordEncoder.class);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration();
        SensitivePropertyHandler handler = new SensitivePropertyHandler();
        ObjectMapper mapper = config.objectMapper(handler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertNotNull(firewall);
        assertEquals(firewall.getClass(), DefaultHttpFirewall.class);
        assertTrue((firewall instanceof DefaultHttpFirewall));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration config = new BeanConfiguration();
        CloudMetadataClient client = config.cloudMetadataClient();
        assertNotNull(client);
        assertEquals(client.getClass(), NullCloudMetadataClient.class);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration config = new BeanConfiguration();
        CookieSerializer serializer = config.cookieSerializer();
        assertNotNull(serializer);
        assertEquals(serializer.getClass(), DefaultCookieSerializer.class);
    }
}
