package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertEquals("password", passwordEncoder.encode("password"));
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 ObjectMapper beanObjectMapper = beanConfiguration.objectMapper();
 assertEquals(objectMapper, beanObjectMapper);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
assertNotNull(cloudMetadataClient);
 assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientSupportsCloudMetadata() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertFalse(cloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientSupportsTenantInstance() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertFalse(cloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNullCloudMetadataClientNeedsOBTenantName() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertFalse(cloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNullCloudMetadataClientNeedsSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertFalse(cloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testNullCloudMetadataClientGetOceanBaseAccessMode() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient.listInstances().isEmpty());
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient.listTenants("").isEmpty());
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNull(cloudMetadataClient.getTenant("", ""));
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient.listDatabaseUsers("", "").isEmpty());
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient.getTenantEndpoint("", ""));
 assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.getTenantEndpoint("", "").getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNull(cloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testObjectMapperSerialization() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 ObjectMapper beanObjectMapper = beanConfiguration.objectMapper();
 Object testObj = new Object();
 String json = beanObjectMapper.writeValueAsString(testObj);
 assertEquals(json, objectMapper.writeValueAsString(testObj));
 }

 @Test
 public void testObjectMapperDeserialization() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 ObjectMapper beanObjectMapper = beanConfiguration.objectMapper();
 String json = "{\"name\":\"Test\"}";
 Object testObj = objectMapper.readValue(json, Object.class);
 Object deserializedObj = beanObjectMapper.readValue(json, Object.class);
 assertEquals(testObj, deserializedObj);
 }

 @Test
 public void testCustomOutputSerializer() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 assertEquals(objectMapper.getSerializerProvider().getSerializerForClass(Internationalizable.class),
 objectMapper.getSerializerProvider().getSerializerForClass(Internationalizable.class).withView(I18nOutputSerializer.class));
 }

 @Test
 public void testDefaultCookieSerializerBean() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getName());
 }
}