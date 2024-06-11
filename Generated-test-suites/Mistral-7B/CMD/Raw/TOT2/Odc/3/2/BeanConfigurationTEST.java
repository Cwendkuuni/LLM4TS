package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private CustomOutputSerializer customOutputSerializer;

 @Before
 public void setUp() {
 beanConfiguration = new BeanConfiguration();
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 customOutputSerializer = new CustomOutputSerializer();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 CustomOutputSerializer expectedCustomOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 Mockito.verify(objectMapper).registerModule(Mockito.eq(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt)));
 Mockito.verify(objectMapper).registerModule(Mockito.eq(JacksonModules.customOutputHandling(customOutputSerializer)));
 Mockito.verify(objectMapper).registerModule(Mockito.eq(JacksonModules.customOutputHandling(expectedCustomOutputSerializer)));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);

 CloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantEndpointWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 beanConfiguration.new NullCloudMetadataClient().getTenantEndpoint("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstancesWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 beanConfiguration.new NullCloudMetadataClient().listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 beanConfiguration.new NullCloudMetadataClient().listTenants("instanceId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 beanConfiguration.new NullCloudMetadataClient().getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsersWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 beanConfiguration.new NullCloudMetadataClient().listDatabaseUsers("instanceId", "tenantId");
 }

 @Test
 public void testGetSysTenantUserWithNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
 OBDatabaseUser sysTenantUser = beanConfiguration.new NullCloudMetadataClient().getSysTenantUser("instanceId");
 assertNull(sysTenantUser);
 }

 @Test
 public void testCloudMetadataClientGetTenantEndpointWithValidInput() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
 expectedTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 Mockito.when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(expectedTenantEndpoint);

 OBTenantEndpoint actualTenantEndpoint = beanConfiguration.cloudMetadataClient().getTenantEndpoint("instanceId", "tenantId");
 Mockito.verify(cloudMetadataClient).getTenantEndpoint("instanceId", "tenantId");
 assertNotNull(actualTenantEndpoint);
 }

 @Test
 public void testCloudMetadataClientListInstancesWithValidInput() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBInstance> expectedInstances = Arrays.asList(new OBInstance(), new OBInstance());
 Mockito.when(cloudMetadataClient.listInstances()).thenReturn(expectedInstances);

 List<OBInstance> actualInstances = beanConfiguration.cloudMetadataClient().listInstances();
 Mockito.verify(cloudMetadataClient).listInstances();
 assertNotNull(actualInstances);
 }

 @Test
 public void testCloudMetadataClientListTenantsWithValidInput() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBTenant> expectedTenants = Arrays.asList(new OBTenant(), new OBTenant());
 String instanceId = "instanceId";
 Mockito.when(cloudMetadataClient.listTenants(instanceId)).thenReturn(expectedTenants);

 List<OBTenant> actualTenants = beanConfiguration.cloudMetadataClient().listTenants(instanceId);
 Mockito.verify(cloudMetadataClient).listTenants(instanceId);
 assertNotNull(actualTenants);
 }

 @Test
 public void testCloudMetadataClientGetTenantWithValidInput() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 OBTenant expectedTenant = new OBTenant();
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 Mockito.when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenReturn(expectedTenant);

 OBTenant actualTenant = beanConfiguration.cloudMetadataClient().getTenant(instanceId, tenantId);
 Mockito.verify(cloudMetadataClient).getTenant(instanceId, tenantId);
 assertNotNull(actualTenant);
 }

 @Test
 public void testCloudMetadataClientListDatabaseUsersWithValidInput() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 List<OBDatabaseUser> expectedDatabaseUsers = Arrays.asList(new OBDatabaseUser(), new OBDatabaseUser());
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 Mockito.when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenReturn(expectedDatabaseUsers);

 List<OBDatabaseUser> actualDatabaseUsers = beanConfiguration.cloudMetadataClient().listDatabaseUsers(instanceId, tenantId);
 Mockito.verify(cloudMetadataClient).listDatabaseUsers(instanceId, tenantId);
 assertNotNull(actualDatabaseUsers);
 }

}