package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.BeanConfiguration;
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

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = { BeanConfiguration.class })
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        boolean isMatch = passwordEncoder.matches(originalPassword, encodedPassword);
        assertTrue(isMatch);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String jsonString = "{\"name\": \"John Smith\",\"age\": 30,\"address\": {\"city\": \"New York\",\"country\": \"USA\"}}";
        CustomOutputSerializer customOutputSerializer = Mockito.spy(new CustomOutputSerializer());
        I18nOutputSerializer i18nOutputSerializer = Mockito.mock(I18nOutputSerializer.class);
        Mockito.doReturn(i18nOutputSerializer).when(customOutputSerializer).getOutputSerializer(Internationalizable.class);
        JacksonModules jacksonModules = Mockito.mock(JacksonModules.class);
        Mockito.when(jacksonModules.customOutputHandling(customOutputSerializer)).then(invocation -> {
            ObjectMapper objectMapper1 = invocation.getArgument(0);
            objectMapper1.registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt));
            return objectMapper1;
        });
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertEquals(expectedObjectMapper, objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        String url = "http://example.com/%25abc";
        assertTrue(defaultHttpFirewall.allowUrlEncoded(url));
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertTrue(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertFalse(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertTrue(nullCloudMetadataClient.listInstances().isEmpty());
        assertTrue(nullCloudMetadataClient.listTenants("").isEmpty());
        assertNull(nullCloudMetadataClient.getTenant("", ""));
        assertTrue(nullCloudMetadataClient.listDatabaseUsers("", "").isEmpty());
        assertNull(nullCloudMetadataClient.getSysTenantUser(""));
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("", ""));
        OBDatabaseUser dbUser = new OBDatabaseUser();
        dbUser.setUserId("user1");
        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        tenantEndpoint.setDatabaseUser(dbUser);
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("", "").getDatabaseUser());
        assertNotNull(nullCloudMetadataClient.getTenantEndpoint("", "").getDatabaseUser().getId());
    }
}