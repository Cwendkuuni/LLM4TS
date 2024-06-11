package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.oceanbase.odc.BeanConfigurationTest;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.NullCloudMetadataClient;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        JacksonModules jacksonModules = mock(JacksonModules.class);
        when(jacksonModules.customOutputHandling(Mockito.any())).thenReturn(customOutputSerializer);
        when(jacksonFactory.unsafeJsonMapper()).thenReturn(objectMapper);
        ObjectMapper objectMapperSpy = Mockito.spy(objectMapper);
        Mockito.verify(objectMapperSpy, Mockito.times(1)).registerModule(jacksonModules);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertNull(nullCloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString()));
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        String cookieName = cookieSerializer.getName();
        assertEquals("JSESSIONID", cookieName);
    }

    @Test
    public void testGetTenantEndpoint() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString());
        assertNotNull(tenantEndpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
    }

    @Test
    public void testSupportsCloudMetadata() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testSupportsTenantInstance() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNeedsOBTenantName() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNeedsSysTenantUser() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testIncludeClusterNameForJdbcUsername() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testListInstances() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        List<OBInstance> instances = nullCloudMetadataClient.listInstances();
        assertNotNull(instances);
        assertTrue(instances.isEmpty());
    }

    @Test
    public void testListTenants() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        String instanceId = "testInstanceId";
        List<OBTenant> tenants = nullCloudMetadataClient.listTenants(instanceId);
        assertNotNull(tenants);
        assertTrue(tenants.isEmpty());
    }

    @Test
    public void testGetTenant() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        String instanceId = "testInstanceId";
        String tenantId = "testTenantId";
        OBTenant tenant = nullCloudMetadataClient.getTenant(instanceId, tenantId);
        assertNull(tenant);
    }

    @Test
    public void testListDatabaseUsers() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        String instanceId = "testInstanceId";
        String tenantId = "testTenantId";
        List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers(instanceId, tenantId);
        assertNotNull(databaseUsers);
        assertTrue(databaseUsers.isEmpty());
    }

    @Test
    public void testGetSysTenantUser() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        String instanceId = "testInstanceId";
        OBTenantUser sysTenantUser = nullCloudMetadataClient.getSysTenantUser(instanceId);
        assertNull(sysTenantUser);
    }

}