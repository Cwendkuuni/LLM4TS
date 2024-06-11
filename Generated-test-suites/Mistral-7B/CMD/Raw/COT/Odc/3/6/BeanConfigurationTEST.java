package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    

    // Testing methods with no return type

    @Test
    public void testInitNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = applicationContext.getBean(NullCloudMetadataClient.class);
        assertNotNull(nullCloudMetadataClient);
    }

    // Testing methods that return an object

    @Test
    public void testPasswordEncoderMethod() {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperMethod() {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewallMethod() {
        HttpFirewall defaultHttpFirewall = applicationContext.getBean(HttpFirewall.class);
        assertNotNull(defaultHttpFirewall);
    }

    // Testing methods that throw exceptions

    @Test(expected = UnsupportedException.class)
    public void testListInstancesMethod() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsMethod() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantMethod() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersMethod() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserMethod() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        cloudMetadataClient.getSysTenantUser("instanceId");
    }

    
}