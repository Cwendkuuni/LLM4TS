package com.oceanbase.odc.config;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    private final BeanConfiguration beanConfiguration = new BeanConfiguration();
    private final NullCloudMetadataClient nullClient = beanConfiguration.new NullCloudMetadataClient();

    @Test
    public void testPasswordEncoderShouldReturnBCryptPasswordEncoder() {
        Assert.assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testDefaultHttpFirewall() {
        Assert.assertTrue(beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCookieSerializerShouldReturnDefaultCookieSerializer() {
        Assert.assertTrue(beanConfiguration.cookieSerializer() instanceof DefaultCookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        Assert.assertFalse(nullClient.supportsCloudMetadata());
        Assert.assertFalse(nullClient.supportsTenantInstance());
        Assert.assertFalse(nullClient.needsOBTenantName());
        Assert.assertFalse(nullClient.needsSysTenantUser());
        Assert.assertTrue(nullClient.includeClusterNameForJdbcUsername());

        OBTenantEndpoint endpoint = nullClient.getTenantEndpoint("test_instance", "test_tenant");
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());

        Assert.assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());

        // These methods throw an exception, so we're testing whether the exception is thrown
        try {
            nullClient.listInstances();
            Assert.fail("Expected exception to be thrown");
        } catch (UnsupportedException e) {
            // expected result
        }

        try {
            nullClient.listTenants("test_instance");
            Assert.fail("Expected exception to be thrown");
        } catch (UnsupportedException e) {
            // expected result
        }

        try {
            nullClient.getTenant("test_instance", "test_tenant");
            Assert.fail("Expected exception to be thrown");
        } catch (UnsupportedException e) {
            // expected result
        }

        try {
            nullClient.listDatabaseUsers("test_instance", "test_tenant");
            Assert.fail("Expected exception to be thrown");
        } catch (UnsupportedException e) {
            // expected result
        }

        try {
            nullClient.getSysTenantUser("test_instance");
            Assert.fail("Expected exception to be thrown");
        } catch (UnsupportedException e) {
            // expected result
        }
    }

}
