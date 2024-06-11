
package com.oceanbase.odc.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void passwordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(passwordEncoder);
    }

    @Test
    public void objectMapper() {
        // Please replace with an instance of SensitivePropertyHandler
        SensitivePropertyHandler sensitivePropertyHandler = null;
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        Assert.assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(httpFirewall);
    }

    @Test
    public void cloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(cloudMetadataClient);
    }

    @Test
    public void cookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().new NullCloudMetadataClient();
        Assert.assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        Assert.assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        Assert.assertFalse(nullCloudMetadataClient.needsOBTenantName());
        Assert.assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        Assert.assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        try {
            nullCloudMetadataClient.listInstances();
        } catch (UnsupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }

        try {
            nullCloudMetadataClient.listTenants("");
        } catch (UnsupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }

        try {
            nullCloudMetadataClient.getTenant("", "");
        } catch (UnsupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }

        try {
            nullCloudMetadataClient.listDatabaseUsers("", "");
        } catch (UnsupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }

        try {
            nullCloudMetadataClient.getSysTenantUser("");
        } catch (UnsupportedException e) {
            Assert.assertNotNull(e.getMessage());
        }

    }

}
