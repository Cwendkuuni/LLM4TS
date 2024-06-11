
package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(this.beanConfiguration.passwordEncoder());
        assertTrue(this.beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(this.beanConfiguration.objectMapper(sensitivePropertyHandler));
        assertTrue(this.beanConfiguration.objectMapper(sensitivePropertyHandler) instanceof ObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(this.beanConfiguration.defaultHttpFirewall());
        assertTrue(this.beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);

        DefaultHttpFirewall firewall = (DefaultHttpFirewall) this.beanConfiguration.defaultHttpFirewall();
        assertTrue(firewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(this.beanConfiguration.cloudMetadataClient());
        assertTrue(this.beanConfiguration.cloudMetadataClient() instanceof CloudMetadataClient);

        CloudMetadataClient client = this.beanConfiguration.cloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(this.beanConfiguration.cookieSerializer());
        assertTrue(this.beanConfiguration.cookieSerializer() instanceof DefaultCookieSerializer);

        DefaultCookieSerializer cookieSerializer = (DefaultCookieSerializer) this.beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}
