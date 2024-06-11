package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.exception.UnsupportedException;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.common.json.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

/**
 * Test class for BeanConfiguration
 *
 * @author yizhou.xw
 */
public class BeanConfigurationTest {

    private CloudMetadataClient cloudMetadataClient;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        cloudMetadataClient = mock(CloudMetadataClient.class);
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        objectMapper = mock(ObjectMapper.class);

        when(cloudMetadataClient.listInstances()).thenReturn(new ArrayList<>());
        when(cloudMetadataClient.listTenants("instanceId", "tenantId"))
                .thenReturn(new ArrayList<>());
        when(cloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(null);
        when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"))
                .thenReturn(new ArrayList<>());
        when(cloudMetadataClient.getSysTenantUser("instanceId")).thenReturn(null);

        when(sensitivePropertyHandler.decrypt("encryptedValue")).thenAnswer(invocation -> invocation.getValue());

        when(objectMapper.writeValueAsString(Mockito.any())).thenAnswer(invocation -> "serializedValue");

    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration().nullCloudMetadataClient();
        nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
    }

    @Test
    public void testObjectMapperSerializedValue() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        String serializedValue = objectMapper.writeValueAsString(new Internationalizable());
        assertNotNull(serializedValue);
    }

}