package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String plainText = "password123";
        String encodedPassword = passwordEncoder.encode(plainText);
        assertTrue(passwordEncoder.matches(plainText, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String json = "{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"New York\", \"country\": \"USA\"}}";
        I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.customOutputHandling(i18nOutputSerializer));
        Object actualObject = objectMapper.readValue(json, Object.class);
        Object expectedObject = expectedObjectMapper.readValue(json, Object.class);
        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        String maliciousUrl = "http://attacker.com/exploit?id=%25{1}%25";
        assertTrue(httpFirewall.allow(maliciousUrl));
    }

    @Test
    public void testCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        String cookieName = cookieSerializer.getName();
        assertEquals("JSESSIONID", cookieName);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }
}