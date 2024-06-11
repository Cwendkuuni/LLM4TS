package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.BeanConfiguration;
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
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeanConfigurationTest {
    private SensitivePropertyHandler sensitivePropertyHandler;
    private BeanConfiguration beanConfiguration;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        beanConfiguration = new BeanConfiguration();
        cloudMetadataClient = mock(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertEquals("$2a$10$KhT.CvKjNJ2y/4x3n4w6uOu4J8ST33r62/9r9Q1vCu5aKb7YpJ1Gm",
                passwordEncoder.encode("test"));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider()
                .findValueSerializer(Internationalizable.class);
        assertEquals(I18nOutputSerializer.class, customOutputSerializer.getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        String filter = "http://test.com/test%25.jsp";
        assertTrue(defaultHttpFirewall.isFilteredRequest(filter));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertSame(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClient_getTenant() {
        beanConfiguration.cloudMetadataClient().getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClient_listInstances() {
        beanConfiguration.cloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClient_listTenants() {
        beanConfiguration.cloudMetadataClient().listTenants("instanceId");
    }

    @Test
    public void testNullCloudMetadataClient() {
        when(beanConfiguration.cloudMetadataClient()).thenReturn(null);
        assertNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }
}