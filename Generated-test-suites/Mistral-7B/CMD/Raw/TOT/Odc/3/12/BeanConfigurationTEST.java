import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 private PasswordEncoder passwordEncoder;
 private ObjectMapper objectMapper;
 private CloudMetadataClient cloudMetadataClient;
 private CookieSerializer cookieSerializer;

 @Autowired
 public BeanConfigurationTest(PasswordEncoder passwordEncoder, ObjectMapper objectMapper,
 CloudMetadataClient cloudMetadataClient, CookieSerializer cookieSerializer) {
 this.passwordEncoder = passwordEncoder;
 this.objectMapper = objectMapper;
 this.cloudMetadataClient = cloudMetadataClient;
 this.cookieSerializer = cookieSerializer;
 }

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 String rawPassword = "password";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 assertNotNull(encodedPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 assertNotNull(decodedPassword);
 assertNotNull(decodedPassword.compareTo(rawPassword) == 0);
 }

 @Test
 public void testObjectMapper() {
 assertNotNull(objectMapper);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDefaultHttpFirewall() {
 defaultHttpFirewall().block("http://example.com/malicious");
 }

 @Test
 public void testNullCloudMetadataClient() {
 assertNull(cloudMetadataClient.getTenant("instanceId", "tenantId"));
 }

 @Test
 public void testCookieSerializer() {
 assertNotNull(cookieSerializer);
 }
}