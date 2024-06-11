package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BeanConfigurationTest {

    private BeanConfiguration configuration;

    @Before
    public void setUp() {
        configuration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(configuration.passwordEncoder());
        assertTrue(configuration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    // Similar tests can be created for other public methods

    @Test
    public void testObjectMapper() {
        assertNotNull(configuration.objectMapper(null));
        // additional assertions if necessary 
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(configuration.defaultHttpFirewall());
        // additional assertions if necessary
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(configuration.cloudMetadataClient());
        // additional assertions if necessary
    }
    
    @Test
    public void testCookieSerializer() {
        assertNotNull(configuration.cookieSerializer());
        // additional assertions if necessary
    }

    //@Test for edge cases and exceptions...

}

//