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
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.ArrayList;
import java.util.List;

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
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        PasswordEncoder passwordEncoderBean = BeanConfiguration.passwordEncoder();
        String encodedPassword = passwordEncoderBean.encode("password");
        assertNotNull(encodedPassword);
        assertEquals("encodedPassword", encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        CustomOutputSerializer customOutputSerializer = mock(CustomOutputSerializer.class);
        when(customOutputSerializer.addSerializer(Internationalizable.class, (CustomOutputSerializer) any())).thenReturn(customOutputSerializer);
        when(objectMapper.registerModule(any())).thenAnswer(i -> objectMapper);
        ObjectMapper objectMapperBean = BeanConfiguration.objectMapper(mock(SensitivePropertyHandler.class));
        assertNotNull(objectMapperBean);
        assertEquals(objectMapper, objectMapperBean);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewallBean = BeanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewallBean);
        assertEquals(defaultHttpFirewall, defaultHttpFirewallBean);
    }

    @Test
    public void testCloudMetadataClient() {
        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
        when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
        CloudMetadataClient cloudMetadataClientBean = BeanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClientBean);
        assertEquals(cloudMetadataClient, cloudMetadataClientBean);
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = mock(DefaultCookieSerializer.class);
        when(serializer.setCookieName("JSESSIONID")).thenReturn(serializer);
        when(cookieSerializer().getClass()).thenReturn(serializer.getClass());
        CookieSerializer cookieSerializerBean = BeanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializerBean);
        assertEquals(cookieSerializer, cookieSerializerBean);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClientBean);
        assertEquals(BeanConfiguration.NullCloudMetadataClient.class, nullCloudMetadataClientBean.getClass());
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClientBean.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClientBean.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClientBean.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertFalse(nullCloudMetadataClientBean.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertTrue(nullCloudMetadataClientBean.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClientBean.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        when(cloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNull(nullCloudMetadataClientBean.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        when(cloudMetadataClient.listTenants("instanceId")).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNull(nullCloudMetadataClientBean.listTenants("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        String instanceId = "instanceId";
        String tenantId = "tenantId";
        when(cloudMetadataClient.getTenant(instanceId, tenantId)).thenThrow(new UnsupportedException("getTenant not supported in non-cloud"));
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNull(nullCloudMetadataClientBean.getTenant(instanceId, tenantId));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        String instanceId = "instanceId";
        String tenantId = "tenantId";
        when(cloudMetadataClient.listDatabaseUsers(instanceId, tenantId)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNull(nullCloudMetadataClientBean.listDatabaseUsers(instanceId, tenantId));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        String instanceId = "instanceId";
        String tenantId = "tenantId";
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        when(cloudMetadataClient.getTenantEndpoint(instanceId, tenantId)).thenReturn(endpoint);
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        OBTenantEndpoint nullCloudMetadataClientBeanEndpoint = nullCloudMetadataClientBean.getTenantEndpoint(instanceId, tenantId);
        assertNotNull(nullCloudMetadataClientBeanEndpoint);
        assertEquals(endpoint, nullCloudMetadataClientBeanEndpoint);
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        String instanceId = "instanceId";
        when(cloudMetadataClient.getSysTenantUser(instanceId)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        NullCloudMetadataClient nullCloudMetadataClientBean = BeanConfiguration.nullCloudMetadataClient();
        assertNull(nullCloudMetadataClientBean.getSysTenantUser(instanceId));
    }
}