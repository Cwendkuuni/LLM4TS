package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapperBuilder;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerImpl;
import com.oceanbase.odc.service.connection.model.NullCloudMetadataClient;

@Configuration
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sensitivePropertyHandler = new SensitivePropertyHandlerImpl();
        objectMapper = ObjectMapperBuilder.create().build();
        ReflectionTestUtils.setFieldValue(beanConfiguration, "sensitivePropertyHandler", sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper expectedObjectMapper = objectMapper;
        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(mock(SensitivePropertyHandler.class));
        assertNotNull(actualObjectMapper);
        assertEquals(expectedObjectMapper, actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
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
        assertTrue(nullCloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
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
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
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
}