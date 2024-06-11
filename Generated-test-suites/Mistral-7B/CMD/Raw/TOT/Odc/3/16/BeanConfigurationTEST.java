package com.oceanbase.odc.config;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private AnnotationConfigApplicationContext context;
 private PasswordEncoder passwordEncoder;
 private ObjectMapper objectMapper;
 private NullCloudMetadataClient cloudMetadataClient;
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setUp() {
 beanConfiguration = new BeanConfiguration();
 context = new AnnotationConfigApplicationContext();
 context.register(BeanConfiguration.class);
 context.refresh();

 passwordEncoder = context.getBean(BeanConfiguration.PasswordEncoder.class);
 objectMapper = context.getBean(BeanConfiguration.ObjectMapper.class);
 cloudMetadataClient = context.getBean(BeanConfiguration.NullCloudMetadataClient.class);
 sensitivePropertyHandler = Mockito.spy(new SensitivePropertyHandler());
 }

 @Test
 public void testPasswordEncoder() {
 String rawPassword = "password";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);

 assertEquals(rawPassword, decodedPassword);
 }

 @Test
 public void testObjectMapper() {
 String jsonString = "{\"name\": \"John\", \"age\": 30}";
 ObjectMapper jsonMapper = Mockito.spy(objectMapper);
 Object mappedObject = jsonMapper.readValue(jsonString, Object.class);

 assertEquals("John", mappedObject.getProperty("name"));
 assertEquals(30, mappedObject.getProperty("age"));

 I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, i18nOutputSerializer);
 jsonMapper.registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

 String serializedObject = jsonMapper.writeValueAsString(mappedObject);

 // Test if I18nOutputSerializer is used when serializing an Internationalizable object
 Internationalizable internationalizable = Mockito.mock(Internationalizable.class);
 when(internationalizable.getLocale()).thenReturn("en_US");
 when(internationalizable.getMessage()).thenReturn("Hello, World!");
 mappedObject.setMessageSource(internationalizable);

 String serializedInternationalizable = jsonMapper.writeValueAsString(mappedObject);

 String expected = "{\"name\": \"John\", \"age\": 30, \"message\": \"Hello, World!\"}";
 assertEquals(expected, serializedInternationalizable);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall httpFirewall = context.getBean(BeanConfiguration.DefaultHttpFirewall.class);
 String urlWithSpecialCharacters = "http://example.com/%25/test";

 assertNotNull(httpFirewall.getAllowPatterns());
 assertTrue(httpFirewall.getAllowPatterns().contains(urlWithSpecialCharacters));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNullCloudMetadataClient() {
 // Test getTenant method
 OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
 assertNull(tenant);

 // Test listInstances method
 List<OBInstance> instances = cloudMetadataClient.listInstances();
 assertNull(instances);
 }

 @Test
 public void testSensitivePropertyHandler() {
 String decryptedSensitiveProperty = sensitivePropertyHandler.decrypt("encryptedSensitiveProperty");
 assertNotNull(decryptedSensitiveProperty);
 }
}