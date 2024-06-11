import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SpringBootTest(classes = BeanConfiguration.class)
@ActiveProfiles("test")
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoderMethod() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 Assertions.assertEquals(password, decodedPassword);
 }

 @Test
 public void testObjectMapperMethod() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 String testJson = "{\"name\": \"testName\"}";
 Internationalizable testObject = new Internationalizable();
 testObject.setName("testName");
 String jsonString = objectMapper.writeValueAsString(testObject);
 Assertions.assertEquals(testJson, jsonString);
 }

 @Test
 public void testDefaultHttpFirewallMethod() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 // add test for url containing special characters
 String urlWithSpecialCharacter = "http://localhost:%25/test";
 defaultHttpFirewall.applyFirewall(urlWithSpecialCharacter);
 Assertions.assertDoesNotThrow(() -> defaultHttpFirewall.applyFirewall(urlWithSpecialCharacter));
 }

 @Test
 public void testCloudMetadataClientMethod() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 Assertions.assertNotNull(cloudMetadataClient);
 // add test for methods in NullCloudMetadataClient
 OceanBaseAccessMode oceanbaseAccessMode = cloudMetadataClient.oceanbaseAccessMode();
 Assertions.assertEquals(OceanBaseAccessMode.DIRECT, oceanbaseAccessMode);
 // assume that listInstances(), listTenants(), getTenant(), listDatabaseUsers(), getSysTenantUser() and getTenantEndpoint() all return empty list or null
 // add test for methods that throw UnsupportedException
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
 }

}