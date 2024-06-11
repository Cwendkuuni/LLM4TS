import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;
 @Autowired
 private NullCloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = this.beanConfiguration.passwordEncoder();
 String encodedPassword = passwordEncoder.encode("test");
 String decodedPassword = passwordEncoder.decode("{bcrypt}$2a$10$KsZQFxRqH9MvV/fYzK4X.u4gZ0z3yFvJ3JbBQbzK7XuQO/D8k6J9");
 assertNotNull(encodedPassword);
 assertNotNull(decodedPassword);
 assertNotNull(passwordEncoder.matches("test", encodedPassword));
 assertNotNull(passwordEncoder.matches(decodedPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = this.beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = this.beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 assertNotNull(cloudMetadataClient);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenant() {
 cloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstances() {
 cloudMetadataClient.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenants() {
 cloudMetadataClient.listTenants("instanceId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsers() {
 cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }
}