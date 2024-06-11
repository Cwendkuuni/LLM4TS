import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 private static final String PASSWORD = "testPassword";
 private static final String TENANT_ID = "testTenantId";
 private static final String INSTANCE_ID = "testInstanceId";

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
 String encodedPassword = passwordEncoder.encode(PASSWORD);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(PASSWORD, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 // test encoding
 String json = "{\"name\":\"testName\",\"age\":30}";
 Object object = objectMapper.readValue(json, Object.class);
 Assertions.assertEquals("testName", object.getProperty("name"));
 Assertions.assertEquals(30, object.getProperty("age"));

 // test decoding with sensitive property
 String jsonWithSensitiveProperty = "{\"name\":\"testName\",\"password\":\"" + PASSWORD + "\"}";
 Object sensitiveObject = objectMapper.readValue(jsonWithSensitiveProperty, Object.class);
 Assertions.assertEquals("testName", sensitiveObject.getProperty("name"));
 Assertions.assertEquals(PASSWORD, sensitiveObject.getProperty("password"));
 }

 @Test
 public void testDefaultHttpFirewall() {
 // test allowUrlEncodedSlash
 String urlWithSlash = "%2Ftest%2F";
 Assertions.assertTrue(defaultHttpFirewall.allowUrlPattern(urlWithSlash));

 // test other special characters
 String urlWithSpecialChar = "%{}[];()+,/?=&";
 Assertions.assertTrue(defaultHttpFirewall.allowUrlPattern(urlWithSpecialChar));
 }

 @Test
 public void testCloudMetadataClient() {
 // test null CloudMetadataClient
 Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
 Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
 Assertions.assertFalse(cloudMetadataClient.needsOBTenantName());
 Assertions.assertFalse(cloudMetadataClient.needsSysTenantUser());
 Assertions.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

 // test getTenantEndpoint
 OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint(INSTANCE_ID, TENANT_ID);
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) applicationContext.getBean(CloudMetadataClient.class);
 Assertions.assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 Assertions.assertFalse(nullCloudMetadataClient.supportsTenantInstance());
 Assertions.assertFalse(nullCloudMetadataClient.needsOBTenantName());
 Assertions.assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 Assertions.assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @org.junit.Test // for JUnit 4 compatibility
 public void testCookieSerializer() {
 String cookieValue = "JSESSIONID=testValue";
 Cookie cookie = cookieSerializer.serialize(new StringCookie("JSESSIONID", "testValue"));
 String deserializedCookieValue = cookieSerializer.deserialize(cookie.getValue()).getValue();
 Assertions.assertEquals(cookieValue, deserializedCookieValue);
 }

 @After
 public void cleanUp() {
 ApplicationContext context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
 context.close();
 }

}