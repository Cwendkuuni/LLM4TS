package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // You can add more test cases here for ObjectMapper
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
        
        try {
            cloudMetadataClient.listInstances();
            cloudMetadataClient.listTenants("instanceId");
            cloudMetadataClient.getTenant("instanceId", "tenantId");
            cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
            cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
            cloudMetadataClient.getSysTenantUser("instanceId");
            fail("Expected UnsupportedException");
        } catch (UnsupportedException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        assertNotNull(cookieSerializer);
    }

}