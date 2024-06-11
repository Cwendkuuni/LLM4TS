import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringBootTest
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private HttpFirewall defaultHttpFirewall;

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 private CookieSerializer cookieSerializer;

 @Test
 public void testPasswordEncoder() {
 String plainTextPassword = "password123";
 String encodedPassword = passwordEncoder.encode(plainTextPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(plainTextPassword, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 // Test serialization
 OBDatabaseUser user = new OBDatabaseUser();
 user.setId(1L);
 user.setUsername("testUser");
 user.setPassword("testPassword");
 String json = objectMapper.writeValueAsString(user);
 // Test deserialization
 OBDatabaseUser deserializedUser = objectMapper.readValue(json, OBDatabaseUser.class);
 Assertions.assertEquals(user.getId(), deserializedUser.getId());
 Assertions.assertEquals(user.getUsername(), deserializedUser.getUsername());
 Assertions.assertEquals(user.getPassword(), deserializedUser.getPassword());
 }

 @Test
 public void testDefaultHttpFirewall() {
 // Test allowUrlEncodedSlash
 String urlWithSlash = "%2Ftest%2Fpath";
 Assertions.assertTrue(defaultHttpFirewall.allowUrlEncodedSlash(urlWithSlash));
 // Test other edge cases
 String urlWithSpecialChars = "http://test.com/%61%6D%65%74%68%6F%64%65%64%2Etest";
 Assertions.assertTrue(defaultHttpFirewall.allowUrlEncodedSlash(urlWithSpecialChars));
 }

 @Test
 public void testNullCloudMetadataClient() {
 // Test methods that throw UnsupportedException
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("testInstance"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("testInstance", "testTenant"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("testInstance", "testTenant"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint("testInstance", "testTenant"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("testInstance"));
 }

 @Test
 public void testCookieSerializer() {
 // Test cookie serialization and deserialization
 String sessionId = "testSessionId";
 CookieSerializer serializedCookie = cookieSerializer.serialize(sessionId);
 String cookieValue = serializedCookie.getValue();
 CookieSerializer deserializedCookie = cookieSerializer.deserialize(cookieValue);
 String deserializedSessionId = deserializedCookie.getName();
 Assertions.assertEquals(sessionId, deserializedSessionId);
 }

}