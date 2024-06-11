package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.Internationalizable;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for BeanConfiguration
 */
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test initialization of BeanConfiguration
     */
    @Test
    public void testInitialization() {
        // Verify that the passwordEncoder bean is created
        assertNotNull(beanConfiguration.passwordEncoder());

        // Verify that the objectMapper bean is created
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        CustomOutputSerializer customOutputSerializer = objectMapper.getDefaultSerializerProvider()
                .findValueSerializer(Internationalizable.class);
        assertNotNull(customOutputSerializer);
        assertTrue(customOutputSerializer instanceof I18nOutputSerializer);
    }

    /**
     * Test creation of passwordEncoder bean
     */
    @Test
    public void testPasswordEncoderBean() {
        assertEquals(BCryptPasswordEncoder.class, beanConfiguration.passwordEncoder().getClass());
    }

    /**
     * Test creation of objectMapper bean
     */
    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        // Test serialization of Internationalizable objects
        Internationalizable intlObj = mock(Internationalizable.class);
        when(intlObj.getLabel()).thenReturn("test label");
        String serialized = objectMapper.writeValueAsString(intlObj);
        assertEquals("{\"label\":\"test label\"}", serialized);

        // Test deserialization of Internationalizable objects
        String deserializedJson = "{\"label\":\"test label\"}";
        Internationalizable deserialized = objectMapper.readValue(deserializedJson, Internationalizable.class);
        assertEquals("test label", deserialized.getLabel());

        // Test sensitive data handling
        String sensitiveData = "password123";
        when(sensitivePropertyHandler.decrypt("encrypted_password123")).thenReturn(sensitiveData);
        ObjectMapper sensitiveObjectMapper = objectMapper.copy();
        ObjectMapper sensitiveDeserializer = sensitiveObjectMapper
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
        String serializedWithSensitiveData = sensitiveDeserializer.writeValueAsString(sensitiveData);
        assertEquals("encrypted_password123", sensitiveObjectMapper.readValue(serializedWithSensitiveData, String.class));
    }

    /**
     * Test creation of defaultHttpFirewall bean
     */
    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(defaultHttpFirewall.allowUrlEncodedSlash());
    }

    /**
     * Test creation of cloudMetadataClient bean
     */
    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    /**
     * Test creation of cookieSerializer bean
     */
    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }
}