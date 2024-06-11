package com.oceanbase.odc.config;

import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        Assert.assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        Assert.assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        Assert.assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        Assert.assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        Assert.assertNotNull(beanConfiguration.cookieSerializer());
    }
}
