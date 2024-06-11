package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringRunner.class)
public class BeanConfigurationTest {

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoderBean() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperBean() {
 BeanConfiguration beanConfiguration = new BeanConfiguration(null);
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientBean() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientGetTenant() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient.getTenant(null, null));
 }

 @Test
 public void testNullCloudMetadataClientGetTenantEndpoint() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint(null, null);
 assertNotNull(tenantEndpoint);
 }

 @Test
 public void testNullCloudMetadataClientListTenants() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 List<OBTenant> tenants = nullCloudMetadataClient.listTenants(null);
 assertNotNull(tenants);
 }

 @Test
 public void testNullCloudMetadataClientListInstances() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 List<OBInstance> instances = nullCloudMetadataClient.listInstances();
 assertNotNull(instances);
 }

 @Test
 public void testNullCloudMetadataClientGetSysTenantUser() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 OBDatabaseUser sysTenantUser = nullCloudMetadataClient.getSysTenantUser(null);
 assertNotNull(sysTenantUser);
 }

 @Test
 public void testNullCloudMetadataClientListDatabaseUsers() {
 BeanConfiguration beanConfiguration = new BeanConfiguration();
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
 List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers(null, null);
 assertNotNull(databaseUsers);
 }
}