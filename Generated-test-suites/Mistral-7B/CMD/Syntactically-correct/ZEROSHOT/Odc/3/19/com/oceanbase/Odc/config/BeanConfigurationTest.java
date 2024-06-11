package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapper() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testNullCloudMetadataClientInstanceIdTenantId() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertNotNull(tenantEndpoint);
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 nullCloudMetadataClient.listInstances();
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 nullCloudMetadataClient.listTenants("instanceId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }

 @Test(expected = UnsupportedException.class)
 public void testNullCloudMetadataClientGetSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 nullCloudMetadataClient.getSysTenantUser("instanceId");
 }

 @Test
 public void testI18nOutputSerializer() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 I18nOutputSerializer i18nOutputSerializer = objectMapper.getValueWriterFactory().getValueWriter(Internationalizable.class).getValueWriter();
 assertNotNull(i18nOutputSerializer);
 }

 @Test
 public void testI18nOutputSerializerSerialize() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 Internationalizable i18nOutput = new Internationalizable();
 i18nOutput.setMessage("test message");
 String serialized = objectMapper.writerWithView(CustomOutputSerializer.class).writeValueAsString(i18nOutput);
 assertNotNull(serialized);
 }

}