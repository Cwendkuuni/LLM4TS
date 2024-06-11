package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

/**
 * Test cases for BeanConfiguration class
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-20 10:30
 */
public class BeanConfigurationTest {

    private BeanConfiguration configuration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        this.sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        this.cloudMetadataClient = mock(CloudMetadataClient.class);
        this.configuration = new BeanConfiguration();
        when(this.cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(this.cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(this.cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = this.configuration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = this.configuration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        this.configuration.cloudMetadataClient();
        assertNotNull(this.cloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        nullCloudMetadataClient.listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetSysTenantUser() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = this.configuration.nullCloudMetadataClient();
        nullCloudMetadataClient.getSysTenantUser("instanceId");
    }

    @Test
    public void testCustomObjectMapper() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        CustomOutputSerializer customOutputSerializer = Mockito.spy(new CustomOutputSerializer());
        when(customOutputSerializer.getSerializer(Internationalizable.class)).thenReturn(new I18nOutputSerializer());

        CustomOutputSerializer customOutputSerializerInObjectMapper = objectMapper.getSerializerProvider()
                .findValueSerializer(Internationalizable.class);

        assertEquals(customOutputSerializer, customOutputSerializerInObjectMapper);
    }

    @Test
    public void testCustomObjectMapperSerializationWithI18nOutputSerializer() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        Internationalizable internationalizable = new Internationalizable();
        internationalizable.setMessage("Hello World!");

        String json = objectMapper.writeValueAsString(internationalizable);
        assertEquals("{\"message\":\"Hello World!\"}", json);
    }

    @Test
    public void testCustomObjectMapperDeserializationWithI18nOutputSerializer() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        String json = "{\"message\":\"Hello World!\"}";
        Internationalizable internationalizable = objectMapper.readValue(json, Internationalizable.class);
        assertEquals("Hello World!", internationalizable.getMessage());
    }

    @Test
    public void testCustomObjectMapperSerializationAndDeserializationWithI18nOutputSerializer() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        Internationalizable internationalizable = new Internationalizable();
        internationalizable.setMessage("Hello World!");

        String json = objectMapper.writeValueAsString(internationalizable);
        Internationalizable internationalizableFromJson = objectMapper.readValue(json, Internationalizable.class);
        assertEquals("Hello World!", internationalizableFromJson.getMessage());
    }

    @Test
    public void testCustomObjectMapperWithNullInput() {
        ObjectMapper objectMapper = this.configuration.objectMapper(this.sensitivePropertyHandler);
        String json = null;

        try {
            objectMapper.readValue(json, Internationalizable.class);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }
}