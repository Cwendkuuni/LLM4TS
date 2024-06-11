package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private I18nOutputSerializer i18nOutputSerializer;

 @Test
 public void testPasswordEncoder() {
 assertNotNull(passwordEncoder);
 assertSame(BCryptPasswordEncoder.class, passwordEncoder.getClass());
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider().getSerializerProvider().findValueSerializer(Internationalizable.class);
 assertNotNull(customOutputSerializer);
 assertSame(I18nOutputSerializer.class, customOutputSerializer.getClass());
 }

 @Test
 public void testDefaultHttpFirewall() {
 assertNotNull(beanConfiguration.defaultHttpFirewall());
 }

 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertSame(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
 }

 @Test
 public void testCookieSerializer() {
 assertNotNull(beanConfiguration.cookieSerializer());
 }
}