package com.oceanbase.odc.config;

import static org.junit.jupiter.api.Assertions.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.UnsupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @BeforeEach
    public void setUp() {
        this.beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertTrue(this.beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(this.beanConfiguration.objectMapper(null));
        assertTrue(this.beanConfiguration.objectMapper(null).getRegisteredModuleIds().contains(JacksonModules.CustomOutputSerializer.class.getName()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertTrue(this.beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = this.beanConfiguration.cloudMetadataClient();
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
        assertThrows(UnsupportedException.class, client::listInstances);
        assertThrows(UnsupportedException.class, () -> client.listTenants(null));
        assertThrows(UnsupportedException.class, () -> client.getTenant(null, null));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers(null, null));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser(null));
    }

    @Test
    public void testCookieSerializer() {
        assertEquals(this.beanConfiguration.cookieSerializer().readCookieValues(new MockHttpServletRequest()), new DefaultCookieSerializer().readCookieValues(new MockHttpServletRequest()));
    }
}
