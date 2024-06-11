package com.oceanbase.odc.config;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {
    
    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    // Test BeanConfiguration class
    @Test
    public void testBeanConfiguration() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration);
    }
    
    // Test passwordEncoder method
    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof PasswordEncoder);
    }

    // Test objectMapper method
    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertTrue(objectMapper instanceof ObjectMapper);
    }
}
