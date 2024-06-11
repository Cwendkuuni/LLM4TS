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
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;
 @InjectMocks
 private I18nOutputSerializer i18nOutputSerializer;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
 assertNotNull(objectMapper);
 assertEquals(objectMapper.getClass(), JacksonFactory.unsafeJsonMapper().getClass());
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantEndpointUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListInstancesUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testListTenantsUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listTenants("instanceId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testListDatabaseUsersUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testGetSysTenantUserUnsupported() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.getSysTenantUser("instanceId");
 }

 @Test
 public void testCustomOutputSerializer() {
 CustomOutputSerializer customOutputSerializer = beanConfiguration.objectMapper(null)
 .getValueFactory()
 .getSerializerProvider()
 .getSerializerForClass(Internationalizable.class);
 assertTrue(customOutputSerializer instanceof I18nOutputSerializer);
 }

}