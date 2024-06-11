package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.ArrayList;
import java.util.List;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;
    private BCryptPasswordEncoder passwordEncoder;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private DefaultHttpFirewall defaultHttpFirewall;
    private NullCloudMetadataClient cloudMetadataClient;
    private DefaultCookieSerializer cookieSerializer;

    @Before
    public void setup() {
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        objectMapper = mock(ObjectMapper.class);
        defaultHttpFirewall = new DefaultHttpFirewall();
        cloudMetadataClient = mock(CloudMetadataClient.class);
        cookieSerializer = mock(CookieSerializer.class);
        beanConfiguration = new BeanConfiguration() {
            {
                this.passwordEncoder = passwordEncoder;
                this.sensitivePropertyHandler = sensitivePropertyHandler;
                this.objectMapper = objectMapper;
                this.defaultHttpFirewall = defaultHttpFirewall;
                this.cloudMetadataClient = cloudMetadataClient;
                this.cookieSerializer = cookieSerializer;
            }
        };
    }

    @Test
    public void testPasswordEncoderBean() {
        assertEquals(BCryptPasswordEncoder.class, beanConfiguration.passwordEncoder().getClass());
    }

    @Test
    public void testObjectMapperBean() {
        verify(objectMapper, times(2)).registerModule(any(JacksonModules.class));
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
        I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
        customOutputSerializer.addSerializer(Internationalizable.class, i18nOutputSerializer);
        verify(objectMapper).registerModule(eq(JacksonModules.customOutputHandling(customOutputSerializer)));
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        assertEquals(DefaultHttpFirewall.class, beanConfiguration.defaultHttpFirewall().getClass());
        assertTrue(defaultHttpFirewall.allowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClientBean() {
        assertSame(cloudMetadataClient, beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializerBean() {
        assertEquals(CookieSerializer.class, beanConfiguration.cookieSerializer().getClass());
        assertEquals("JSESSIONID", beanConfiguration.cookieSerializer().getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertSame(NullCloudMetadataClient.class, beanConfiguration.cloudMetadataClient().getClass());
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        assertFalse(beanConfiguration.cloudMetadataClient().supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientSupportsTenantInstance() {
        assertFalse(beanConfiguration.cloudMetadataClient().supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClientNeedsOBTenantName() {
        assertFalse(beanConfiguration.cloudMetadataClient().needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClientNeedsSysTenantUser() {
        assertFalse(beanConfiguration.cloudMetadataClient().needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClientIncludeClusterNameForJdbcUsername() {
        assertTrue(beanConfiguration.cloudMetadataClient().includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClientOceanbaseAccessMode() {
        assertEquals(OceanBaseAccessMode.DIRECT, beanConfiguration.cloudMetadataClient().oceanbaseAccessMode());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListInstances() {
        beanConfiguration.cloudMetadataClient().listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListTenants() {
        beanConfiguration.cloudMetadataClient().listTenants("instanceId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        beanConfiguration.cloudMetadataClient().getTenant("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientListDatabaseUsers() {
        beanConfiguration.cloudMetadataClient().listDatabaseUsers("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        beanConfiguration.cloudMetadataClient().getTenantEndpoint("instanceId", "tenantId");
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetSysTenantUser() {
        beanConfiguration.cloudMetadataClient().getSysTenantUser("instanceId");
    }
}