import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SpringBootTest
@SpringJUnitConfig(classes = BeanConfiguration.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

 private static final String PASSWORD = "password";
 private static final String SENSITIVE_PROPERTY = "sensitiveProperty";

 @Autowired
 private ApplicationContext context;
 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
 String encodedPassword = passwordEncoder.encode(PASSWORD);
 Assertions.assertNotNull(encodedPassword);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
 CustomOutputSerializer customOutputSerializer = objectMapper.getDefaultSerializerProvider()
 .findSerializer(Internationalizable.class);
 Assertions.assertNotNull(customOutputSerializer);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = context.getBean(HttpFirewall.class);
 Assertions.assertNotNull(defaultHttpFirewall);
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 Assertions.assertTrue(defaultHttpFirewall.allowUrlEncodedSlash("/"));
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testGetSysTenantUser_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
 }

 @Test
 public void testListInstances_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
 }

 @Test
 public void testListTenants_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, instanceId -> cloudMetadataClient.listTenants(instanceId));
 }

 @Test
 public void testGetTenant_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, (InstanceId, tenantId) -> cloudMetadataClient.getTenant(instanceId, tenantId));
 }

 @Test
 public void testListDatabaseUsers_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, (instanceId, tenantId) -> cloudMetadataClient.listDatabaseUsers(instanceId, tenantId));
 }

 @Test
 public void testGetTenantEndpoint_throwsException() {
 NullCloudMetadataClient cloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 Assertions.assertThrows(UnsupportedException.class, (instanceId, tenantId) -> cloudMetadataClient.getTenantEndpoint(instanceId, tenantId));
 }

 @Test
 public void testSensitivePropertyHandler() {
 String decryptedSensitiveProperty = sensitivePropertyHandler.decrypt(sensitivePropertyHandler.encrypt(SENSITIVE_PROPERTY));
 Assertions.assertEquals(SENSITIVE_PROPERTY, decryptedSensitiveProperty);
 }

}