package com.oceanbase.odc.config;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    BeanConfiguration beanConfiguration;
    BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
        nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListInstancesException() {
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListTenantsException() {
        nullCloudMetadataClient.listTenants("fakeInstanceId");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTenantException() {
        nullCloudMetadataClient.getTenant("fakeInstanecId", "fakeTenantId");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListDatabaseUsersException() {
        nullCloudMetadataClient.listDatabaseUsers("fakeInstanceId", "fakeTenantId");
    }

    @Test
    public void testGetTenantEndpoint() {
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("fakeInstanceID", "fakeTenantId"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSysTenantUserException() {
        nullCloudMetadataClient.getSysTenantUser("fakeInstanceId");
    }
}