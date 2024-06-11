package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@Import(BeanConfiguration.class)
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
 CustomOutputSerializer customOutputSerializer = Mockito.spy(customOutputSerializer());
 assertNotNull(objectMapper);
 // Add your assertions for objectMapper here
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
 // Add your assertions for CloudMetadataClient here
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 CloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 // Add your assertions for NullCloudMetadataClient here
 }

 private CustomOutputSerializer customOutputSerializer() {
 return Mockito.spy(JacksonModules.customOutputSerializer());
 }

}