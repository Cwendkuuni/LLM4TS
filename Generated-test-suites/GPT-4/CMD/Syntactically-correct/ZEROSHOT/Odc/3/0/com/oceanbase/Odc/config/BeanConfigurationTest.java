package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

import static org.junit.Assert.*;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void passwordEncoderTest() {
        assertTrue(beanConfiguration.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall instanceof HttpFirewall);
    }
    
    @Test 
    public void cloudMetadataClientTest(){
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void cookieSerializerTest() {
        assertTrue(beanConfiguration.cookieSerializer() instanceof DefaultCookieSerializer);
    }
    
    @Test 
    public void NullCloudMetadataClientTest(){
       BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
       
       assertFalse(client.supportsCloudMetadata());
       assertFalse(client.supportsTenantInstance());
       assertFalse(client.needsOBTenantName());
       assertFalse(client.needsSysTenantUser());
       assertTrue(client.includeClusterNameForJdbcUsername());
       assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
       assertTrue(client.getTenantEndpoint("","") instanceof OBTenantEndpoint);
       assertEquals(OceanBaseAccessMode.DIRECT, client.getTenantEndpoint("","").getAccessMode());
       
       try{
          client.listInstances(); 
       } catch(Exception e){
          assertTrue(e instanceof UnsupportedException);
       }
       
       try{
          client.listTenants("");
       } catch(Exception e){
          assertTrue(e instanceof UnsupportedException);
       }
       
       try{
          client.getTenant("", "");
       } catch(Exception e){
          assertTrue(e instanceof UnsupportedException);
       }
       
       try{
          client.listDatabaseUsers("","");
       } catch(Exception e){
          assertTrue(e instanceof UnsupportedException);
       }
       
       try{
          client.getSysTenantUser("");
       } catch(Exception e){
          assertTrue(e instanceof UnsupportedException);
       }
    } 
}
