
package com.oceanbase.odc.config;

import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

import org.junit.Test;
import org.junit.Assert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration configuration = new BeanConfiguration();
        Assert.assertTrue(configuration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration configuration = new BeanConfiguration();
        Assert.assertTrue(configuration.objectMapper(new SensitivePropertyHandlerStub()) instanceof JacksonModules.CustomOutputSerializer);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration configuration = new BeanConfiguration();
        DefaultHttpFirewall firewall = (DefaultHttpFirewall) configuration.defaultHttpFirewall();
        Assert.assertTrue(firewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        Assert.assertTrue(configuration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration configuration = new BeanConfiguration();
        DefaultCookieSerializer expected = new DefaultCookieSerializer();
        DefaultCookieSerializer result = (DefaultCookieSerializer) configuration.cookieSerializer();
        Assert.assertEquals(expected.getCookieName(), result.getCookieName());
    }
    
    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();

        Assert.assertFalse(client.needsSysTenantUser());
        Assert.assertFalse(client.needsOBTenantName());
        Assert.assertFalse(client.supportsTenantInstance());
        Assert.assertFalse(client.supportsCloudMetadata());
        Assert.assertTrue(client.includeClusterNameForJdbcUsername());
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
    }
    
    private class SensitivePropertyHandlerStub implements SensitivePropertyHandler {
        @Override
        public String decrypt(String value) {
            return value;
        }
    }
}
