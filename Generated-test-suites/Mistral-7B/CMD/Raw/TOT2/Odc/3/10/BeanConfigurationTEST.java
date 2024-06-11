package com.oceanbase.odc.config;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        String originalPassword = "password123";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);

        assertEquals(originalPassword, decodedPassword);
    }

    @Test
    public void testObjectMapper() throws JsonProcessingException {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        String sensitiveData = "sensitive data";

        String jsonString = "{\"sensitiveData\": \"" + sensitiveData + "\"}";

        ObjectMapper expectedObjectMapper = Mockito.spy(JacksonFactory.unsafeJsonMapper());
        Mockito.when(expectedObjectMapper.writeValueAsString(Mockito.anyObject())).thenReturn(jsonString);
        Mockito.when(sensitivePropertyHandler.decrypt("encryptedSensitiveData")).thenReturn(sensitiveData);

        Object actualObject = objectMapper.readValue(jsonString, Object.class);
        Object expectedObject = expectedObjectMapper.readValue(jsonString, Object.class);

        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();

        String allowedUrl = "http://example.com";
        String disallowedUrl1 = "http://malicious.com/<script>";
        String disallowedUrl2 = "http://malicious.com/%22%61%6d%65%3D%63%61%6C%6C%65%3D%63%68%65%63%74%65%2F";

        assertTrue(defaultHttpFirewall.allowUrlPatterns(allowedUrl));
        assertFalse(defaultHttpFirewall.allowUrlPatterns(disallowedUrl1));
        assertFalse(defaultHttpFirewall.allowUrlPatterns(disallowedUrl2));
    }

    @Test
    public void testCloudMetadataClient() {
        // TODO: Add test cases for the CloudMetadataClient
    }

    @Test
    public void testCookieSerializer() {
        // TODO: Add test cases for the CookieSerializer
    }
}