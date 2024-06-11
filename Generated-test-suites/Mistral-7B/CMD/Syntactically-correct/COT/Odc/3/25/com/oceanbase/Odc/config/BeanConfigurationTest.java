import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

class BeanConfigurationTest {

 @Test
 void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 assertTrue(passwordEncoder.matches(password, encodedPassword));
 }

 @Test
 void testObjectMapperMethod() {
 SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
 String jsonString = "{\"name\":\"Test\", \"age\":18}";
 ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper();
 Object actualObject = objectMapper.readValue(jsonString, Object.class);
 Object expectedObject = expectedObjectMapper.readValue(jsonString, Object.class);
 assertEquals(expectedObject, actualObject);
 }

 @Test
 void testDefaultHttpFirewallMethod() {
 DefaultHttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
 String urlWithSlash = "http://test.com/%25";
 assertFalse(defaultHttpFirewall.isUrlSafe(urlWithSlash));
 }

 @Test
 void testCloudMetadataClientMethod() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 }

 @Test
 void testCookieSerializerMethod() {
 CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
 assertEquals("JSESSIONID", cookieSerializer.getName());
 }

 @Test
 void testNullCloudMetadataClientGetTenantEndpointMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
 }

 @Test
 void testNullCloudMetadataClientGetSysTenantUserMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
 }

 @Test
 void testNullCloudMetadataClientListInstancesMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, nullCloudMetadataClient::listInstances);
 }

 @Test
 void testNullCloudMetadataClientListTenantsMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, (() -> nullCloudMetadataClient.listTenants("instanceId"))::printStackTrace);
 }

 @Test
 void testNullCloudMetadataClientListDatabaseUsersMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, (() -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))::printStackTrace);
 }

 @Test
 void testNullCloudMetadataClientGetTenantMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 void testNullCloudMetadataClientGetDatabaseUserByIdMethodThrowsUnsupportedException() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getDatabaseUserById("instanceId", "tenantId", "dbUserName"));
 }

 @Test
 void testNullCloudMetadataClientGetTenantEndpointMethodReturnsExpectedObject() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
 expectedTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 OBTenantEndpoint actualTenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(expectedTenantEndpoint, actualTenantEndpoint);
 }

}