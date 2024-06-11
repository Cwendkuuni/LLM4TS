package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {
    BeanConfiguration config = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sph = mock(SensitivePropertyHandler.class);
        ObjectMapper mapper = config.objectMapper(sph);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(((DefaultHttpFirewall)firewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = config.cloudMetadataClient();
        assertNotNull(client);

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());

        assertThrows(UnsupportedException.class, () -> client.listInstances());
        assertThrows(UnsupportedException.class, () -> client.listTenants("instanceId"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = config.cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer)serializer).getCookieName());
    }
}