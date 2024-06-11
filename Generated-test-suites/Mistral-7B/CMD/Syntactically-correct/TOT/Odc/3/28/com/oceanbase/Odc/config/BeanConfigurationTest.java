package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

 @Autowired
 @Qualifier("passwordEncoder")
 private PasswordEncoder passwordEncoder;

 @Autowired
 @Qualifier("objectMapper")
 private ObjectMapper objectMapper;

 @Autowired
 @Qualifier("defaultHttpFirewall")
 private HttpFirewall defaultHttpFirewall;

 @Autowired
 @Qualifier("cloudMetadataClient")
 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 @Qualifier("cookieSerializer")
 private CookieSerializer cookieSerializer;

 @Test
 public void testPasswordEncoder() {
 String rawPassword = "password";
 String encodedPassword = passwordEncoder.encode(rawPassword);
 String decodedPassword = passwordEncoder.decode(encodedPassword);

 assertNotNull(encodedPassword);
 assertNotNull(decodedPassword);
 assertNotNull(passwordEncoder.matches(rawPassword, encodedPassword));
 assertNotNull(passwordEncoder.matches(decodedPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() {
 // Test serialization
 String json = "{\"name\": \"John Doe\", \"age\": 30}";
 Object javaObject = objectMapper.readValue(json, Object.class);
 assertNotNull(javaObject);

 // Test deserialization
 String javaObjectJson = objectMapper.writeValueAsString(javaObject);
 assertNotNull(javaObjectJson);
 }

 @Test
 public void testDefaultHttpFirewall() {
 String url = "http://localhost:8080/%25test%25.jsp";
 boolean allowed = defaultHttpFirewall.allow(url);
 assert(allowed);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClientMock = Mockito.mock(CloudMetadataClient.class);
 BeanConfiguration configuration = new BeanConfiguration();
 configuration.setCloudMetadataClient(cloudMetadataClientMock);
 // Test property injection
 configuration.cloudMetadataClient();
 // Test methods call
 Mockito.verify(cloudMetadataClientMock, Mockito.times(0)).supportsCloudMetadata();
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClient() {
 BeanConfiguration configuration = new BeanConfiguration();
 configuration.cloudMetadataClient();
 }

 @Test
 public void testCookieSerializer() {
 // Test serialization
 String jsessionid = "JSESSIONID=1234567890";
 String serializedCookie = cookieSerializer.serialize(jsessionid);
 assertNotNull(serializedCookie);

 // Test deserialization
 String[] cookieValues = cookieSerializer.deserialize(serializedCookie);
 String deserializedJsessionid = cookieValues[0];
 assertNotNull(deserializedJsessionid);
 assertEqual("JSESSIONID", deserializedJsessionid.substring(0, 10));
 }
}