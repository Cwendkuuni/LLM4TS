package com.oceanbase.odc.config;

import java.util.List;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import static org.junit.Assert.*;

@SpringBootTest
public class BeanConfigurationTest {

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testPasswordEncoder() {
        PasswordEncoder result = beanConfiguration.passwordEncoder();
        assertNotNull(result);
        assertTrue(result instanceof PasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall result = beanConfiguration.defaultHttpFirewall();
        assertNotNull(result);
        assertTrue(result instanceof HttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
        assertFalse(beanConfiguration.cloudMetadataClient().supportsCloudMetadata());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer result = beanConfiguration.cookieSerializer();
        assertNotNull(result);
        assertTrue(result instanceof CookieSerializer);
    }

}