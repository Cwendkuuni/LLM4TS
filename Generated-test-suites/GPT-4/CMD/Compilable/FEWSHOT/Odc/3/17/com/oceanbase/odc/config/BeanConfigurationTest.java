package com.oceanbase.odc.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.springframework.security.web.firewall.HttpFirewall;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.session.web.http.CookieSerializer;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {
    
    @Autowired
    private BeanConfiguration beanConfig;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfig.objectMapper(null);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfig.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfig.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfig.cookieSerializer();
        assertNotNull(cookieSerializer);
    }
}