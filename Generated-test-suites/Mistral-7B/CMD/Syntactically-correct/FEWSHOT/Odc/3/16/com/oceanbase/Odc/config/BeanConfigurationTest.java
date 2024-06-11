package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.BeanConfigurationTest;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@Configuration
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        assertEquals("2b11b6d56e8f510b76c901a9f271d788c0b2129b6f1b0c1b2b139eb4fb9d4e98", encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);

        ArgumentCaptor<CustomOutputSerializer> customOutputSerializerArgumentCaptor = ArgumentCaptor.forClass(CustomOutputSerializer.class);
        assertEquals(true, objectMapper.getSerializerProviders().stream()
                .anyMatch(serializerProvider -> serializerProvider.getSerializerBindings().getSerializerBindingsForType(Internationalizable.class) == customOutputSerializerArgumentCaptor.capture()));
        I18nOutputSerializer i18nOutputSerializer = customOutputSerializerArgumentCaptor.getValue();
        assertEquals("i18n", i18nOutputSerializer.getLocale());

        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(i18nOutputSerializer));
        assertEquals(expectedObjectMapper.getClass(), objectMapper.getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        assertFalse(defaultHttpFirewall.isAllowUrlEncodedSlash());
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());
        List<OBInstance> instances = new ArrayList<>();
        instances.add(new OBInstance());
        assertFalse(cloudMetadataClient.listInstances().equals(instances));
        List<OBTenant> tenants = new ArrayList<>();
        tenants.add(new OBTenant());
        assertFalse(cloudMetadataClient.listTenants("").equals(tenants));
        OBTenant tenant = new OBTenant();
        assertNull(cloudMetadataClient.getTenant("", ""));
        List<OBDatabaseUser> databaseUsers = new ArrayList<>();
        databaseUsers.add(new OBDatabaseUser());
        assertFalse(cloudMetadataClient.listDatabaseUsers("", "").equals(databaseUsers));
        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        assertNull(cloudMetadataClient.getSysTenantUser(""));
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantWithEmptyInstanceIdOrTenantId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.getTenant("", "");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantEndpointWithEmptyInstanceIdOrTenantId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.getTenantEndpoint("", "");
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsWithEmptyInstanceId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listTenants("");
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesWithEmptyInstanceId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersWithEmptyInstanceIdOrTenantId() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration().cloudMetadataClient();
        cloudMetadataClient.listDatabaseUsers("", "");
    }
}