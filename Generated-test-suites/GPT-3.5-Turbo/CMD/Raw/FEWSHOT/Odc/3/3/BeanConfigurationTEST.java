package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mockito;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfig = new BeanConfiguration();
        assertEquals("JSESSIONID", beanConfig.cookieSerializer().getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

}