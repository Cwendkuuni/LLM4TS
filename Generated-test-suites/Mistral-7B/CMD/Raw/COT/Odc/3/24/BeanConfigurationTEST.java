package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

/**
 * Test for BeanConfiguration class
 *
 * @author yizhou.xw
 */
public class BeanConfigurationTest {

    private AnnotationConfigApplicationContext context;
    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;
    private ObjectMapper objectMapper;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        beanConfiguration = context.getBean(BeanConfiguration.class);
        sensitivePropertyHandler = context.getBean(SensitivePropertyHandler.class);
        objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        cloudMetadataClient = context.getBean(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        String json = "{\"name\":\"testName\",\"age\":18}";
        ObjectMapper mapper = objectMapper;
        Internationalizable internationalizable = mapper.readValue(json, Internationalizable.class);
        assertEquals("testName", internationalizable.getName());
        assertEquals(18, internationalizable.getAge());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        String url = "http://test.com/%25test.com";
        assertTrue(firewall.isUrlSafe(url));
    }

    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClient_getTenant() {
        cloudMetadataClient.getTenant("testInstanceId", "testTenantId");
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClient_listInstances() {
        assertNull(nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClient_listTenants() {
        String instanceId = "testInstanceId";
        assertNull(nullCloudMetadataClient.listTenants(instanceId));
    }

    @Test
    public void testNullCloudMetadataClient_getTenantEndpoint() {
        String instanceId = "testInstanceId";
        String tenantId = "testTenantId";
        OBTenantEndpoint endpoint = beanConfiguration.cloudMetadataClient().getTenantEndpoint(instanceId, tenantId);
        assertNotNull(endpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testNullCloudMetadataClient_getSysTenantUser() {
        String instanceId = "testInstanceId";
        OBDatabaseUser user = beanConfiguration.cloudMetadataClient().getSysTenantUser(instanceId);
        assertNull(user);
    }

    @Test
    public void testListDatabaseUsers() {
        String instanceId = "testInstanceId";
        String tenantId = "testTenantId";
        List<OBDatabaseUser> users = beanConfiguration.cloudMetadataClient().listDatabaseUsers(instanceId, tenantId);
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

}