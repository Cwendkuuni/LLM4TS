package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private NullCloudMetadataClient nullCloudMetadataClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sensitivePropertyHandler = new SensitivePropertyHandler();
        objectMapper = JacksonFactory.unsafeJsonMapper();
        nullCloudMetadataClient = new NullCloudMetadataClient();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String plainText = "testPassword";
        String expected = "$2a$10$K/J8kKqF4Qv7Xx7GJW3iJu17qB85vw4t1j2p6/q6TqR4uK9/3PqT";
        String encodedPassword = passwordEncoder.encode(plainText);
        assertTrue(passwordEncoder.matches(plainText, encodedPassword));
        assertEquals(expected, encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapperUnderTest = beanConfiguration.objectMapper(sensitivePropertyHandler);
        ObjectMapper expected = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertEquals(expected, objectMapperUnderTest);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        DefaultHttpFirewall expectedDefaultHttpFirewall = new DefaultHttpFirewall();
        expectedDefaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertEquals(expectedDefaultHttpFirewall, defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertEquals(nullCloudMetadataClient, beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        DefaultCookieSerializer expectedCookieSerializer = new DefaultCookieSerializer();
        expectedCookieSerializer.setCookieName("JSESSIONID");
        assertEquals(expectedCookieSerializer, cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClientUnderTest = beanConfiguration.nullCloudMetadataClient();
        NullCloudMetadataClient expectedNullCloudMetadataClient = new NullCloudMetadataClient();
        assertEquals(expectedNullCloudMetadataClient, nullCloudMetadataClientUnderTest);
    }
}