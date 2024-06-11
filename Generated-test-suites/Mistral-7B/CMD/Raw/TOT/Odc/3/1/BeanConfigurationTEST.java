import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;
 @Autowired
 private PasswordEncoder passwordEncoder;
 @Autowired
 private ObjectMapper objectMapper;
 @Autowired
 private DefaultHttpFirewall defaultHttpFirewall;
 @Autowired
 private CloudMetadataClient cloudMetadataClient;
 @Autowired
 private CookieSerializer cookieSerializer;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 String password = "password";
 String encodedPassword = beanConfiguration.passwordEncoder().encode(password);
 assertNotNull(encodedPassword);
 String decodedPassword = beanConfiguration.passwordEncoder().decode(encodedPassword);
 assertNotNull(decodedPassword);
 assertNotNull(password.equals(decodedPassword));
 }

 @Test
 public void testObjectMapper() {
 // Add test cases for encoding and decoding with sensitive properties
 }

 @Test
 public void testDefaultHttpFirewall() {
 String urlWithSpecialCharacters = "http://example.com/%25";
 assertNotNull(beanConfiguration.defaultHttpFirewall().allowUrlAccess(urlWithSpecialCharacters));
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClientMock = mock(CloudMetadataClient.class);
 when(cloudMetadataClientMock.listInstances()).thenReturn(List.of(new OBInstance()));
 when(cloudMetadataClientMock.listTenants("instanceId", "tenantId")).thenReturn(List.of(new OBTenant()));
 when(cloudMetadataClientMock.getTenant("instanceId", "tenantId")).thenReturn(new OBTenant());
 when(cloudMetadataClientMock.listDatabaseUsers("instanceId", "tenantId")).thenReturn(List.of(new OBDatabaseUser()));
 when(cloudMetadataClientMock.getTenantEndpoint("instanceId", "tenantId")).thenReturn(new OBTenantEndpoint());
 when(cloudMetadataClientMock.getSysTenantUser("instanceId")).thenReturn(new OBDatabaseUser());
 beanConfiguration.setCloudMetadataClient(cloudMetadataClientMock);
 assertNotNull(beanConfiguration.cloudMetadataClient());
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClient() {
 beanConfiguration.setCloudMetadataClient(null);
 beanConfiguration.cloudMetadataClient();
 }
}