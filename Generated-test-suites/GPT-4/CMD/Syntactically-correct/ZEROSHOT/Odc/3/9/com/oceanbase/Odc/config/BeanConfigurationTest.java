
package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    
    @Before
    public void setUp(){
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = new SensitivePropertyHandler();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper(){
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = (DefaultHttpFirewall) beanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = (DefaultCookieSerializer) beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", defaultCookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullClient = beanConfiguration.new NullCloudMetadataClient();

        assertFalse(nullClient.supportsCloudMetadata());
        assertFalse(nullClient.supportsTenantInstance());
        assertFalse(nullClient.needsOBTenantName());
        assertFalse(nullClient.needsSysTenantUser());
        assertTrue(nullClient.includeClusterNameForJdbcUsername());

        assertThrows(UnsupportedException.class, () -> {nullClient.listInstances();});
        assertThrows(UnsupportedException.class, () -> {nullClient.listTenants("test");});
        assertThrows(UnsupportedException.class, () -> {nullClient.getTenant("test", "test");});
        assertThrows(UnsupportedException.class, () -> {nullClient.listDatabaseUsers("test", "test");});
        assertThrows(UnsupportedException.class, () -> {nullClient.getSysTenantUser("test");});

        OBTenantEndpoint tenantEndpoint = nullClient.getTenantEndpoint("test", "test");
        assertNotNull(tenantEndpoint);
    }
}
