import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
@WebAppConfiguration
public class BeanConfigurationTest {

 private NullCloudMetadataClient cloudMetadataClient;
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Autowired
 public void setUp(NullCloudMetadataClient cloudMetadataClient, SensitivePropertyHandler sensitivePropertyHandler) {
 this.cloudMetadataClient = cloudMetadataClient;
 this.sensitivePropertyHandler = sensitivePropertyHandler;
 }

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = this.beanConfiguration.passwordEncoder();
 String password = "password123";
 String encodedPassword = passwordEncoder.encode(password);
 assertTrue(passwordEncoder.matches(password, encodedPassword));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = this.beanConfiguration.objectMapper(sensitivePropertyHandler);
 String json = "{\"name\": \"OceanBase\", \"age\": 10}";
 Internationalizable internationalizable = objectMapper.readValue(json, Internationalizable.class);
 assertEquals("OceanBase", internationalizable.getName());
 assertEquals(10, internationalizable.getAge());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = this.beanConfiguration.defaultHttpFirewall();
 String url = "http://localhost/%25/test";
 assertTrue(defaultHttpFirewall.isTrusted(url));
 }

 @Test
 public void testCloudMetadataClient() {
 assertNull(this.cloudMetadataClient.getTenant(null, null));
 }

 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientListInstances() {
 this.cloudMetadataClient.listInstances();
 }

}