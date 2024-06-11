package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.model.NullCloudMetadataClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;
    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall httpFirewall;
    private CookieSerializer cookieSerializer;

    @Before
    public void setUp() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = mock(NullCloudMetadataClient.class);
        passwordEncoder = new BCryptPasswordEncoder();
        objectMapper = new ObjectMapper();
        httpFirewall = new DefaultHttpFirewall();
        cookieSerializer = new DefaultCookieSerializer();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder actualPasswordEncoder = BeanConfiguration.passwordEncoder();
        assertEquals(actualPasswordEncoder, passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper actualObjectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall actualHttpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertEquals(actualHttpFirewall, httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient actualCloudMetadataClient = BeanConfiguration.cloudMetadataClient();
        assertNull(actualCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer actualCookieSerializer = BeanConfiguration.cookieSerializer();
        assertEquals(actualCookieSerializer, cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertNotNull(actualNullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(actualNullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(actualNullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(actualNullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(actualNullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertTrue(actualNullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertEquals(actualNullCloudMetadataClient.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> actualNullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> actualNullCloudMetadataClient.listTenants(""));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> actualNullCloudMetadataClient.getTenant("", ""));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> actualNullCloudMetadataClient.listDatabaseUsers("", ""));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        OBTenantEndpoint actualOBTenantEndpoint = actualNullCloudMetadataClient.getTenantEndpoint("", "");
        assertNotNull(actualOBTenantEndpoint);
        assertEquals(actualOBTenantEndpoint.getAccessMode(), OceanBaseAccessMode.DIRECT);
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient actualNullCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertThrows(UnsupportedException.class, () -> actualNullCloudMetadataClient.getSysTenantUser(""));
    }

    @Test
    public void testObjectMapperWithSensitivePropertyHandler() {
        ObjectMapper actualObjectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(actualObjectMapper);
    }
}