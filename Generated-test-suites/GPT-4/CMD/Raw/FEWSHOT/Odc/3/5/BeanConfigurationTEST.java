
package com.oceanbase.odc.config;

import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Test;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration();
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = config.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall httpFirewall = config.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration config = new BeanConfiguration();
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient)  config.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration config = new BeanConfiguration();
        CookieSerializer serializer = config.cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());

        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
        
        OBTenantEndpoint endpoint = client.getTenantEndpoint("testInstance", "testTenant");
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }
}
