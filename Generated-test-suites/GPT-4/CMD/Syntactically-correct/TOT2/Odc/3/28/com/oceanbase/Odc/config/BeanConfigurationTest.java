package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    BeanConfiguration beanConfig;
    SensitivePropertyHandler propHandler;

    @Before
    public void setup() {
        beanConfig = new BeanConfiguration();
        propHandler = Mockito.mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfig.objectMapper(propHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfig.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall instanceof DefaultHttpFirewall);
    }
    
    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfig.cloudMetadataClient());
        assertTrue(beanConfig.cloudMetadataClient() instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfig.cookieSerializer());
        assertTrue(beanConfig.cookieSerializer() instanceof DefaultCookieSerializer);
    }
}
