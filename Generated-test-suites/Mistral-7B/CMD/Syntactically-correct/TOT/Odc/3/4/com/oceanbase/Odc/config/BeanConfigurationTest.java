import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private NullCloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 cloudMetadataClient = mock(NullCloudMetadataClient.class);
 beanConfiguration = new BeanConfiguration();
 beanConfiguration.setSensitivePropertyHandler(sensitivePropertyHandler);
 beanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String password = "password123";
 String encodedPassword = passwordEncoder.encode(password);
 String decodedPassword = passwordEncoder.decode(encodedPassword);

 assert(password.equals(decodedPassword));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 String jsonString = "{\"name\": \"John Doe\", \"age\": 30, \"greeting\": \"Hello, World!\"}";
 Internationalizable internationalizable = new Internationalizable();
 internationalizable.setName("John Doe");
 internationalizable.setAge(30);
 internationalizable.setGreeting("Hello, World!");

 String json = objectMapper.writeValueAsString(internationalizable);
 assert(json.equals(jsonString));

 Internationalizable deserializedInternationalizable = objectMapper.readValue(jsonString, Internationalizable.class);
 assert(internationalizable.getName().equals(deserializedInternationalizable.getName()));
 assert(internationalizable.getAge() == deserializedInternationalizable.getAge());
 assert(internationalizable.getGreeting().equals(deserializedInternationalizable.getGreeting()));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assert(defaultHttpFirewall != null);
 }

 @Test
 public void testCloudMetadataClient() {
 // Test null cloudMetadataClient
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assert(nullCloudMetadataClient instanceof NullCloudMetadataClient);

 // Test cloudMetadataClient creation when bean is missing
 when(cloudMetadataClient).isSupportedCloudMetadata().thenReturn(false);
 NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assert(cloudMetadataClient instanceof NullCloudMetadataClient);

 // Test cloudMetadataClient creation when bean is present
 when(cloudMetadataClient.isSupportedCloudMetadata()).thenReturn(true);
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assert(cloudMetadataClient != null);
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetTenant() {
 beanConfiguration.getCloudMetadataClient().getTenant("instanceId", "tenantId");
 }
}