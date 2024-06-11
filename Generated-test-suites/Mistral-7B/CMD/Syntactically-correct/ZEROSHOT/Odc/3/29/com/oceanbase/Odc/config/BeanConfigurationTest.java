package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = { BeanConfiguration.class })
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private SensitivePropertyHandler sensitivePropertyHandler;

 @Before
 public void setUp() {
 AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
 context.register(BeanConfiguration.class);
 context.refresh();
 this.beanConfiguration = context.getBean(BeanConfiguration.class);
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapperBean() {
 ObjectMapper objectMapperBean = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertEquals(this.objectMapper, objectMapperBean);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
 }

 @Test
 public void testCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertEquals(cookieSerializer.getClass(), DefaultCookieSerializer.class);
 }

 @Test(expected = UnsupportedException.class)
 public void testGetTenantShouldThrowExceptionWhenCloudMetadataNotSupported() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 fail("Unexpected return value: " + tenant);
 }

 @Test
 public void testListInstancesShouldReturnEmptyList() {
 List<OBInstance> instances = beanConfiguration.cloudMetadataClient().listInstances();
 assertEquals(Collections.emptyList(), instances);
 }

 @Test
 public void testListTenantsShouldReturnEmptyList() {
 String instanceId = "instanceId";
 List<OBTenant> tenants = beanConfiguration.cloudMetadataClient().listTenants(instanceId);
 assertEquals(Collections.emptyList(), tenants);
 }

 @Test
 public void testGetTenantEndpointShouldReturnDefaultTenantEndpoint() {
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 OBTenantEndpoint tenantEndpoint = beanConfiguration.cloudMetadataClient().getTenantEndpoint(instanceId, tenantId);
 assertNotNull(tenantEndpoint);
 assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
 }

 @Test
 public void testListDatabaseUsersShouldReturnEmptyList() {
 String instanceId = "instanceId";
 String tenantId = "tenantId";
 List<OBDatabaseUser> databaseUsers = beanConfiguration.cloudMetadataClient().listDatabaseUsers(instanceId, tenantId);
 assertEquals(Collections.emptyList(), databaseUsers);
 }

 @Test
 public void testGetSysTenantUserShouldReturnNull() {
 String instanceId = "instanceId";
 OBDatabaseUser sysTenantUser = beanConfiguration.cloudMetadataClient().getSysTenantUser(instanceId);
 assertNull(sysTenantUser);
 }

 @Test
 public void testObjectMapperShouldParseYaml() throws Exception {
 String yaml = "foo: bar";
 YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
 Properties properties = yamlPropertiesFactoryBean.getObject();
 String foo = properties.getProperty("foo");
 assertEquals("bar", foo);

 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 List<String> strings = objectMapper.readValue(yaml, new TypeReference<List<String>>(){});
 assertEquals("bar", strings.get(0));
 }
}