package com.oceanbase.odc.config;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    BeanConfiguration beanConfiguration;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HttpFirewall httpFirewall;

    @Autowired
    CloudMetadataClient cloudMetadataClient;

    @Autowired
    CookieSerializer cookieSerializer;

    @Autowired
    NullCloudMetadataClient nullCloudMetadataClient;

    @Test
    public void testNullCloudMetadataClient() {
        assertNotNull(nullCloudMetadataClient);
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.encode("test").startsWith("$2a$10$"));
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
    }

    @Test
    public void testHttpFirewall() {
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}