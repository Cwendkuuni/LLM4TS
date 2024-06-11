package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.oceanbase.odc.common.json.JacksonFactory;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfig;

    @Mock
    private SensitivePropertyHandler mockSensitivePropertyHandler;

    @Before
    public void setup() {
        beanConfig = new BeanConfiguration();
        mockSensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(JacksonFactory.unsafeJsonMapper());
        assertNotNull(beanConfig.objectMapper(mockSensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfig.defaultHttpFirewall();
        assertNotNull(firewall);
        assertTrue(firewall instanceof DefaultHttpFirewall);
    }

    // More tests for other methods...
}