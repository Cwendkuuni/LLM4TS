package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapperUtils;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

/**
 * Test cases for the BeanConfiguration class
 */
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        CustomOutputSerializer customOutputSerializer = ObjectMapperUtils.getCustomOutputSerializer(objectMapper);
        assertNotNull(objectMapper);
        assertNotNull(customOutputSerializer);
        assertNotNull(customOutputSerializer.getSerializer(Internationalizable.class));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        CloudMetadataClient nullCloudMetadataClient = mock(CloudMetadataClient.class);
        when(nullCloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(nullCloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(nullCloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(nullCloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(nullCloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
        when(nullCloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);

        List<OBInstance> instanceList = Collections.emptyList();
        when(nullCloudMetadataClient.listInstances()).thenReturn(instanceList);

        List<OBTenant> tenantList = Collections.emptyList();
        when(nullCloudMetadataClient.listTenants(any(String.class))).thenReturn(tenantList);

        Optional<OBTenant> tenantOptional = Optional.empty();
        when(nullCloudMetadataClient.getTenant(any(String.class), any(String.class)))
                .thenReturn(tenantOptional.orElse(new OBTenant()));

        List<OBDatabaseUser> databaseUserList = Collections.emptyList();
        when(nullCloudMetadataClient.listDatabaseUsers(any(String.class), any(String.class)))
                .thenReturn(databaseUserList);

        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        when(nullCloudMetadataClient.getTenantEndpoint(any(String.class), any(String.class)))
                .thenReturn(tenantEndpoint);

        OBDatabaseUser sysTenantUser = mock(OBDatabaseUser.class);
        when(nullCloudMetadataClient.getSysTenantUser(any(String.class))).thenReturn(sysTenantUser);

        NullCloudMetadataClient nullInstance = new BeanConfiguration().nullCloudMetadataClient();
        assertNotNull(nullInstance);
    }
}