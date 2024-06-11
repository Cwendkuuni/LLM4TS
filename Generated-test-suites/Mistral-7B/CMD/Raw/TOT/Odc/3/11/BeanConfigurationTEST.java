import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private NullCloudMetadataClient cloudMetadataClient;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoderBeanCreation() {
 PasswordEncoder passwordEncoder = bean(PasswordEncoder.class);
 assertEquals("encodedPassword", passwordEncoder.encode("password"));
 }

 @Test
 public void testObjectMapperBeanCreation() {
 ObjectMapper objectMapper = bean(ObjectMapper.class);
 // add assertions for objectMapper here
 }

 @Test
 public void testDefaultHttpFirewallBeanCreation() {
 HttpFirewall defaultHttpFirewall = bean(HttpFirewall.class);
 // add assertions for defaultHttpFirewall here
 }

 @Test
 public void testCloudMetadataClientBeanCreation() {
 CloudMetadataClient cloudMetadataClient = bean(CloudMetadataClient.class);
 assertNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBeanCreation() {
 CookieSerializer cookieSerializer = bean(CookieSerializer.class);
 // add assertions for cookieSerializer here
 }

 @Test
 public void testNullCloudMetadataClientBehavior() {
 OBTenantEndpoint endpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertNull(endpoint);
 }

 // Add more test cases for other methods and edge cases

 // Helper method to get autowired beans
 private <T> T bean(Class<T> clazz) {
 return context.getBean(clazz);
 }

 // Spring context for test
 private ApplicationContext context;

 @Before
 public void initContext() {
 context = new AnnotationConfigApplicationContext();
 context.scan("com.oceanbase.odc.config");
 context.refresh();
 }

}