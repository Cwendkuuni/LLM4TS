import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBTenant;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration configuration;
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setup() {
 configuration = new BeanConfiguration();
 sensitivePropertyHandler = spy(SensitivePropertyHandler.class);
 }

 @Test
 public void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = configuration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperMethod() {
 ObjectMapper objectMapper = configuration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 HttpFirewall defaultHttpFirewall = configuration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientMethod() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerMethod() {
 CookieSerializer cookieSerializer = configuration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantEndpointMethodOfNullCloudMetadataClientThrowsException() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 OBTenantEndpoint tenantEndpoint = ((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).getTenantEndpoint("instanceId", "tenantId");
 }

 @Test
 public void testGetTenantMethodOfNullCloudMetadataClientReturnsNull() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNull(((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testListTenantsMethodOfNullCloudMetadataClientReturnsEmptyList() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 List<OBTenant> tenants = ((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).listTenants("instanceId");
 assertNotNull(tenants);
 assertFalse(tenants.isEmpty());
 }

 @Test
 public void testListInstancesMethodOfNullCloudMetadataClientReturnsEmptyList() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 List<OBInstance> instances = ((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).listInstances();
 assertNotNull(instances);
 assertFalse(instances.isEmpty());
 }

 @Test
 public void testGetSysTenantUserMethodOfNullCloudMetadataClientReturnsNull() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 OBTenantEndpoint sysTenantUser = ((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).getSysTenantUser("instanceId");
 assertNull(sysTenantUser);
 }

 @Test
 public void testListDatabaseUsersMethodOfNullCloudMetadataClientReturnsEmptyList() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 List<OBDatabaseUser> users = ((BeanConfiguration.NullCloudMetadataClient) cloudMetadataClient).listDatabaseUsers("instanceId", "tenantId");
 assertNotNull(users);
 assertFalse(users.isEmpty());
 }

}