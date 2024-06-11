package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;

@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 private PasswordEncoder passwordEncoder;
 private ObjectMapper objectMapper;
 private HttpFirewall httpFirewall;
 private NullCloudMetadataClient cloudMetadataClient;

 @Autowired
 public BeanConfigurationTest(PasswordEncoder passwordEncoder, ObjectMapper objectMapper,
 HttpFirewall httpFirewall, NullCloudMetadataClient cloudMetadataClient) {
 this.passwordEncoder = passwordEncoder;
 this.objectMapper = objectMapper;
 this.httpFirewall = httpFirewall;
 this.cloudMetadataClient = cloudMetadataClient;
 }

 @Before
 public void setup() {
 // initialize objects if necessary
 }

 @Test
 public void testPasswordEncoder() {
 String plainText = "plainTextPassword";
 String encodedPassword = passwordEncoder.encode(plainText);
 boolean encodedMatch = passwordEncoder.matches(plainText, encodedPassword);
 assertTrue(encodedMatch);
 }

 @Test
 public void testObjectMapper() {
 Internationalizable internationalizable = new Internationalizable() {
 @Override
 public String getMessage() {
 return "test message";
 }
 };
 String json = objectMapper.writeValueAsString(internationalizable);
 Internationalizable deserialized = objectMapper.readValue(json, Internationalizable.class);
 assertEquals("test message", deserialized.getMessage());
 }

 @Test
 public void testHttpFirewall() {
 // test normal url
 String url = "https://www.google.com";
 assertTrue(httpFirewall.isSafe(url));
 // test url with special characters
 String urlWithSpecialChars = "https://www.google.com/%25test";
 assertTrue(httpFirewall.isSafe(urlWithSpecialChars));
 // test malicious url
 String maliciousUrl = "https://www.google.com/<script>alert('XSS')</script>";
 assertFalse(httpFirewall.isSafe(maliciousUrl));
 }

 @Test
 public void testCloudMetadataClient() {
 // test getTenant, assumes NullCloudMetadataClient returns a default instance
 String instanceId = "testInstanceId";
 String tenantId = "testTenantId";
 OBTenant tenant = cloudMetadataClient.getTenant(instanceId, tenantId);
 assertNull(tenant);
 }
}