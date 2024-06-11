package com.oceanbase.odc.config;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.OBInstance;

import java.util.List;

public class BeanConfigurationTest {
    
    private static SensitivePropertyHandler sensitivePropertyHandler;
    private static BeanConfiguration beanConfiguration;
        
    @BeforeClass
    public static void setup(){
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        beanConfiguration = new BeanConfiguration();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonSupportingMethods(){
        BeanConfiguration.NullCloudMetadataClient nullClient = beanConfiguration.new NullCloudMetadataClient();
        nullClient.listInstances();
    }

    @Test
    public void testSupportingMethods(){
        BeanConfiguration.NullCloudMetadataClient nullClient = beanConfiguration.new NullCloudMetadataClient();
        
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, nullClient.oceanbaseAccessMode());
        Assert.assertFalse(nullClient.needsOBTenantName());
    }
   
   @Test
    public void testObjectMapper(){
        Assert.assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }
    
    @Test
    public void testPasswordEncoder(){
        Assert.assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testDefaultHttpFirewall(){
        Assert.assertNotNull(beanConfiguration.defaultHttpFirewall());
    }
    
    @Test
    public void testCloudMetadataClient(){
        Assert.assertNotNull(beanConfiguration.cloudMetadataClient());
    }
    
    @Test
    public void testCookieSerializer(){
        Assert.assertNotNull(beanConfiguration.cookieSerializer());
    }
}