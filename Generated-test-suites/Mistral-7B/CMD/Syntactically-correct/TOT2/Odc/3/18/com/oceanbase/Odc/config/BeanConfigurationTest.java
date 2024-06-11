import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest(classes = BeanConfiguration.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;
 @Autowired
 private NullCloudMetadataClient cloudMetadataClient;

 @BeforeAll
 public static void setup() {
 // Perform any necessary setup here
 }

 @Test
 public void testPasswordEncoderMethod() {
 String plainTextPassword = "password123";
 String encodedPassword = beanConfiguration.passwordEncoder().encode(plainTextPassword);
 String decodedPassword = beanConfiguration.passwordEncoder().decode(encodedPassword);

 Assertions.assertEquals(plainTextPassword, decodedPassword);
 }

 @Test
 public void testObjectMapperMethod() {
 String inputJson = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\"}";
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);

 Internationalizable internationalizable = objectMapper.readValue(inputJson, Internationalizable.class);

 Assertions.assertEquals("John Doe", internationalizable.getName());
 Assertions.assertEquals(30, internationalizable.getAge());
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 // Add test cases for the defaultHttpFirewall method here
 }

 @Test
 public void testCloudMetadataClientMethod() {
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
 // Add more test cases for cloudMetadataClient methods here
 }

 @Test
 public void testCookieSerializerMethod() {
 // Add test cases for the cookieSerializer method here
 }

 @Test
 public void testNullCloudMetadataClientInitialization() {
 // Test the initialization of the NullCloudMetadataClient
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantMethodWithUnsupportedCloudMetadata() {
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 cloudMetadataClient.getTenant(instanceId, tenantId);
 }

 @Test
 public void testGetTenantEndpointMethodWithDirectAccessMode() {
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 OBTenantEndpoint tenantEndpoint = beanConfiguration.getTenantEndpoint(instanceId, tenantId);

 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
 }

 // Add more test cases for other methods and edge cases here

}