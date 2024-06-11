package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    private BeanConfiguration configuration;
    private SensitivePropertyHandler handler;

    @Before
    public void setUp() {
        configuration = new BeanConfiguration();
        handler = Mockito.mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = configuration.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = configuration.objectMapper(handler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = configuration.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) configuration.cloudMetadataClient();
        assertNotNull(client);
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

        String instanceId = "instance";
        String tenantId = "tenant";
        List<OBInstance> instances = client.listInstances();
        List<OBTenant> tenants = client.listTenants(instanceId);
        OBTenant tenant = client.getTenant(instanceId, tenantId);

        assertTrue(instances.isEmpty());
        assertTrue(tenants.isEmpty());
        assertEquals(OceanBaseAccessMode.DIRECT, tenant.getAccessMode());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = configuration.cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", serializer.getCookieName());
    }
}