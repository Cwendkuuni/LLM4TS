package com.oceanbase.odc.config;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.oceanbase.odc.config.BeanConfiguration;

public class BeanConfigurationTest {

    private ApplicationContext context;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(context.getBean("passwordEncoder"));
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(context.getBean("objectMapper"));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(context.getBean("defaultHttpFirewall"));
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(context.getBean("cloudMetadataClient"));
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(context.getBean("cookieSerializer"));
    }

    // tests for NullCloudMetadataClient exceptions
    @Test(expected = UnsupportedException.class)
    public void testListInstancesException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) context.getBean("cloudMetadataClient");
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) context.getBean("cloudMetadataClient");
        nullCloudMetadataClient.listTenants("1");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) context.getBean("cloudMetadataClient");
        nullCloudMetadataClient.getTenant("1", "1");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) context.getBean("cloudMetadataClient");
        nullCloudMetadataClient.listDatabaseUsers("1", "1");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserException() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = (BeanConfiguration.NullCloudMetadataClient) context.getBean("cloudMetadataClient");
        nullCloudMetadataClient.getSysTenantUser("1");
    }
}
