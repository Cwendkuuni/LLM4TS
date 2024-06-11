package com.oceanbase.odc.config;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {
    
    // Initialize object of class under test
    BeanConfiguration configuration = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        assertNotNull("Test that PasswordEncoder bean is created", configuration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler mockHandler = Mockito.mock(SensitivePropertyHandler.class);
        assertNotNull("Test that ObjectMapper bean is created", configuration.objectMapper(mockHandler));
    }

    @Test
    public void testObjectMapper_nullSensitivePropertyHandler() {
        try{
            configuration.objectMapper(null);
            fail("Handling null SensitivePropertyHandler is expected to throw an exception");
        } catch (Exception e) {
            // Test Passes
        }
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull("Test that HttpFirewall is created", configuration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull("Test that CloudMetadataClient is created", configuration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull("Test that CookieSerializer bean is created", configuration.cookieSerializer());
    }
}