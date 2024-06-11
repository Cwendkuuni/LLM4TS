import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 @Mock
 private SensitivePropertyHandler sensitivePropertyHandler;
 @InjectMocks
 private BeanConfiguration testBeanConfiguration;

 @Before
 public void setUp() {
 beanConfiguration = new BeanConfiguration();
 testBeanConfiguration = new BeanConfiguration();
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertEquals("$2a$10$YzJvG4OyiZjKVXxjLwJu7OJZzG7zV0LBxo0GHgJK3R8NjKqvYj6G",
 passwordEncoder.encode("test"));
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 String json = "{\"name\": \"test\", \"age\": 18}";
 assertEquals(json, objectMapper.writeValueAsString(new TestClass("test", 18)));
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 String url = "http://example.com/%25";
 assertNull(defaultHttpFirewall.getBlockedString(url));
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = testBeanConfiguration.cloudMetadataClient();
 assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertEquals("JSESSIONID", cookieSerializer.getName());
 }

 @Test
 public void testNullCloudMetadataClientBean() {
 CloudMetadataClient nullCloudMetadataClient = testBeanConfiguration.cloudMetadataClient();
 assertNull(nullCloudMetadataClient);
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstancesMethod() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 when(cloudMetadataClient.listInstances()).thenReturn(Collections.emptyList());
 testBeanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 testBeanConfiguration.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsMethod() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 when(cloudMetadataClient.listTenants(instanceId)).thenReturn(Collections.emptyList());
 testBeanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 testBeanConfiguration.listTenants(instanceId);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantMethod() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenReturn(null);
 testBeanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 testBeanConfiguration.getTenant(instanceId, tenantId);
 }

 @Test
 public void testListDatabaseUsersMethod() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 List<OBDatabaseUser> obDatabaseUsers = Arrays.asList(new OBDatabaseUser(), new OBDatabaseUser());
 when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenReturn(obDatabaseUsers);
 testBeanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 assertEquals(obDatabaseUsers, testBeanConfiguration.listDatabaseUsers(instanceId, tenantId));
 }

 @Test(expected = UnsupportedException.class)
 public void testGetSysTenantUserMethod() {
 CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);
 String instanceId = "testInstanceId";
 when(cloudMetadataClient.getSysTenantUser(instanceId)).thenReturn(null);
 testBeanConfiguration.setCloudMetadataClient(cloudMetadataClient);
 testBeanConfiguration.getSysTenantUser(instanceId);
 }
}

class TestClass implements Internationalizable {
 private String name;
 private int age;

 public TestClass(String name, int age) {
 this.name = name;
 this.age = age;
 }

 @Override
 public String getMessageKey() {
 return "test.message";
 }

 @Override
 public String getMessage() {
 return "test message";
 }
}