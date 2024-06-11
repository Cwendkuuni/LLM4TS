
package com.oceanbase.odc.config;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import static org.junit.Assert.*;

public class BeanConfigurationTest {
    BeanConfiguration config;
    
    @Before
    public void setup() throws Exception {
        config = new BeanConfiguration();
    }
    
    @Test
    public void passwordEncoderTest() {
        assertNotNull(config.passwordEncoder());
        assertTrue(config.passwordEncoder() instanceof BCryptPasswordEncoder);
    }
    
    @Test
    public void objectMapperTest() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler() {
            // Stubbed instance
        };
        assertNotNull(config.objectMapper(sensitivePropertyHandler));
    }
    
    @Test
    public void defaultHttpFirewallTest() {
        assertNotNull(config.defaultHttpFirewall());
        assertTrue(config.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }
    
    @Test
    public void cloudMetadataClientTest() {
        assertNotNull(config.cloudMetadataClient());
        assertFalse(config.cloudMetadataClient().supportsCloudMetadata());
        assertFalse(config.cloudMetadataClient().supportsTenantInstance());
        assertFalse(config.cloudMetadataClient().needsOBTenantName());
        assertFalse(config.cloudMetadataClient().needsSysTenantUser());
        assertTrue(config.cloudMetadataClient().includeClusterNameForJdbcUsername());
    }

    @Test
    public void cookieSerializerTest() {
        assertNotNull(config.cookieSerializer());
        assertTrue(config.cookieSerializer() instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) config.cookieSerializer()).getCookieName());
    }

    @After
    public void tearDown() throws Exception {
        config = null;
    }
}
