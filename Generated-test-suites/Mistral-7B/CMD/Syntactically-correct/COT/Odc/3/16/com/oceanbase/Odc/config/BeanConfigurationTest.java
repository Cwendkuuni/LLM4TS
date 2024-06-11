import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private PasswordEncoder passwordEncoder;
 private NullCloudMetadataClient cloudMetadataClient;

 @BeforeEach
 public void setup() {
 beanConfiguration = new BeanConfiguration();
 passwordEncoder = beanConfiguration.passwordEncoder();
 cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 }

 @Test
 public void testPasswordEncoder() {
 String rawPassword = "password123";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 Assertions.assertTrue(BCrypt.checkpw(rawPassword.toCharArray(), encodedPassword));
 }

 @Test
 public void testObjectMapper() {
 // Add test case for objectMapper here
 }

 @Test
 public void testDefaultHttpFirewall() {
 // Add test case for defaultHttpFirewall here
 }

 @Test
 public void testCloudMetadataClient() {
 // Add test case for cloudMetadataClient here
 // Test for supportsCloudMetadata()
 // Test for supportsTenantInstance()
 // Test for needsOBTenantName()
 // Test for needsSysTenantUser()
 // Test for includeClusterNameForJdbcUsername()
 // Test for oceanbaseAccessMode()
 // Test for listInstances()
 // Test for listTenants()
 // Test for getTenant()
 // Test for listDatabaseUsers()
 // Test for getTenantEndpoint()
 // Test for getSysTenantUser()
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant(null, null));
 }

 @Test
 public void testNullCloudMetadataClient() {
 // Add test case for NullCloudMetadataClient here
 OBInstance instance = new OBInstance();
 instance.setInstanceId("testInstanceId");
 OBTenant tenant = new OBTenant();
 tenant.setTenantId("testTenantId");
 OBTenantEndpoint endpoint = new OBTenantEndpoint();
 endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
 // Call methods that should throw UnsupportedException in NullCloudMetadataClient
 cloudMetadataClient.listInstances();
 cloudMetadataClient.listTenants(instance.getInstanceId());
 cloudMetadataClient.getTenant(instance.getInstanceId(), tenant.getTenantId());
 cloudMetadataClient.listDatabaseUsers(instance.getInstanceId(), tenant.getTenantId());
 cloudMetadataClient.getTenantEndpoint(instance.getInstanceId(), tenant.getTenantId());
 cloudMetadataClient.getSysTenantUser(instance.getInstanceId());
 }

}