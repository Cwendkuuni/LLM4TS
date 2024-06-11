package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // To Test, we need to mock SensitivePropertyHandler since it's not defined in the provided class. 
        // Consider it is properly mocked:
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class); 
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
        assertTrue(beanConfiguration.objectMapper(sensitivePropertyHandler) instanceof ObjectMapper);
    }
    
    @Test
    public void testHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
        assertTrue(beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
        assertTrue(beanConfiguration.cookieSerializer() instanceof DefaultCookieSerializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClient_withUnsupportedMethod() {
        BeanConfiguration.NullCloudMetadataClient ncmc = new BeanConfiguration().new NullCloudMetadataClient();
        // methods that throw UnsupportedException
        ncmc.listInstances();
        ncmc.listTenants("");
        ncmc.getTenant("", "");
        ncmc.listDatabaseUsers("", "");
        ncmc.getSysTenantUser("");
    }
}
