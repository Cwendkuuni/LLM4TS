import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BeanConfigurationTest {

 @Autowired
 private ApplicationContext applicationContext;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
 String originalPassword = "password123";
 String encodedPassword = passwordEncoder.encode(originalPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(originalPassword, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 SensitivePropertyHandler sensitivePropertyHandler = applicationContext.getBean(SensitivePropertyHandler.class);
 ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
 String input = "{\"name\": \"Test\", \"age\": 30, \"password\": \"password123\"}";
 // test normal deserialization
 ObjectMapper originalObjectMapper = JacksonFactory.unsafeJsonMapper();
 Object originalObject = originalObjectMapper.readValue(input, Object.class);
 Object mappedObject = objectMapper.readValue(input, Object.class);
 Assertions.assertEquals(originalObject, mappedObject);
 // test decryption
 String decryptedInput = "{\"name\": \"Test\", \"age\": 30, \"password\": \"${encrypted:password123}}" +
 "\"}";
 Object decryptedObject = objectMapper.readValue(decryptedInput, Object.class);
 String decryptedPassword = sensitivePropertyHandler.decrypt(decryptedObject.getClass(), decryptedObject, "password123");
 Assertions.assertEquals("password123", decryptedPassword);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = applicationContext.getBean(HttpFirewall.class);
 String validUrl = "http://example.com/%65%6e%63%6F%64%65%64";
 String invalidUrl = "http://example.com/%0A";
 Assertions.assertTrue(httpFirewall.allowUrlAccess(validUrl));
 Assertions.assertFalse(httpFirewall.allowUrlAccess(invalidUrl));
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
 Assertions.assertNotNull(cloudMetadataClient);
 // TODO: add test cases for methods in CloudMetadataClient interface
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = applicationContext.getBean(NullCloudMetadataClient.class);
 Assertions.assertNotNull(nullCloudMetadataClient);
 // test exception handling
 Assertions.assertThrows(UnsupportedException.class, () -> {
 nullCloudMetadataClient.listInstances();
 });
 // TODO: add test cases for other methods in NullCloudMetadataClient class
 }

}