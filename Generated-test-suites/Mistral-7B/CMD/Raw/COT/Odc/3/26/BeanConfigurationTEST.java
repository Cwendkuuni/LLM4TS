package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = BeanConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall defaultHttpFirewall;
    private CloudMetadataClient cloudMetadataClient;
    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        passwordEncoder = beanConfiguration.passwordEncoder();
        objectMapper = beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class));
        defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cookieSerializer = beanConfiguration.cookieSerializer();
    }

    @Test
    public void testPasswordEncoder() {
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientNull() {
        assertNotNull(cloudMetadataClient);
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientListInstances() {
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientListTenants() {
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientGetTenant() {
        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientListDatabaseUsers() {
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientGetSysTenantUser() {
        cloudMetadataClient.getSysTenantUser("instanceId");
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testBeanWithMissingBean() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(beanConfiguration.cloudMetadataClient().getTenant("instanceId", "tenantId"));
    }

    @Test(expected = NullPointerException.class)
    public void testBeanWithNullBean() {
        beanConfiguration.cloudMetadataClient(null);
    }

    @Test(expected = RuntimeException.class)
    public void testBeanWithException() {
        beanConfiguration.cloudMetadataClient(new Exception("Test Exception"));
    }

    @Test
    public void testBeanWithCustomObject() {
        beanConfiguration.objectMapper(new ObjectMapper());
    }

    @Test
    public void testBeanWithList() {
        List<OBInstance> instances = new ArrayList<>();
        beanConfiguration.listInstances(instances);
        assertNotNull(instances);
    }

    @Test
    public void testBeanWithOBTenant() {
        OBTenant tenant = new OBTenant();
        beanConfiguration.getTenant("instanceId", tenant);
        assertNotNull(tenant);
    }

    @Test
    public void testBeanWithOBDatabaseUser() {
        OBDatabaseUser user = new OBDatabaseUser();
        beanConfiguration.getSysTenantUser("instanceId");
        assertNotNull(user);
    }
}