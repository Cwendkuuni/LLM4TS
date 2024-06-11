package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 private BeanConfiguration beanConfiguration;
 private SensitivePropertyHandler sensitivePropertyHandler;
 private JacksonFactory jacksonFactory;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 beanConfiguration = new BeanConfiguration();
 sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
 jacksonFactory = Mockito.spy(JacksonFactory.class);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);

 ObjectMapper spyObjectMapper = Mockito.spy(objectMapper);
 assertEquals(jacksonFactory.unsafeJsonMapper(), spyObjectMapper.getDelegate());
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
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
 OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 fail("getTenant method should throw UnsupportedException");
 }
}