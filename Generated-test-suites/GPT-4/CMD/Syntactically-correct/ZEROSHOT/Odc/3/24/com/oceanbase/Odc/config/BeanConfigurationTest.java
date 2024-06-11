package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertTrue(objectMapper instanceof JacksonFactory.UnsafeJsonMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(httpFirewall instanceof DefaultHttpFirewall);
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
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) cookieSerializer).getCookieName());
    }

    @Test
    public void testNullCloudMetadataClientUnsupportedMethods() {
        BeanConfiguration.NullCloudMetadataClient client = beanConfiguration.new NullCloudMetadataClient();

        try {
            client.listInstances();
            fail("Expected UnsupportedException not thrown");
        } catch (UnsupportedException e) {}

        try {
            client.listTenants("");
            fail("Expected UnsupportedException not thrown");
        } catch (UnsupportedException e) {}

        try {
            client.getTenant("", "");
            fail("Expected UnsupportedException not thrown");
        } catch (UnsupportedException e) {}

        try {
            client.listDatabaseUsers("","");
            fail("Expected UnsupportedException not thrown");
        } catch (UnsupportedException e) {}

        try {
            client.getSysTenantUser("");
            fail("Expected UnsupportedException not thrown");
        } catch (UnsupportedException e) {}
    }
}
