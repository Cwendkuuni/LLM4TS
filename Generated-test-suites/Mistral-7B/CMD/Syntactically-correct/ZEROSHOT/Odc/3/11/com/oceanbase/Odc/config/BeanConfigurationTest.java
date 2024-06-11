package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private ObjectMapper objectMapper;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapper() {
 SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
 ObjectMapper objMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
 assertNotNull(objMapper);
 }

 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
 assertNotNull(cloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test
 public void testNullCloudMetadataClient() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
 assertNotNull(nullCloudMetadataClient);
 }

 @Test
 public void testObjectMapperWithI18nOutputSerializer() {
 ObjectMapper objMapper = JacksonFactory.unsafeJsonMapper();
 objMapper.registerModule(JacksonModules.customOutputHandling(new I18nOutputSerializer()));
 ObjectMapper objMapperBeans = new BeanConfiguration().objectMapper(Mockito.mock(SensitivePropertyHandler.class));
 assertNotNull(objMapperBeans);
 I18nOutputSerializer i18nOutputSerializer = Mockito.spy(new I18nOutputSerializer());
 ObjectMapper objMapperSpy = Mockito.spy(objMapper);
 Mockito.doReturn(i18nOutputSerializer).when(objMapperBeans).getSerializerProvider()
 .findValueSerializer(Internationalizable.class);
 assertNotNull(objMapperBeans.getValueFactory());
 }
}