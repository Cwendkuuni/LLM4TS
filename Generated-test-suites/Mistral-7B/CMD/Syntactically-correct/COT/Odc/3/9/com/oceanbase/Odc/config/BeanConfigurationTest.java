import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class BeanConfigurationTest {

 private static final String TEST_PASSWORD = "testPassword";

 @Autowired
 @Qualifier("passwordEncoder")
 private PasswordEncoder passwordEncoder;

 @Autowired
 @Qualifier("objectMapper")
 private ObjectMapper objectMapper;

 @Autowired
 @Qualifier("defaultHttpFirewall")
 private HttpFirewall defaultHttpFirewall;

 @Test
 public void testPasswordEncoder() {
 String encodedPassword = passwordEncoder.encode(TEST_PASSWORD);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(TEST_PASSWORD, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 String jsonString = "{\"name\": \"testName\", \"age\": 25}";
 Internationalizable internationalizable = new Internationalizable();
 internationalizable.setName("testName");
 internationalizable.setAge(25);
 String jsonWithInternationalizable = objectMapper.writeValueAsString(internationalizable);
 Internationalizable deserializedInternationalizable = objectMapper.readValue(jsonString, Internationalizable.class);
 Assertions.assertEquals(internationalizable.getName(), deserializedInternationalizable.getName());
 Assertions.assertEquals(internationalizable.getAge(), deserializedInternationalizable.getAge());
 }

 @Test
 public void testDefaultHttpFirewall() {
 String urlWithSpecialCharacter = "http://localhost:8080/%25test";
 Assertions.assertTrue(defaultHttpFirewall.allowUrlPattern(urlWithSpecialCharacter));
 }

 @Test
 public void testCloudMetadataClient_supportsCloudMetadata() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCloudMetadataClient_supportsTenantInstance() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testCloudMetadataClient_needsOBTenantName() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertTrue(cloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testCloudMetadataClient_needsSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertTrue(cloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testCloudMetadataClient_includeClusterNameForJdbcUsername() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testCloudMetadataClient_oceanbaseAccessMode() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testCloudMetadataClient_listInstances() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 }

 @Test
 public void testCloudMetadataClient_listTenants() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testCloudMetadataClient_getTenant() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testCloudMetadataClient_listDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 }

 @Test
 public void testCloudMetadataClient_getTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testCloudMetadataClient_getSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
 }

}