package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        JacksonModules customModules = new JacksonModules();
        ObjectMapper mapper1 = JacksonFactory.unsafeJsonMapper();
        ObjectMapper mapper2 = new BeanConfiguration().objectMapper(sensitivePropertyHandler);

        CustomOutputSerializer customOutputSerializer1 = mapper1.getSerializerProvider().getSerializerProvider().findValueSerializer(Internationalizable.class);
        CustomOutputSerializer customOutputSerializer2 = mapper2.getSerializerProvider().getSerializerProvider().findValueSerializer(Internationalizable.class);

        assertNotEquals(customOutputSerializer1, customOutputSerializer2);
        assertInstanceOf(I18nOutputSerializer.class, customOutputSerializer2);

        ObjectMapper mapper3 = new BeanConfiguration().objectMapper(null);
        assertNull(mapper3.getSerializerProvider().getEncoder(String.class));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String url = "/test/%65%6e%63%75%61%67%65%2Ftest";
        assertFalse(defaultHttpFirewall.isSanitized(url));
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertTrue(defaultHttpFirewall.isSanitized(url));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }
}