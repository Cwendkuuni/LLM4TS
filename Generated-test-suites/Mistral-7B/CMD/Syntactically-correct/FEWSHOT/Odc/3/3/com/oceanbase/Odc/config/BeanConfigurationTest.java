package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.oceanbase.odc.commons.i18n.I18nOutputSerializer;
import com.oceanbase.odc.commons.i18n.Internationalizable;
import com.oceanbase.odc.commons.json.JacksonFactory;
import com.oceanbase.odc.commons.json.JacksonModules;
import com.oceanbase.odc.commons.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for BeanConfiguration
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-20 10:50
 */
@Slf4j
@Configuration
public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall defaultHttpFirewall;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        passwordEncoder = new BCryptPasswordEncoder();

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        defaultHttpFirewall = mock(DefaultHttpFirewall.class);

        cloudMetadataClient = mock(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        boolean isMatch = passwordEncoder.matches("password123", encodedPassword);
        assertTrue(isMatch);
    }

    @Test
    public void testObjectMapper() {
        // Test serialization
        OBInstance instance = new OBInstance();
        instance.setId("instanceId");
        instance.setName("instanceName");

        OBTenant tenant = new OBTenant();
        tenant.setId("tenantId");
        tenant.setName("tenantName");

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);

        OBDatabaseUser user = new OBDatabaseUser();
        user.setId("userId");
        user.setName("userName");
        user.setPassword("password");

        List<OBInstance> instances = Arrays.asList(instance);
        List<OBTenant> tenants = Arrays.asList(tenant);
        List<OBDatabaseUser> users = Arrays.asList(user);

        when(objectMapper.writeValueAsString(instances)).thenReturn("instances");
        when(objectMapper.writeValueAsString(tenants)).thenReturn("tenants");
        when(objectMapper.writeValueAsString(users)).thenReturn("users");

        String instancesJson = objectMapper.writeValueAsString(instances);
        String tenantsJson = objectMapper.writeValueAsString(tenants);
        String usersJson = objectMapper.writeValueAsString(users);

        assertEquals("instances", instancesJson);
        assertEquals("tenants", tenantsJson);
        assertEquals("users", usersJson);

        // Test deserialization
        String instancesStr = "instances";
        String tenantsStr = "tenants";
        String usersStr = "users";

        when(objectMapper.readValue(instancesStr, OBInstance[].class)).thenReturn(instances.toArray(new OBInstance[0]));
        when(objectMapper.readValue(tenantsStr, OBTenant[].class)).thenReturn(tenants.toArray(new OBTenant[0]));
        when(objectMapper.readValue(usersStr, OBDatabaseUser[].class)).thenReturn(users.toArray(new OBDatabaseUser[0]));

        OBInstance[] instanceArray = objectMapper.readValue(instancesStr, OBInstance[].class);
        OBTenant[] tenantArray = objectMapper.readValue(tenantsStr, OBTenant[].class);
        OBDatabaseUser[] userArray = objectMapper.readValue(usersStr, OBDatabaseUser[].class);

        assertEquals(instance, instanceArray[0]);
        assertEquals(tenant, tenantArray[0]);
        assertEquals(user, userArray[0]);
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Test allowUrlEncodedSlash
        String urlWithSlash = "/test%2F";
        assertFalse(defaultHttpFirewall.allow("test", urlWithSlash));

        when(defaultHttpFirewall.setAllowUrlEncodedSlash(true)).thenReturn(defaultHttpFirewall);

        assertTrue(defaultHttpFirewall.allow("test", urlWithSlash));
    }

    @Test
    public void testCloudMetadataClient() {
        // Test supportsCloudMetadata
        assertFalse(cloudMetadataClient.supportsCloudMetadata());

        // Test supportsTenantInstance
        assertFalse(cloudMetadataClient.supportsTenantInstance());

        // Test needsOBTenantName
        assertFalse(cloudMetadataClient.needsOBTenantName());

        // Test needsSysTenantUser
        assertFalse(cloudMetadataClient.needsSysTenantUser());

        // Test includeClusterNameForJdbcUsername
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());

        // Test oceanbaseAccessMode
        OceanBaseAccessMode accessMode = cloudMetadataClient.oceanbaseAccessMode();
        assertEquals(OceanBaseAccessMode.DIRECT, accessMode);

        // Test listInstances
        when(cloudMetadataClient.listInstances()).thenReturn(Arrays.asList(new OBInstance()));
        List<OBInstance> instances = cloudMetadataClient.listInstances();
        assertEquals(1, instances.size());

        // Test listTenants
        when(cloudMetadataClient.listTenants("instanceId")).thenReturn(Arrays.asList(new OBTenant()));
        List<OBTenant> tenants = cloudMetadataClient.listTenants("instanceId");
        assertEquals(1, tenants.size());

        // Test getTenant
        when(cloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(new OBTenant());
        OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNotNull(tenant);

        // Test listDatabaseUsers
        when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId")).thenReturn(Arrays.asList(new OBDatabaseUser()));
        List<OBDatabaseUser> databaseUsers = cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertEquals(1, databaseUsers.size());

        // Test getTenantEndpoint
        OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);

        // Test getSysTenantUser
        when(cloudMetadataClient.getSysTenantUser("instanceId")).thenReturn(new OBDatabaseUser());
        OBDatabaseUser sysTenantUser = cloudMetadataClient.getSysTenantUser("instanceId");
        assertNotNull(sysTenantUser);
    }
}