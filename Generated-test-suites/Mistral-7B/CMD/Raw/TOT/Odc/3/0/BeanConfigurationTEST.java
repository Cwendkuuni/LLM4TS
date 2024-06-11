import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Test
 @DisplayName("Test passwordEncoder Bean creation")
 public void testPasswordEncoderCreation() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 Assertions.assertNotNull(passwordEncoder, "passwordEncoder should not be null");
 }

 @Test
 @DisplayName("Test ObjectMapper Bean creation")
 public void testObjectMapperCreation() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 Assertions.assertNotNull(objectMapper, "ObjectMapper should not be null");
 }

 @Test
 @DisplayName("Test defaultHttpFirewall Bean creation")
 public void testDefaultHttpFirewallCreation() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 Assertions.assertNotNull(defaultHttpFirewall, "defaultHttpFirewall should not be null");
 }

 @Test
 @DisplayName("Test CloudMetadataClient Bean creation")
 public void testCloudMetadataClientCreation() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 Assertions.assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient,
 "CloudMetadataClient should be instance of NullCloudMetadataClient");
 }

 @Test
 @DisplayName("Test CookieSerializer Bean creation")
 public void testCookieSerializerCreation() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 Assertions.assertNotNull(cookieSerializer, "CookieSerializer should not be null");
 }

}