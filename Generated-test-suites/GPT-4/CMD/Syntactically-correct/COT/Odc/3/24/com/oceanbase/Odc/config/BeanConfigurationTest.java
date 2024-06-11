package com.oceanbase.odc.config;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfig;
    private SensitivePropertyHandler handler;

    @Before
    public void setUp() throws Exception {
        beanConfig = new BeanConfiguration();
        handler = new SensitivePropertyHandler();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfig.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfig.objectMapper(handler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfig.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfig.cookieSerializer());
    }
}