package com.oceanbase.odc.config;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.firewall.HttpFirewall;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.springframework.session.web.http.CookieSerializer;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }
    
    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperWithNullPropertyHandler() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientException() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}