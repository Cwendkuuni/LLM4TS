package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

/**
 * Test class for BeanConfiguration
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-13 15:50
 */
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Mock
    private CloudMetadataClient cloudMetadataClient;

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper expectedObjectMapper = mock(ObjectMapper.class);
        when(expectedObjectMapper.registerModule(JacksonModules.sensitiveInputHandling(any()))).thenReturn(objectMapper);
        when(expectedObjectMapper.registerModule(JacksonModules.customOutputHandling(any()))).thenReturn(objectMapper);
        doReturn(objectMapper).when(beanConfiguration).objectMapper(sensitivePropertyHandler);
        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertEquals(expectedObjectMapper, actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClientMock = mock(CloudMetadataClient.class);
        when(beanConfiguration.cloudMetadataClient()).thenReturn(cloudMetadataClientMock);
        assertEquals(cloudMetadataClientMock, beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testNullCloudMetadataClientBean() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
    }

    @Test
    public void testGetTenantEndpointMethod() {
        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(new OBTenantEndpoint());
        OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
        OBTenantEndpoint actualTenantEndpoint = beanConfiguration.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(expectedTenantEndpoint, actualTenantEndpoint);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantMethodWithCloudMetadataClient() {
        when(cloudMetadataClient.getTenant(anyString(), anyString())).thenThrow(new UnsupportedException("Unsupported"));
        beanConfiguration.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantMethodWithNullCloudMetadataClient() {
        beanConfiguration.getTenant("instanceId", "tenantId");
    }

    @Test
    public void testListTenantsMethodWithCloudMetadataClient() {
        when(cloudMetadataClient.listTenants(anyString())).thenReturn(new ArrayList<>());
        List<OBTenant> expectedTenants = new ArrayList<>();
        List<OBTenant> actualTenants = beanConfiguration.listTenants("instanceId");
        assertEquals(expectedTenants, actualTenants);
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsMethodWithNullCloudMetadataClient() {
        beanConfiguration.listTenants("instanceId");
    }

    @Test
    public void testListInstancesMethodWithCloudMetadataClient() {
        when(cloudMetadataClient.listInstances()).thenReturn(new ArrayList<>());
        List<OBInstance> expectedInstances = new ArrayList<>();
        List<OBInstance> actualInstances = beanConfiguration.listInstances();
        assertEquals(expectedInstances, actualInstances);
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesMethodWithNullCloudMetadataClient() {
        beanConfiguration.listInstances();
    }

    @Test
    public void testGetSysTenantUserMethodWithCloudMetadataClient() {
        when(cloudMetadataClient.getSysTenantUser(anyString())).thenReturn(new OBDatabaseUser());
        OBDatabaseUser expectedSysTenantUser = new OBDatabaseUser();
        OBDatabaseUser actualSysTenantUser = beanConfiguration.getSysTenantUser("instanceId");
        assertEquals(expectedSysTenantUser, actualSysTenantUser);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserMethodWithNullCloudMetadataClient() {
        beanConfiguration.getSysTenantUser("instanceId");
    }

    @Test
    public void testListDatabaseUsersMethodWithCloudMetadataClient() {
        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(new ArrayList<>());
        List<OBDatabaseUser> expectedDatabaseUsers = new ArrayList<>();
        List<OBDatabaseUser> actualDatabaseUsers = beanConfiguration.listDatabaseUsers("instanceId", "tenantId");
        assertEquals(expectedDatabaseUsers, actualDatabaseUsers);
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersMethodWithNullCloudMetadataClient() {
        beanConfiguration.listDatabaseUsers("instanceId", "tenantId");
    }
}