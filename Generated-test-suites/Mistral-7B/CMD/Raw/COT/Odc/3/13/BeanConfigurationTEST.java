import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SpringBootTest
@ContextConfiguration(classes = BeanConfiguration.class)
@ActiveProfiles("test")
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 private static final String TENANT_ID = "test_tenant";
 private static final String INSTANCE_ID = "test_instance";

 @Autowired
 private CloudMetadataClient cloudMetadataClient;
 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
 String encryptedPassword = passwordEncoder.encode("plainTextPassword");
 Assertions.assertFalse(passwordEncoder.matches("plainTextPassword", encryptedPassword), "Password should not match plain text");
 String decryptedPassword = passwordEncoder.decode(encryptedPassword);
 Assertions.assertEquals("plainTextPassword", decryptedPassword);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
 String jsonString = "{ \"name\": \"test_name\", \"age\": 1 }";
 OBTenant tenant = objectMapper.readValue(jsonString, OBTenant.class);
 Assertions.assertEquals("test_name", tenant.getName());
 Assertions.assertEquals(1, tenant.getAge());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
 Assertions.assertTrue(defaultHttpFirewall.getAllowedHttpMethods().containsAll(List.of("GET", "POST", "PUT", "DELETE")));
 }

 @Test
 public void testCloudMetadataClient_supportsCloudMetadata() {
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCloudMetadataClient_listInstances() {
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 }

 @Test
 public void testCloudMetadataClient_listTenants() {
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(INSTANCE_ID));
 }

 @Test
 public void testCloudMetadataClient_getTenant() {
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant(INSTANCE_ID, TENANT_ID));
 }

 @Test
 public void testCloudMetadataClient_listDatabaseUsers() {
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers(INSTANCE_ID, TENANT_ID));
 }

 @Test
 public void testCloudMetadataClient_getTenantEndpoint() {
 OBTenantEndpoint endpoint = new BeanConfiguration().cloudMetadataClient().getTenantEndpoint(INSTANCE_ID, TENANT_ID);
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testCloudMetadataClient_getSysTenantUser() {
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser(INSTANCE_ID));
 }

}