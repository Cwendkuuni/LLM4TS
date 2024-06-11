package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 sensitivePropertyHandler = Mockito.spy(new SensitivePropertyHandler());
 objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 }

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
}