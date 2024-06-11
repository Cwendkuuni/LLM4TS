package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
    }
    
    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertTrue(beanConfiguration.objectMapper(sensitivePropertyHandler) instanceof ObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertTrue(beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cs = beanConfiguration.cookieSerializer();
        assertTrue(cs instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", cs.getCookieName());
    }
}