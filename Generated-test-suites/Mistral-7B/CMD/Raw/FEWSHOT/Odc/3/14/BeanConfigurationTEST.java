package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@EnableConfiguration(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall defaultHttpFirewall;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;
    private CookieSerializer cookieSerializer;

    @Before
    public void setUp() {
        passwordEncoder = beanConfiguration.passwordEncoder();
        objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cookieSerializer = beanConfiguration.cookieSerializer();
    }

    @Test
    public void testPasswordEncoder() {
        assertEquals("$2a$10$SLs8uqr4BmQsBf3AiZMg5OwIWQ3JX5h1RzXV4oEW84SrRwL.nP62",
                passwordEncoder.encode("password"));
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(objectMapper);
       ObjectMapper objectMapperSpy = spy(objectMapper);
       JacksonModules jacksonModules = objectMapperSpy.getSerializerConfig().getConfigParser().parseConfiguration(
                new StringReader("[\n" +
                        "{\"class\": \"com.oceanbase.odc.common.i18n.I18nOutputSerializer\",\n" +
                        "\"properties\": {\n" +
                        "\"outputSerializerClass\": \"com.oceanbase.odc.common.i18n.I18nOutputSerializer\"\n" +
                        "}}\n" +
                        "]"));
        verify(objectMapperSpy).registerModule(eq(jacksonModules));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall.allowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
       verify(cloudMetadataClient, never()).listInstances();
       verify(cloudMetadataClient, never()).listTenants(anyString());
       verify(cloudMetadataClient, never()).getTenant(anyString(), anyString());
       verify(cloudMetadataClient, never()).listDatabaseUsers(anyString(), anyString());
       verify(cloudMetadataClient, never()).getTenantEndpoint(anyString(), anyString());
       verify(cloudMetadataClient, never()).getSysTenantUser(anyString());
    }

    @Test
    public void testCookieSerializer() {
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }
}