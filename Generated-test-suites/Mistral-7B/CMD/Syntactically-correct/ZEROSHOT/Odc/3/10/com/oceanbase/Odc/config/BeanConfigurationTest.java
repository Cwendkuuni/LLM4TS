package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private AnnotationConfigApplicationContext context;
 private BeanConfiguration config;
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
 config = context.getBean(BeanConfiguration.class);
 sensitivePropertyHandler = context.getBean(SensitivePropertyHandler.class);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = config.passwordEncoder();
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = config.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = config.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = config.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = config.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertEquals("JSESSIONID", cookieSerializer.getName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNotNull(nullCloudMetadataClient);
 assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 }

 @Test
 public void testNullCloudMetadataClientTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
 assertNotNull(tenants);
 assertTrue(tenants.isEmpty());
 }

 @Test
 public void testNullCloudMetadataClientDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 assertNotNull(databaseUsers);
 assertTrue(databaseUsers.isEmpty());
 }

}