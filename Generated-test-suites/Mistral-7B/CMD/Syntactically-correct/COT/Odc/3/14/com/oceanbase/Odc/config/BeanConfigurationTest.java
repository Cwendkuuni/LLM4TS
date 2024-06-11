import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SpringBootTest
@Import(BeanConfiguration.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
 String originalPassword = "password";
 String encodedPassword = passwordEncoder.encode(originalPassword);
 Assertions.assertNotEquals(originalPassword, encodedPassword);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = new BeanConfiguration(sensitivePropertyHandler).objectMapper(sensitivePropertyHandler);
 String jsonString = "{\"name\":\"Alice\",\"age\":18}";
 ObjectMapper expectedObjectMapper = new ObjectMapper();
 Internationalizable alice = expectedObjectMapper.readValue(jsonString, Internationalizable.class);
 Object actualObject = objectMapper.readValue(jsonString, Object.class);
 Assertions.assertSame(alice.getClass(), actualObject.getClass());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
 Assertions.assertTrue(defaultHttpFirewall.allowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClientNull() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientSupportsTenantInstance() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertFalse(nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNullCloudMetadataClientNeedsOBTenantName() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertFalse(nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNullCloudMetadataClientNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testNullCloudMetadataClientOceanbaseAccessMode() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 OBTenantEndpoint expectedEndpoint = new OBTenantEndpoint();
 expectedEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 OBTenantEndpoint actualEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 Assertions.assertSame(expectedEndpoint, actualEndpoint);
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
 }

}