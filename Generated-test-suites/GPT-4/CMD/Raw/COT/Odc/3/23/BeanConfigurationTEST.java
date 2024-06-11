
package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeanConfigurationTest
{
    BeanConfiguration bC = new BeanConfiguration();

    @Test
    public void TestPasswordEncoder(){
        assertNotNull(bC.passwordEncoder());
    }
    
    @Test
    public void TestObjectMapper(){
        SensitivePropertyHandler handler = new SensitivePropertyHandler();
        assertNotNull(bC.objectMapper(handler));
    }
    
    @Test
    public void TestNullCloudMetadataClient(){
        BeanConfiguration.NullCloudMetadataClient client = bC.new NullCloudMetadataClient();
        assertFalse(client.needsOBTenantName());
    }
    
    @Test(expected=UnsupportedException.class)
    public void TestExceptionInNullCloudMetadataClient(){
        BeanConfiguration.NullCloudMetadataClient client = bC.new NullCloudMetadataClient();
        client.getTenant("id", "tenantId");
    }
    
    @Test(expected=NullPointerException.class)
    public void TestObjectMapperWithNullSensitivePropertyHandler(){
        bC.objectMapper(null);
    }
    
    @Test
    public void TestDefaultHttpFirewall(){
        assertNotNull(bC.defaultHttpFirewall());
    }
    
    @Test
    public void TestCloudMetadataClient(){
        assertNotNull(bC.cloudMetadataClient());
    }
    
    @Test
    public void TestCookieSerializer(){
        assertNotNull(bC.cookieSerializer());
    }    
}
