import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String password = "password123";
 String encodedPassword = passwordEncoder.encode(password);
 assertEquals(encodedPassword, passwordEncoder.encode(password));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 String jsonString = "{\"name\":\"John Doe\",\"age\":30}";
 OBInstance obj = new OBInstance();
 obj.setName("myInstance");
 when(sensitivePropertyHandler.decrypt("myEncryptedValue")).thenReturn("sensitiveValue");
 when(sensitivePropertyHandler.decrypt("myOtherEncryptedValue")).thenReturn("otherSensitiveValue");
 ObjectMapper expectedObjectMapper = Mockito.spy(JacksonFactory.unsafeJsonMapper());
 expectedObjectMapper.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
 expectedObjectMapper.registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
 assertEquals(expectedObjectMapper.readValue(jsonString, OBInstance.class), objectMapper.readValue(jsonString, OBInstance.class));
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNull(httpFirewall.getUrlPatternsBlockedByDefault().get("/**/../../.*"));
 }

 @Test
 public void testCloudMetadataClient() {
 NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNull(cloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString()));
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertEquals("JSESSIONID", cookieSerializer.getName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNull(nullCloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString()));
 }

}