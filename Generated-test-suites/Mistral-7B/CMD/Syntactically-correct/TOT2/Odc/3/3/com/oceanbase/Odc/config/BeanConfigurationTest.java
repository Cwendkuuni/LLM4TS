import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class BeanConfigurationTest {

 private AnnotationConfigApplicationContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 this.context = new AnnotationConfigApplicationContext();
 this.context.register(BeanConfiguration.class);
 this.context.refresh();
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = this.context.getBean(PasswordEncoder.class);
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 assertEquals(encodedPassword, passwordEncoder.encode(password));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = this.context.getBean(ObjectMapper.class);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = this.context.getBean(HttpFirewall.class);
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 assertNotNull(cloudMetadataClient);
 // Simulate missing tenant and instance responses from the cloud metadata client
 NullCloudMetadataClient nullCloudMetadataClient = mock(NullCloudMetadataClient.class);
 when(nullCloudMetadataClient.listInstances()).thenReturn(null);
 when(nullCloudMetadataClient.listTenants(Mockito.anyString())).thenReturn(null);
 when(nullCloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
 when(nullCloudMetadataClient.listDatabaseUsers(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
 when(nullCloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
 when(nullCloudMetadataClient.getSysTenantUser(Mockito.anyString())).thenReturn(null);
 this.context.replaceBean(CloudMetadataClient.class, nullCloudMetadataClient);
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testBeanCreation() {
 PasswordEncoder passwordEncoder = this.context.getBean(PasswordEncoder.class);
 ObjectMapper objectMapper = this.context.getBean(ObjectMapper.class);
 HttpFirewall defaultHttpFirewall = this.context.getBean(HttpFirewall.class);
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 assertNotNull(passwordEncoder);
 assertNotNull(objectMapper);
 assertNotNull(defaultHttpFirewall);
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 }

 @Test
 public void testGetTenant() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 OBTenant tenant = new OBTenant();
 tenant.setId("testTenantId");
 tenant.setName("testTenantName");
 when(cloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString())).thenReturn(tenant);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
 OBTenant retrievedTenant = nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId");
 assertEquals(tenant, retrievedTenant);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetTenant_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString())).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId");
 }

 @Test
 public void testListInstances() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 List<OBInstance> instances = Mockito.mock(List.class);
 when(cloudMetadataClient.listInstances()).thenReturn(instances);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.listInstances()).thenCallRealMethod();
 List<OBInstance> retrievedInstances = nullCloudMetadataClient.listInstances();
 assertEquals(instances, retrievedInstances);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListInstances_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.listInstances()).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.listInstances();
 }

 @Test
 public void testListTenants() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 List<OBTenant> tenants = Mockito.mock(List.class);
 when(cloudMetadataClient.listTenants(Mockito.anyString())).thenReturn(tenants);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.listTenants(Mockito.anyString())).thenCallRealMethod();
 List<OBTenant> retrievedTenants = nullCloudMetadataClient.listTenants("testInstanceId");
 assertEquals(tenants, retrievedTenants);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListTenants_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.listTenants(Mockito.anyString())).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.listTenants("testInstanceId");
 }

 @Test
 public void testGetTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
 tenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 when(cloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString())).thenReturn(tenantEndpoint);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
 OBTenantEndpoint retrievedTenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId");
 assertEquals(tenantEndpoint, retrievedTenantEndpoint);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetTenantEndpoint_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString())).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId");
 }

 @Test
 public void testListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 List<OBDatabaseUser> databaseUsers = Mockito.mock(List.class);
 when(cloudMetadataClient.listDatabaseUsers(Mockito.anyString(), Mockito.anyString())).thenReturn(databaseUsers);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.listDatabaseUsers(Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
 List<OBDatabaseUser> retrievedDatabaseUsers = nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId");
 assertEquals(databaseUsers, retrievedDatabaseUsers);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testListDatabaseUsers_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.listDatabaseUsers(Mockito.anyString(), Mockito.anyString())).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId");
 }

 @Test
 public void testGetSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 OBTenantEndpoint sysTenantUser = new OBTenantEndpoint();
 when(cloudMetadataClient.getSysTenantUser(Mockito.anyString())).thenReturn(sysTenantUser);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 when(nullCloudMetadataClient.getSysTenantUser(Mockito.anyString())).thenCallRealMethod();
 OBTenantEndpoint retrievedSysTenantUser = nullCloudMetadataClient.getSysTenantUser("testInstanceId");
 assertEquals(sysTenantUser, retrievedSysTenantUser);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetSysTenantUser_unsupported() {
 CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
 when(cloudMetadataClient.getSysTenantUser(Mockito.anyString())).thenThrow(UnsupportedOperationException.class);
 NullCloudMetadataClient nullCloudMetadataClient = this.context.getBean(CloudMetadataClient.class);
 nullCloudMetadataClient = Mockito.spy(nullCloudMetadataClient);
 nullCloudMetadataClient.getSysTenantUser("testInstanceId");
 }
}