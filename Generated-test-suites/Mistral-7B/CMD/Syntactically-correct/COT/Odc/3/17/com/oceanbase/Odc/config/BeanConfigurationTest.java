import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest
@Import(BeanConfiguration.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Test
 public void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String plainTextPassword = "password123";
 String encodedPassword = passwordEncoder.encode(plainTextPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(plainTextPassword, decodedPassword);
 }

 @Test
 public void testObjectMapperMethod() {
 SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 // Test serialization
 String jsonString = "{ \"name\": \"John Doe\", \"age\": 30 }";
 Object obj = objectMapper.readValue(jsonString, Object.class);
 Assertions.assertEquals(obj.getClass(), Object.class);
 // Test deserialization
 String jsonString2 = "{\"name\": \"Jane Doe\", \"age\": 25}";
 Object obj2 = objectMapper.readValue(jsonString2, Internationalizable.class);
 Assertions.assertEquals(obj2.getClass(), Internationalizable.class);
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 String urlWithSlash = "http://example.com/%25";
 boolean allowed = defaultHttpFirewall.isUrlAllowed(urlWithSlash, null);
 Assertions.assertTrue(allowed);
 }

 @Test
 public void testCloudMetadataClientMethod_NullCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = this.cloudMetadataClient;
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
 Assertions.assertFalse(cloudMetadataClient.needsOBTenantName());
 Assertions.assertFalse(cloudMetadataClient.needsSysTenantUser());
 Assertions.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(""));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("", ""));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("", ""));
 Assertions.assertNull(cloudMetadataClient.getTenantEndpoint("", ""));
 Assertions.assertNull(cloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testCloudMetadataClientMethod_ExceptionHandling() {
 CloudMetadataClient cloudMetadataClient = this.cloudMetadataClient;
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
 Assertions.assertFalse(cloudMetadataClient.needsOBTenantName());
 Assertions.assertFalse(cloudMetadataClient.needsSysTenantUser());
 Assertions.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(""));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("", ""));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("", ""));
 Assertions.assertNull(cloudMetadataClient.getTenantEndpoint("", ""));
 Assertions.assertNull(cloudMetadataClient.getSysTenantUser(""));
 // Test with empty instanceId and non-empty tenantId
 Assertions.assertThrows(NullPointerException.class, () -> cloudMetadataClient.getTenant("", "tenantId"));
 // Test with non-empty instanceId and empty tenantId
 Assertions.assertThrows(NullPointerException.class, () -> cloudMetadataClient.getTenant("instanceId", ""));
 // Test with empty instanceId and non-empty tenantId and non-empty databaseUserName
 Assertions.assertThrows(NullPointerException.class, () -> cloudMetadataClient.getDatabaseUser("", "tenantId", "databaseUserName"));
 // Test with non-empty instanceId and empty tenantId and non-empty databaseUserName
 Assertions.assertThrows(NullPointerException.class, () -> cloudMetadataClient.getDatabaseUser("instanceId", "", "databaseUserName"));
 }

}