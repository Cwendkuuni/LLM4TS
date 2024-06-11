package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        BCryptPasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

}