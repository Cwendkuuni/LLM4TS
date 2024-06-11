package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

import lombok.extern.slf4j.Slf4j;

/**
 * Unit tests for BeanConfiguration class
 *
 * @author yizhou.xw
 */
@Slf4j
@Configuration
public class BeanConfigurationTest {

    private PasswordEncoder passwordEncoder;
    private ObjectMapper objectMapper;
    private HttpFirewall defaultHttpFirewall;
    private CloudMetadataClient cloudMetadataClient;
    private CookieSerializer cookieSerializer;

    @Before
    public void setup() {
        passwordEncoder = mock(PasswordEncoder.class);
        objectMapper = mock(ObjectMapper.class);
        defaultHttpFirewall = mock(DefaultHttpFirewall.class);
        cloudMetadataClient = mock(CloudMetadataClient.class);
        cookieSerializer = mock(CookieSerializer.class);
    }

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        assertEquals(bCryptPasswordEncoder.encode("password"), BeanConfiguration.passwordEncoder().encode("password"));
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(Mockito.any(SensitivePropertyHandler.class)::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertEquals(expectedObjectMapper, BeanConfiguration.objectMapper(Mockito.mock(SensitivePropertyHandler.class)));
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertEquals(defaultHttpFirewall, BeanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertNull(nullCloudMetadataClient.listInstances());
        assertNull(nullCloudMetadataClient.listTenants("instanceId"));
        assertNull(nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertNull(nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertNull(nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertNull(nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        assertEquals(serializer, BeanConfiguration.cookieSerializer());
    }

}