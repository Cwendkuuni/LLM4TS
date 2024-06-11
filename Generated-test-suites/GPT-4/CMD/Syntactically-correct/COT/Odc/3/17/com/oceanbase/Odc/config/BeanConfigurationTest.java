package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        this.beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
        assertNotNull(objectMapper);
        // Here you could add more assertions to test the specific configurations of the ObjectMapper
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
        assertTrue(((DefaultHttpFirewall)httpFirewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
    }
    
    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer)cookieSerializer).getCookieName());
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupportedOperations() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        
        nullCloudMetadataClient.listInstances();
        nullCloudMetadataClient.listTenants("instanceId");
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }

}
