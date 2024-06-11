package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BeanConfigurationTest {

    @InjectMocks
    BeanConfiguration beanConfiguration;

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup(){
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws Exception {
        when(sensitivePropertyHandler.decrypt(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }
}
