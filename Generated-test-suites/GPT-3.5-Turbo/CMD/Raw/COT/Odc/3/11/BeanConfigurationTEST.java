
package com.oceanbase.odc.config;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
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

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        nullCloudMetadataClient.init();
        assertNotNull(nullCloudMetadataClient);
    }
}
