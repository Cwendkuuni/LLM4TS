package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 String originalPassword = "password123";
 String encodedPassword = passwordEncoder.encode(originalPassword);
 assertTrue(passwordEncoder.matches(originalPassword, encodedPassword));
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 // Test serialization with I18nOutputSerializer
 I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
 String json = objectMapper.writeValueAsString(i18nOutputSerializer);
 // Add assertions for JSON structure and contents here

 // Test deserialization
 I18nOutputSerializer deserializedI18nOutputSerializer = objectMapper.readValue(json, I18nOutputSerializer.class);
 // Add assertions for deserialized object here
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 // Add assertions for firewall behavior here
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertSame(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
 // Add tests for NullCloudMetadataClient here if needed
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertEquals("JSESSIONID", cookieSerializer.getName());
 // Add assertions for cookie serialization behavior here
 }

}