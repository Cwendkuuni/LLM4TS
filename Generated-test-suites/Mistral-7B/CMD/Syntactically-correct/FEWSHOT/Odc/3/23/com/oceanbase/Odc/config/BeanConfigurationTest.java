package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test cases for the BeanConfiguration class
 */
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BeanConfiguration().passwordEncoder();
        String password = "password";
        String encodedPassword = encoder.encode(password);
        boolean isMatched = encoder.matches("password", encodedPassword);
        assertTrue(isMatched);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration(sensitivePropertyHandler).objectMapper(sensitivePropertyHandler);
        // Add test cases for objectMapper methods here
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        // Add test cases for defaultHttpFirewall methods here
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        // Add test cases for cloudMetadataClient methods here
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
        // Add test cases for NullCloudMetadataClient methods here
    }
}