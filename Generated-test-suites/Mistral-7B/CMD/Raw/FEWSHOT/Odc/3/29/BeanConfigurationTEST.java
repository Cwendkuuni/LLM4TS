package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration(null);
        ObjectMapper objectMapper = config.objectMapper(new SensitivePropertyHandler());
        String jsonString = "{\"username\": \"test\", \"password\": \"password\"}";
        OBDatabaseUser user = objectMapper.readValue(jsonString, OBDatabaseUser.class);
        assertEquals("test", user.getUsername());
        assertNull(user.getPassword());
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall defaultHttpFirewall = config.defaultHttpFirewall();
        String url = "http://example.com/%25";
        assertTrue(defaultHttpFirewall.allowUrl(url));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration config = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = config.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertFalse(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
        assertNull(cloudMetadataClient.listInstances());
        assertNull(cloudMetadataClient.listTenants(""));
        assertNull(cloudMetadataClient.getTenant("", ""));
        assertNull(cloudMetadataClient.listDatabaseUsers("", ""));
        assertNull(cloudMetadataClient.getTenantEndpoint("", ""));
        assertNull(cloudMetadataClient.getSysTenantUser(""));
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration config = new BeanConfiguration();
        CookieSerializer cookieSerializer = config.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }
}