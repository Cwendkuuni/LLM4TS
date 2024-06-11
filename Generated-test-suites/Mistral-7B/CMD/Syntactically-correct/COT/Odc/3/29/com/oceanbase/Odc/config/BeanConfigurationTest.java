package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

 @InjectMocks
 private BeanConfiguration beanConfiguration;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 /**
 * Test the passwordEncoder method
 */
 @Test
 public void testPasswordEncoder() {
 PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
 assertNotNull(passwordEncoder);
 }

 /**
 * Test the objectMapper method
 */
 @Test
 public void testObjectMapper() {
 SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
 ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
 assertNotNull(objectMapper);
 }

 /**
 * Test the defaultHttpFirewall method
 */
 @Test
 public void testDefaultHttpFirewall() {
 HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
 assertNotNull(defaultHttpFirewall);
 }

 /**
 * Test the cloudMetadataClient method
 */
 @Test
 public void testCloudMetadataClient() {
 CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
 assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
 }

 /**
 * Test the cloudMetadataClient supportsCloudMetadata method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientSupportsCloudMetadata() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
 }

 /**
 * Test the cloudMetadataClient needsSysTenantUser method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientNeedsSysTenantUser() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 assertFalse(nullCloudMetadataClient.needsSysTenantUser());
 }

 /**
 * Test the cloudMetadataClient listInstances method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientListInstances() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listInstances();
 }

 /**
 * Test the cloudMetadataClient listTenants method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientListTenants() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listTenants("instanceId");
 }

 /**
 * Test the cloudMetadataClient getTenant method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientGetTenant() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.getTenant("instanceId", "tenantId");
 }

 /**
 * Test the cloudMetadataClient listDatabaseUsers method
 */
 @Test(expected = UnsupportedException.class)
 public void testCloudMetadataClientListDatabaseUsers() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
 }

 /**
 * Test the cloudMetadataClient getTenantEndpoint method
 */
 @Test
 public void testCloudMetadataClientGetTenantEndpoint() {
 NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) beanConfiguration.cloudMetadataClient();
 OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertNotNull(tenantEndpoint);
 }

 /**
 * Test the cookieSerializer method
 */
 @Test
 public void testCookieSerializer() {
 CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
 assertNotNull(cookieSerializer);
 }

}