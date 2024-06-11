package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class BeanConfigurationTest {

    private ObjectMapper objectMapper;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = mock(CloudMetadataClient.class);
        objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertEquals(expectedObjectMapper, objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String urlWithSlash = "http://example.com/path%2F";
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash(urlWithSlash));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        verify(cloudMetadataClient, times(0)).supportsCloudMetadata();
        verify(cloudMetadataClient, times(0)).supportsTenantInstance();
        verify(cloudMetadataClient, times(0)).needsOBTenantName();
        verify(cloudMetadataClient, times(0)).needsSysTenantUser();
        verify(cloudMetadataClient, times(0)).includeClusterNameForJdbcUsername();
        verify(cloudMetadataClient, times(0)).oceanbaseAccessMode();
        verify(cloudMetadataClient, times(0)).listInstances();
        verify(cloudMetadataClient, times(0)).listTenants("");
        verify(cloudMetadataClient, times(0)).getTenant("", "");
        verify(cloudMetadataClient, times(0)).listDatabaseUsers("");
        verify(cloudMetadataClient, times(0)).getTenantEndpoint("");
        verify(cloudMetadataClient, times(0)).getSysTenantUser("");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        when(cloudMetadataClient.getTenant("", ""))
                .thenReturn(null);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.getTenant("", "");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        when(cloudMetadataClient.listDatabaseUsers("", ""))
                .thenReturn(null);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.listDatabaseUsers("", "");
    }

    @Test
    public void testGetTenantEndpoint() {
        OBTenantEndpoint expectedTenantEndpoint = new OBTenantEndpoint();
        expectedTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        when(cloudMetadataClient.getTenantEndpoint("", ""))
                .thenReturn(expectedTenantEndpoint);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        OBTenantEndpoint actualTenantEndpoint = beanConfiguration.getTenantEndpoint("", "");
        assertEquals(expectedTenantEndpoint, actualTenantEndpoint);
    }

    @Test
    public void testListInstances() {
        List<OBInstance> expectedInstances = Arrays.asList(new OBInstance(), new OBInstance());
        when(cloudMetadataClient.listInstances())
                .thenReturn(expectedInstances);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        List<OBInstance> actualInstances = beanConfiguration.listInstances();
        assertEquals(expectedInstances, actualInstances);
    }

    @Test
    public void testListTenants() {
        List<OBTenant> expectedTenants = Arrays.asList(new OBTenant(), new OBTenant());
        when(cloudMetadataClient.listTenants(""))
                .thenReturn(expectedTenants);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        List<OBTenant> actualTenants = beanConfiguration.listTenants("");
        assertEquals(expectedTenants, actualTenants);
    }
}