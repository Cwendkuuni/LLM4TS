package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.MockSensitivePropertyHandler;

public class BeanConfigurationTest {
    private BeanConfiguration config = new BeanConfiguration();
    private MockSensitivePropertyHandler sensitivePropertyHandler = new MockSensitivePropertyHandler();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(config.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall firewall = (DefaultHttpFirewall) config.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertTrue(config.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) config.cloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertTrue(client.oceanbaseAccessMode() == OceanBaseAccessMode.DIRECT);

        try {
            client.listInstances();
            fail("Exception should be thrown, because listInstances() is not supported");
        } catch(UnsupportedException e) {
            // Expected exception
        }

        try {
            client.listTenants("id");
            fail("Exception should be thrown, because listTenants() is not supported");
        } catch(UnsupportedException e) {
            // Expected exception
        }

        try {
            client.getTenant("instanceId", "tenantId");
            fail("Exception should be thrown, because getTenant() is not supported");
        } catch(UnsupportedException e) {
            // Expected exception
        }

        try {
            client.listDatabaseUsers("instanceId", "tenantId");
            fail("Exception should be thrown, because listDatabaseUsers() is not supported");
        } catch(UnsupportedException e) {
            // Expected exception
        }

        try {
            client.getSysTenantUser("instanceId");
            fail("Exception should be thrown, because getSysTenantUser() is not supported");
        } catch(UnsupportedException e) {
            // Expected exception
        }
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(config.cookieSerializer());
    }
}