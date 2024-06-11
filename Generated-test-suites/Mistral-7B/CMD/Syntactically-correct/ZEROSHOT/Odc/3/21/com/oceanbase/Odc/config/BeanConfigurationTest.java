package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapper() {
 SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
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
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
 // Test methods that do not throw exceptions
 nullCloudMetadataClient.includeClusterNameForJdbcUsername();
 nullCloudMetadataClient.oceanbaseAccessMode();
 // Test methods that throw exceptions
 try {
 nullCloudMetadataClient.listInstances();
 nullCloudMetadataClient.listTenants("instanceId", "tenantId");
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 nullCloudMetadataClient.getSysTenantUser("instanceId");
 } catch (UnsupportedException e) {
 // Expected
 }
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

}