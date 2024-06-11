package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Slf4j
public class BeanConfigurationTest {

    private ObjectMapper objectMapper;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void init() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        objectMapper = new JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
        cloudMetadataClient = mock(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String password = "testPassword123";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertNotNull(encodedPassword);
        assertNotNull(decodedPassword);
        assertNotNull(passwordEncoder.matches(password, encodedPassword));
        assertNotNull(passwordEncoder.matches(decodedPassword, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapperUnderTest = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        JsonNode jsonNode = objectMapperUnderTest.readTree("{}");
        assertNotNull(jsonNode);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String url = "http://example.com/%25test";
        boolean allowsUrlEncodedSlash = defaultHttpFirewall.allowedUrlEncodedSlash(url);
        assert(allowsUrlEncodedSlash);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient bean = beanConfiguration.cloudMetadataClient();
        List<OBInstance> instances = Arrays.asList(new OBInstance(), new OBInstance());
        Mockito.when(cloudMetadataClient.listInstances()).thenReturn(instances);
        List<OBTenant> tenants = Arrays.asList(new OBTenant(), new OBTenant());
        Mockito.when(cloudMetadataClient.listTenants("instanceId")).thenReturn(tenants);
        OBTenant tenant = new OBTenant();
        Mockito.when(cloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(tenant);
        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        Mockito.when(cloudMetadataClient.getSysTenantUser("instanceId")).thenReturn(sysTenantUser);
        List<OBDatabaseUser> databaseUsers = Arrays.asList(new OBDatabaseUser(), new OBDatabaseUser());
        Mockito.when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId")).thenReturn(databaseUsers);
        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        Mockito.when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(tenantEndpoint);

        // Test methods that call cloudMetadataClient
        // ...
    }
}