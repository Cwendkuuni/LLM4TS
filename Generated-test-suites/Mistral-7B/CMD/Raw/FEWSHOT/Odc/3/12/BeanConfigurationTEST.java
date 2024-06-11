package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.sensitive.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerImpl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.validation.constraints.NotBlank;

@SuppressWarnings("unchecked")
public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private HttpFirewall httpFirewall;
    private CloudMetadataClient cloudMetadataClient;
    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        sensitivePropertyHandler = new SensitivePropertyHandlerImpl();
        objectMapper = JacksonFactory.unsafeJsonMapper();
        httpFirewall = mock(HttpFirewall.class);
        cloudMetadataClient = mock(CloudMetadataClient.class);
        cookieSerializer = new DefaultCookieSerializer();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = BeanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        PasswordEncoder expectedPasswordEncoder = new BCryptPasswordEncoder();
        assertEquals(expectedPasswordEncoder, passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        CustomOutputSerializer customOutputSerializer = objectMapper.getSerializerProvider()
                .findValueSerializer(Internationalizable.class);
        assertNotNull(customOutputSerializer);
        I18nOutputSerializer expectedSerializer = new I18nOutputSerializer();
        assertEquals(expectedSerializer, customOutputSerializer);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        DefaultHttpFirewall expectedDefaultHttpFirewall = new DefaultHttpFirewall();
        assertEquals(expectedDefaultHttpFirewall, defaultHttpFirewall);
        when(expectedDefaultHttpFirewall.isAllowUrlEncodedSlash()).thenReturn(true);
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = BeanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = BeanConfiguration.NullCloudMetadataClient.instance();
        assertNotNull(nullCloudMetadataClient);
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertNull(nullCloudMetadataClient.listInstances());
        assertNull(nullCloudMetadataClient.listTenants(""));
        assertNull(nullCloudMetadataClient.getTenant("", ""));
        assertNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
        assertNull(nullCloudMetadataClient.getTenantEndpoint("", ""));
        assertNull(nullCloudMetadataClient.getSysTenantUser(""));
    }

    @Test
    public void testObjectMapperWithSensitiveData() {
        ObjectMapper objectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
        assertEquals(expectedObjectMapper, objectMapper);
    }
}