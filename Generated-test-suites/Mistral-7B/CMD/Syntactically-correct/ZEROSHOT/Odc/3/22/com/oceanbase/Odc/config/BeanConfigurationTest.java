package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.common.json.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = BeanConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClientInit() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.init();
    }

    @Test
    public void testNullCloudMetadataClientSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBInstance> obInstances = nullCloudMetadataClient.listInstances();
        assertNotNull(obInstances);
        assertFalse(obInstances.isEmpty());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBTenant> obTenants = nullCloudMetadataClient.listTenants("instanceId");
        assertNotNull(obTenants);
        assertFalse(obTenants.isEmpty());
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNotNull(tenant);
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBDatabaseUser> obDatabaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertNotNull(obDatabaseUsers);
        assertFalse(obDatabaseUsers.isEmpty());
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(tenantEndpoint);
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBDatabaseUser sysTenantUser = nullCloudMetadataClient.getSysTenantUser("instanceId");
        assertNull(sysTenantUser);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientUnsupportedOperation() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        nullCloudMetadataClient.includesClusterNameForJdbcUsername();
    }

    @Test
    public void testObjectMapperCustomOutputSerializer() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        CustomOutputSerializer customOutputSerializer = (CustomOutputSerializer) objectMapper.getSerializerProvider().findValueSerializer(Internationalizable.class);
        I18nOutputSerializer i18nOutputSerializer = (I18nOutputSerializer) customOutputSerializer.getSerializer();
        assertNotNull(i18nOutputSerializer);
    }

}