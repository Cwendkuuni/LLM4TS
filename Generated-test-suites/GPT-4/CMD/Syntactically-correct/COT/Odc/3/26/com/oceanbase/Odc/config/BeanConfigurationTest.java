
package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
        assertNotNull(customOutputSerializer);
    }

    @Test
    public void testHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test(expected=UnsupportedException.class)
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
        List instances = client.listInstances();
    }
    
    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

}
