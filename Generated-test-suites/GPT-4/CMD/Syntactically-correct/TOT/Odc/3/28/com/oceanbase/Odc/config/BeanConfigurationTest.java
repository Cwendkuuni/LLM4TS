package com.oceanbase.odc.config;

import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper(){
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.objectMapper(new SensitivePropertyHandler()));
    }

    @Test
    public void testDefaultHttpFirewall(){
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient(){
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testNullCloudMetadataClientMethods(){
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        assertEquals(client.supportsCloudMetadata(), false);
        assertEquals(client.includeClusterNameForJdbcUsername(), true);
        //...similar tests for the rest of the methods in the NullCloudMetadataClient class
    }
    
    @Test
    public void testCookieSerializer(){
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        assertNotNull(beanConfiguration.cookieSerializer());
    }
 
}