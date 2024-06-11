package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import javax.validation.constraints.NotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Test
    public void testPasswordEncoder() {
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertTrue(matches);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNull(cloudMetadataClient.getSysTenantUser(null));
        assertNull(cloudMetadataClient.getTenant(null, null));
        assertNull(cloudMetadataClient.getTenantEndpoint(null, null));
        assertNull(cloudMetadataClient.getDatabaseUsers(null, null));
        assertArrayEquals(new OBInstance[0], cloudMetadataClient.listInstances().toArray());
        assertArrayEquals(new OBTenant[0], cloudMetadataClient.listTenants(null).toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullCloudMetadataClientGetSysTenantUser() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient nullCloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.getSysTenantUser(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullCloudMetadataClientGetTenant() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient nullCloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.getTenant(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient nullCloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.getTenantEndpoint(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullCloudMetadataClientGetDatabaseUsers() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient nullCloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.getDatabaseUsers(null, null);
    }

    @Test
    public void testCloudMetadataClientListInstances() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient cloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        List<OBInstance> instances = cloudMetadataClient.listInstances();
        assertNotNull(instances);
    }

    @Test
    public void testCloudMetadataClientListTenants() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient cloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        List<OBTenant> tenants = cloudMetadataClient.listTenants("instanceId");
        assertNotNull(tenants);
    }

    @Test
    public void testCloudMetadataClientGetTenant() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient cloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNotNull(tenant);
    }

    @Test
    public void testCloudMetadataClientGetTenantEndpoint() {
        @SuppressWarnings("unchecked")
        CloudMetadataClient cloudMetadataClient = (CloudMetadataClient) new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        OBTenantEndpoint tenantEndpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(tenantEndpoint);
    }

}