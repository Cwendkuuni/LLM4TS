import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.exception.UnsupportedException;
import com.oceanbase.odc.core.shared.exception.ConfigException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.NullCloudMetadataClient;

@SpringBootTest
@Import(BeanConfiguration.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 private BeanConfiguration beanConfiguration;

 @BeforeEach
 public void setup() {
 cloudMetadataClient = Mockito.spy(beanConfiguration.cloudMetadataClient());
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String password = "password123";
 String encodedPassword = passwordEncoder.encode(password);
 Assertions.assertNotNull(encodedPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(password, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(Mockito.mock(SensitivePropertyHandler.class));
 String json = "{\"name\": \"John Doe\", \"age\": 30}";
 ObjectMapper jacksonFactory = JacksonFactory.unsafeJsonMapper();
 ObjectMapper mapper = jacksonFactory.readerForUpdating(objectMapper);
 mapper.readValue(json, Object.class);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 String url = "http://example.com/%25test";
 Assertions.assertTrue(defaultHttpFirewall.allowedUrl("http://example.com/" + url));
 }

 @Test
 public void testCloudMetadataClient_noCloudMetadataSupported() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 Assertions.assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCloudMetadataClient_getTenant() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("", ""));
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 String cookieName = "JSESSIONID";
 Assertions.assertEquals(cookieName, cookieSerializer.getName());
 }
}