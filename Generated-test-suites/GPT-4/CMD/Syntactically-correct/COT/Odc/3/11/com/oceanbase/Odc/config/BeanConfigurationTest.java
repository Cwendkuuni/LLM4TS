package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {
    BeanConfiguration beanConfiguration = new BeanConfiguration();
    
    @Test
    public void testPasswordEncoder() {
        assertEquals(BCryptPasswordEncoder.class, beanConfiguration.passwordEncoder().getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperWithNullInput() {
        beanConfiguration.objectMapper(null);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertEquals(DefaultHttpFirewall.class, beanConfiguration.defaultHttpFirewall().getClass());
    }

    @Test
    public void testCloudMetadataClient() {
        assertEquals(BeanConfiguration.NullCloudMetadataClient.class, beanConfiguration.cloudMetadataClient().getClass());
    }

    @Test
    public void testCookieSerializer() {
        assertEquals(DefaultCookieSerializer.class, beanConfiguration.cookieSerializer().getClass());
        assertEquals("JSESSIONID", ((DefaultCookieSerializer)beanConfiguration.cookieSerializer()).getCookieName());
    }
}