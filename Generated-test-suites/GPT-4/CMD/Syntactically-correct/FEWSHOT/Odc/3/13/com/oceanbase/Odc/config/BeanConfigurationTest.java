
package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = (DefaultCookieSerializer) beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClientSupports() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();

        client.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();

        client.listTenants("instanceId");
    }

    // You can add more tests as per your requirement.
}
