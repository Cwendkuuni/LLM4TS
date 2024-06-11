package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@SpringJUnitConfig(classes = BeanConfiguration.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration configuration;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = configuration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapper = configuration.objectMapper(null);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = configuration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
 assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = configuration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertInstanceOf(NullCloudMetadataClient.class, nullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientSupportsTenantInstance() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.supportsTenantInstance());
 }

 @Test
 public void testNullCloudMetadataClientNeedsOBTenantName() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.needsOBTenantName());
 }

 @Test
 public void testNullCloudMetadataClientNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 }

 @Test
 public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 }

 @Test
 public void testNullCloudMetadataClientGetOceanbaseAccessMode() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(""));
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant("", ""));
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("", ""));
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("", "");
 assertNotNull(endpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = configuration.new NullCloudMetadataClient();
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testObjectMapperDeserialization() {
 ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
 String json = "{\"name\":\"test\",\"age\":30}";
 OBInstance instance = objectMapper.readValue(json, OBInstance.class);
 assertNotNull(instance);
 assertNotNull(instance.getName());
 assertEquals("test", instance.getName());
 assertNotNull(instance.getAge());
 assertEquals(30, instance.getAge().intValue());
 }

}