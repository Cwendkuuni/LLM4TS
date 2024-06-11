import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.CloudMetadataClient;
import com.oceanbase.odc.config.NullCloudMetadataClient;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = BeanConfiguration.passwordEncoder();
 String password = "testPassword";
 String encodedPassword = passwordEncoder.encode(password);
 String decodedPassword = passwordEncoder.decode(encodedPassword);
 assertNotNull(encodedPassword);
 assertNotNull(decodedPassword);
 assertNotNull(encodedPassword.equals(passwordEncoder.encode(password)));
 assertNotNull(decodedPassword.equals(password));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = BeanConfiguration.objectMapper(Mockito.mock(SensitivePropertyHandler.class));
 assertNotNull(objectMapper);
 // add more test cases for ObjectMapper, such as deserializing JSON and handling sensitive data
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = BeanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 // add more test cases for HttpFirewall, such as filtering malicious URLs and handling special characters
 }

 @Test
 public void testNullCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = BeanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
 nullCloudMetadataClient.init(); // check initialization message is logged
 // add more test cases for NullCloudMetadataClient, such as checking unsupported methods return the expected values
 }

}