package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BeanConfiguration.class})
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testPasswordEncoderBean() {
        assertNotNull(context.getBean("passwordEncoder"));
    }

    @Test
    public void testObjectMapperBean() {
        assertNotNull(context.getBean("objectMapper"));
    }
    
    @Test
    public void testDefaultHttpFirewallBean() {
        assertNotNull(context.getBean("defaultHttpFirewall"));
    }
    
    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient client = context.getBean(CloudMetadataClient.class);
        assertNotNull(client);
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertFalse(client.needsSysTenantUser());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertNotNull(client.getTenantEndpoint("", ""));
        try {
            client.listDatabaseUsers("", "");
            fail("Expected an UnsupportedException to be thrown");
        } catch (UnsupportedException e) {
           // Success
        }
    }
    
    @Test
    public void testCookieSerializerBean() {
        assertNotNull(context.getBean("cookieSerializer"));
    }
}