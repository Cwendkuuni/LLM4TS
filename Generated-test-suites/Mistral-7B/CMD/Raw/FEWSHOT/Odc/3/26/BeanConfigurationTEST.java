package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("unchecked")
public class BeanConfigurationTest {

    private AnnotationConfigApplicationContext context;
    private BeanConfiguration config;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private HttpFirewall httpFirewall;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        config = context.getBean(BeanConfiguration.class);
        sensitivePropertyHandler = context.getBean(SensitivePropertyHandler.class);
        objectMapper = context.getBean(ObjectMapper.class);
        httpFirewall = context.getBean(HttpFirewall.class);
        cloudMetadataClient = context.getBean(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        assertTrue(passwordEncoder.matches(originalPassword, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapperBean = context.getBean(ObjectMapper.class);
        assertEquals(objectMapperBean, objectMapper);
        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider().getSerializerProvider().findValueSerializer(Internationalizable.class);
        assertEquals(I18nOutputSerializer.class, customOutputSerializer.getClass());
    }

    @Test
    public void testHttpFirewall() {
        String maliciousUrl = "http://example.com/<script>alert('XSS')</script>";
        assertFalse(httpFirewall.isUrlPatternSafe(maliciousUrl));
        String safeUrl = "http://example.com/safe";
        assertTrue(httpFirewall.isUrlPatternSafe(safeUrl));
    }

    @Test
    public void testCloudMetadataClient() {
        assertNotNull(cloudMetadataClient);
        verify(cloudMetadataClient, never()).listInstances();
        verify(cloudMetadataClient, never()).listTenants(anyString());
        verify(cloudMetadataClient, never()).getTenant(anyString(), anyString());
        verify(cloudMetadataClient, never()).listDatabaseUsers(anyString(), anyString());
        verify(cloudMetadataClient, never()).getTenantEndpoint(anyString(), anyString());
        verify(cloudMetadataClient, never()).getSysTenantUser(anyString());
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientMethods() {
        cloudMetadataClient.listInstances();
        cloudMetadataClient.listTenants("instanceId");
        cloudMetadataClient.getTenant("instanceId", "tenantId");
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        cloudMetadataClient.getSysTenantUser("instanceId");
    }
}