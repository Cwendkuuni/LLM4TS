package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider()
                .findValueSerializer(Internationalizable.class);
        assertNotNull(customOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }

    @Test
    public void testSensitivePropertyHandler() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        assertEquals("decryptedValue", sensitivePropertyHandler.decrypt("encryptedValue"));
    }

    @Test
    public void testObjectMapperWithSensitivePropertyHandler() {
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapperWithHandler = objectMapper
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertNotNull(objectMapperWithHandler);
    }

    @Test
    public void testObjectMapperWithCustomOutputSerializer() {
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapperWithSerializer = objectMapper
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertNotNull(objectMapperWithSerializer);
    }
}