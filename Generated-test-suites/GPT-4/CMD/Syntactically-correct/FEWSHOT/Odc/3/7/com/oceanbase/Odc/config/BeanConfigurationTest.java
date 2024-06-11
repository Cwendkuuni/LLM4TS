
package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfig = new BeanConfiguration();
    private NullCloudMetadataClient nullCloudMetaClient = new BeanConfiguration.NullCloudMetadataClient();
    
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfig.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfig.objectMapper(null);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfig.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall.getFirewallConfig().isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfig.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfig.cookieSerializer();
        assertEquals(cookieSerializer.getCookieName(), "JSESSIONID");
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertFalse(nullCloudMetaClient.supportsCloudMetadata());
        assertFalse(nullCloudMetaClient.supportsTenantInstance());
        assertFalse(nullCloudMetaClient.needsOBTenantName());
        assertFalse(nullCloudMetaClient.needsSysTenantUser());
        assertTrue(nullCloudMetaClient.includeClusterNameForJdbcUsername());
        assertEquals(nullCloudMetaClient.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
        assertThrows(UnsupportedException.class, nullCloudMetaClient::listInstances);
        assertThrows(UnsupportedException.class, () -> nullCloudMetaClient.listTenants("testInstanceId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetaClient.getTenant("testInstanceId", "testTenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetaClient.listDatabaseUsers("testInstanceId", "testTenantId"));
        assertThrows(UnsupportedException.class, () -> nullCloudMetaClient.getSysTenantUser("testInstanceId"));
    }
}
