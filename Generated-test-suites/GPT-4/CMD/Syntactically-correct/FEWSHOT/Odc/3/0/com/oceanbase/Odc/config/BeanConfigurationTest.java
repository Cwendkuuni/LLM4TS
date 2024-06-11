package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    @Test
    public void passwordEncoderTest() {
        BeanConfiguration configuration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = configuration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(passwordEncoder.encode("test123"), passwordEncoder.encode("test123"));
    }

    @Test
    public void objectMapperTest() {
        BeanConfiguration configuration = new BeanConfiguration();
        ObjectMapper objectMapper = configuration.objectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);
        assertTrue(objectMapper.canSerialize(TestClass.class));
    }

    @Test
    public void defaultHttpFirewallTest() {
        BeanConfiguration configuration = new BeanConfiguration();
        HttpFirewall httpFirewall = configuration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertEquals("DefaultHttpFirewall", httpFirewall.getClass().getSimpleName());
    }

    @Test
    public void cloudMetadataClientTest() {
        BeanConfiguration configuration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertThrows(UnsupportedException.class, cloudMetadataClient::listInstances);
    }

    @Test
    public void cookieSerializerTest() {
        BeanConfiguration configuration = new BeanConfiguration();
        CookieSerializer cookieSerializer = configuration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("DefaultCookieSerializer", cookieSerializer.getClass().getSimpleName());
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}