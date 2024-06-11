package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SuppressWarnings("unchecked")
public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private NullCloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = mock(NullCloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoderCreation() {
        PasswordEncoder passwordEncoder = BeanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperCreation() {
        ObjectMapper objectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testHttpFirewallCreation() {
        HttpFirewall httpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClientCreation() {
        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertNotNull(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerCreation() {
        CookieSerializer cookieSerializer = BeanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.listTenants("instanceId"));
        assertNotNull(nullCloudMetadataClient.listInstances());
        assertNotNull(nullCloudMetadataClient.needsSysTenantUser());
        assertNotNull(nullCloudMetadataClient.needsOBTenantName());
        assertNotNull(nullCloudMetadataClient.supportsTenantInstance());
        assertNotNull(nullCloudMetadataClient.supportsCloudMetadata());
        assertNotNull(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertNotNull(nullCloudMetadataClient.oceanbaseAccessMode());
        assertNotNull(nullCloudMetadataClient.getSysTenantUser("instanceId"));
        assertNotNull(nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNotNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId").getName());
        List<OBInstance> instances = new ArrayList<>();
        instances.add(new OBInstance());
        when(cloudMetadataClient.listInstances()).thenReturn(instances);
        assertNotNull(nullCloudMetadataClient.listInstances());
        List<OBTenant> tenants = new ArrayList<>();
        tenants.add(new OBTenant());
        when(cloudMetadataClient.listTenants("instanceId")).thenReturn(tenants);
        assertNotNull(nullCloudMetadataClient.listTenants("instanceId"));
    }
}