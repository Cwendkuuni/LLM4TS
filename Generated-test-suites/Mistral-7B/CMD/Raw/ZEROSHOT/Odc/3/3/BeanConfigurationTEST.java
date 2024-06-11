package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        assertTrue(passwordEncoder.matches(originalPassword, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        String json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"Sport\", \"Music\"]}";
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper();
        Object actualObject = objectMapper.readValue(json, Object.class);
        Object expectedObject = expectedObjectMapper.readValue(json, Object.class);
        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        String urlWithSlash = "http://example.com/test%2F";
        assertTrue(defaultHttpFirewall.allowUrl(urlWithSlash));
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantThrowsException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesThrowsException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsThrowsException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersThrowsException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUserThrowsException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }

    @Test
    public void testGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertEquals(OceanBaseAccessMode.DIRECT, tenantEndpoint.getAccessMode());
    }
}