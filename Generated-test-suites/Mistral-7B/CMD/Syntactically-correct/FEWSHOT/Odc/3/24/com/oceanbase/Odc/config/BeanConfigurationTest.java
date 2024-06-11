package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
public class BeanConfigurationTest {

    private BCryptPasswordEncoder passwordEncoder;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private I18nOutputSerializer i18nOutputSerializer;
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        i18nOutputSerializer = new I18nOutputSerializer();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, i18nOutputSerializer);
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        beanConfiguration = new BeanConfiguration() {
            {
                this.passwordEncoder = passwordEncoder;
                this.objectMapper = objectMapper;
                this.defaultHttpFirewall = new DefaultHttpFirewall();
                this.sensitivePropertyHandler = sensitivePropertyHandler;
            }
        };
    }

    @Test
    public void testPasswordEncoder() {
        String encodedPassword = beanConfiguration.passwordEncoder().encode("password");
        String decodedPassword = beanConfiguration.passwordEncoder().decode("encodedPassword");

        assertNotNull(encodedPassword);
        assertEquals("password", decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        String jsonString = "{\"name\":\"John\",\"age\":30}";
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        assertEquals(John.class, objectMapper.readValue(jsonString, John.class).getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        String url = "http://example.com/%25test";

        assertTrue(beanConfiguration.defaultHttpFirewall().allow(url));
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        assertNotNull(cloudMetadataClient);
        assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        cloudMetadataClient.getTenant("instanceId", "tenantId");
    }

}

class John {
    String name;
    int age;
}