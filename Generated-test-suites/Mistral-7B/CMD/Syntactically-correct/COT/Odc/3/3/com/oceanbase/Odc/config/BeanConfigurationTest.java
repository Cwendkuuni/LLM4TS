import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest
@Import(BeanConfiguration.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Test
 public void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 String decodedPassword = passwordEncoder.decode(encodedPassword);

 Assertions.assertEquals(password, decodedPassword);
 }

 @Test
 public void testObjectMapperMethod() {
 SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 String jsonString = "{\"name\": \"test\", \"age\": 20}";
 ObjectMapper expectedObjectMapper = new ObjectMapper();

 try {
 Object map = objectMapper.readValue(jsonString, Object.class);
 Object expectedMap = expectedObjectMapper.readValue(jsonString, Object.class);

 Assertions.assertEquals(map, expectedMap);
 } catch (Exception e) {
 e.printStackTrace();
 FailureHints.fail("ObjectMapper test failed.");
 }
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 // Check if defaultHttpFirewall supports allowUrlEncodedSlash
 String url = "/test%25/test";
 boolean result = defaultHttpFirewall.isUrlSafe(url);

 Assertions.assertTrue(result);
 }

 @Test
 public void testCloudMetadataClientMethod() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 Assertions.assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerMethod() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 Assertions.assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClientMethod() {
 CloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 Assertions.assertNotNull(nullCloudMetadataClient);

 // Test methods that throw UnsupportedException in NullCloudMetadataClient
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("testInstanceId"));
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId"));
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId"));
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId"));
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("testInstanceId"));
 }

 @Test
 public void testGetTenantMethodWithValidInput() {
 OBTenant expectedTenant = new OBTenant();
 expectedTenant.setName("testTenant");
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";

 OBTenant actualTenant = cloudMetadataClient.getTenant(instanceId, tenantId);

 Assertions.assertEquals(expectedTenant, actualTenant);
 }

 @Test
 public void testGetTenantMethodWithNullInstanceId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String tenantId = "testTenantId";

 Assertions.assertNull(cloudMetadataClient.getTenant(null, tenantId));
 }

 @Test
 public void testGetTenantMethodWithNullTenantId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String instanceId = "testInstanceId";

 Assertions.assertNull(cloudMetadataClient.getTenant(instanceId, null));
 }

 @Test
 public void testListDatabaseUsersMethodWithEmptyInstanceId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String tenantId = "testTenantId";

 Assertions.assertNotNull(cloudMetadataClient.listDatabaseUsers("", tenantId));
 Assertions.assertTrue(cloudMetadataClient.listDatabaseUsers("", tenantId).isEmpty());
 }

 @Test
 public void testListDatabaseUsersMethodWithEmptyTenantId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String instanceId = "testInstanceId";

 Assertions.assertNotNull(cloudMetadataClient.listDatabaseUsers(instanceId, ""));
 Assertions.assertTrue(cloudMetadataClient.listDatabaseUsers(instanceId, "").isEmpty());
 }

 @Test
 public void testListDatabaseUsersMethodWithNullInstanceIdAndTenantId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

 Assertions.assertNull(cloudMetadataClient.listDatabaseUsers(null, null));
 }

 @Test
 public void testListDatabaseUsersMethodWithNullInstanceId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String tenantId = "testTenantId";

 Assertions.assertNotNull(cloudMetadataClient.listDatabaseUsers(null, tenantId));
 Assertions.assertTrue(cloudMetadataClient.listDatabaseUsers(null, tenantId).isEmpty());
 }

 @Test
 public void testListDatabaseUsersMethodWithNullTenantId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String instanceId = "testInstanceId";

 Assertions.assertNotNull(cloudMetadataClient.listDatabaseUsers(instanceId, null));
 Assertions.assertTrue(cloudMetadataClient.listDatabaseUsers(instanceId, null).isEmpty());
 }

 @Test
 public void testListDatabaseUsersMethodWithValidInput() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 List<OBDatabaseUser> expectedUsers = Arrays.asList(new OBDatabaseUser(), new OBDatabaseUser());

 List<OBDatabaseUser> actualUsers = cloudMetadataClient.listDatabaseUsers(instanceId, tenantId);

 Assertions.assertEquals(expectedUsers, actualUsers);
 }

 @Test
 public void testListInstancesMethodWithEmptyInput() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

 Assertions.assertNotNull(cloudMetadataClient.listInstances());
 Assertions.assertTrue(cloudMetadataClient.listInstances().isEmpty());
 }

 @Test
 public void testListInstancesMethodWithValidInput() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 List<OBInstance> expectedInstances = Arrays.asList(new OBInstance(), new OBInstance());

 List<OBInstance> actualInstances = cloudMetadataClient.listInstances();

 Assertions.assertEquals(expectedInstances, actualInstances);
 }

 @Test
 public void testGetSysTenantUserMethodWithEmptyInstanceId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

 Assertions.assertNull(cloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testGetSysTenantUserMethodWithValidInput() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 OBDatabaseUser expectedUser = new OBDatabaseUser();
 String instanceId = "testInstanceId";

 OBDatabaseUser actualUser = cloudMetadataClient.getSysTenantUser(instanceId);

 Assertions.assertEquals(expectedUser, actualUser);
 }

 @Test
 public void testGetSysTenantUserMethodWithNullInstanceId() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

 Assertions.assertNull(cloudMetadataClient.getSysTenantUser(null));
 }
}