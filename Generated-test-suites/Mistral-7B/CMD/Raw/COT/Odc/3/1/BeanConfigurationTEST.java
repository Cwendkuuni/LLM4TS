import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @InjectMocks
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperMethod() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientMethod() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerMethod() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantEndpointMethodWithNullInstanceId() {
 beanConfiguration.getTenantEndpoint(null, "tenantId");
 }

 @Test
 public void testGetTenantEndpointMethodWithValidArguments() {
 OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
 expectedTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 OBTenantEndpoint actualTenantEndpoint = beanConfiguration.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(expectedTenantEndpoint, actualTenantEndpoint);
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstancesMethod() {
 beanConfiguration.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsMethodWithNullInstanceId() {
 beanConfiguration.listTenants(null, "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsMethodWithNullTenantId() {
 beanConfiguration.listTenants("instanceId", null);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantMethodWithNullInstanceId() {
 beanConfiguration.getTenant(null, "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantMethodWithNullTenantId() {
 beanConfiguration.getTenant("instanceId", null);
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsersMethodWithNullInstanceId() {
 beanConfiguration.listDatabaseUsers("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsersMethodWithNullTenantId() {
 beanConfiguration.listDatabaseUsers("instanceId", null);
 }

 @Test
 public void testGetSysTenantUserMethodWithValidArguments() {
 OBDatabaseUser expectedSysTenantUser = new OBDatabaseUser();
 expectedSysTenantUser.setUsername("sysTenantUser");
 OBDatabaseUser actualSysTenantUser = beanConfiguration.getSysTenantUser("instanceId");
 assertEquals(expectedSysTenantUser, actualSysTenantUser);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetSysTenantUserMethodWithNullInstanceId() {
 beanConfiguration.getSysTenantUser(null);
 }

 @Test
 public void testGetTenantWithValidArguments() {
 OBTenant expectedTenant = new OBTenant();
 expectedTenant.setTenantName("tenantName");
 OBTenant actualTenant = beanConfiguration.getTenant("instanceId", "tenantName");
 assertEquals(expectedTenant, actualTenant);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantWithNullInstanceId() {
 beanConfiguration.getTenant(null, "tenantName");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantWithNullTenantName() {
 beanConfiguration.getTenant("instanceId", null);
 }

 @Test
 public void testListInstancesMethodWithValidArguments() {
 OBInstance expectedInstance = new OBInstance();
 expectedInstance.setInstanceName("instanceName");
 List<OBInstance> actualInstances = beanConfiguration.listInstances();
 assertEquals(1, actualInstances.size());
 assertEquals(expectedInstance, actualInstances.get(0));
 }

 @Test
 public void testListTenantsMethodWithValidArguments() {
 OBTenant expectedTenant = new OBTenant();
 expectedTenant.setTenantName("tenantName");
 List<OBTenant> actualTenants = beanConfiguration.listTenants("instanceId", "tenantName");
 assertEquals(1, actualTenants.size());
 assertEquals(expectedTenant, actualTenants.get(0));
 }
}