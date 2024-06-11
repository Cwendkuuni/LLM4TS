package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class BeanConfigurationTest {

 private AnnotationConfigApplicationContext context;

 @Before
 public void setUp() {
 context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
 }

 @Test
 public void testPasswordEncoderBeanCreation() {
 PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperBeanCreation() {
 ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallBeanCreation() {
 HttpFirewall defaultHttpFirewall = context.getBean(HttpFirewall.class);
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientBeanCreation() {
 CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
 assertNotNull(cloudMetadataClient);
 // Verify that it's an instance of NullCloudMetadataClient
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBeanCreation() {
 CookieSerializer cookieSerializer = context.getBean(CookieSerializer.class);
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClientMethods() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 // Test methods that are supposed to throw UnsupportedException
 assertNull(nullCloudMetadataClient.listInstances());
 assertNull(nullCloudMetadataClient.listTenants(""));
 assertNull(nullCloudMetadataClient.getTenant("", ""));
 assertNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
 assertNull(nullCloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint(" instanceId ", " tenantId ");
 assertNotNull(tenantEndpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
 }

 @Test
 public void testGetDatabaseUser() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNull(nullCloudMetadataClient.getSysTenantUser(""));
 }

 @Test
 public void testListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
 }

 @Test
 public void testListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNull(nullCloudMetadataClient.listTenants("instanceId"));
 }

 @Test
 public void testListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNull(nullCloudMetadataClient.listInstances());
 }

 @Test
 public void testGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = context.getBean(NullCloudMetadataClient.class);
 assertNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 }
}