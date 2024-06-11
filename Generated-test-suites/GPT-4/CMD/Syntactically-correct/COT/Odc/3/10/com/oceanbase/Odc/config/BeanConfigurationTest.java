package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        this.beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    } 

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler handler = Mockito.mock(SensitivePropertyHandler.class);
        assertNotNull(beanConfiguration.objectMapper(handler));
    } 

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    } 

    @Test
    public void testCloudMetadataClient() {
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    } 

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    } 
}