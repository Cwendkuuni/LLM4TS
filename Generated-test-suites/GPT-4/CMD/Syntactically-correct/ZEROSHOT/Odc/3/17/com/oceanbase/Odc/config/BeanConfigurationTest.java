
package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    private BeanConfiguration.NullCloudMetadataClient cloudMetadataClient;

    @Before
    public void setUp() throws Exception {
        cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // SensitivePropertyHandler class needed for testing should be mocked
        // This is just a placeholder
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
    }
}
