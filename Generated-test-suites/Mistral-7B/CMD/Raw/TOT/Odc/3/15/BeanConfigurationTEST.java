import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Autowired
    private NullCloudMetadataClient cloudMetadataClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CookieSerializer cookieSerializer;

    @Autowired
    private DefaultHttpFirewall defaultHttpFirewall;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        String plainTextPassword = "password123";
        String encodedPassword = passwordEncoder.encode(plainTextPassword);
        assertNotNull(encodedPassword);

        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(plainTextPassword, decodedPassword);

        assertNull(passwordEncoder.decode("wrongPassword"));
    }

    @Test
    public void testObjectMapper() {
        String jsonString = "{\"message\": \"Hello world!\"}";
        I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();

        I18nOutputSerializer i18nOutputSerializerFromJson = objectMapper.readValue(jsonString, I18nOutputSerializer.class);
        assertEquals(i18nOutputSerializer.getMessage(), i18nOutputSerializerFromJson.getMessage());
    }

    @Test
    public void testDefaultHttpFirewall() {
        String allowedUrl = "http://example.com/allowed%2Fpath";
        String disallowedUrl = "http://example.com/disallowed%2Fpath";

        assert !defaultHttpFirewall.isSimpleRequestForged(allowedUrl);
        assert defaultHttpFirewall.isSimpleRequestForged(disallowedUrl);
    }

    @Test
    public void testCloudMetadataClient() {
        assertNull(cloudMetadataClient.getTenant(null, null));
        assertNull(cloudMetadataClient.listInstances());
        assertNull(cloudMetadataClient.getTenantEndpoint(null, null));
    }

    @Test
    public void testBeanConfiguration() {
        List<OBInstance> instances = new ArrayList<>();
        instances.add(new OBInstance());

        List<OBTenant> tenants = new ArrayList<>();
        tenants.add(new OBTenant());

        List<OBDatabaseUser> dbUsers = new ArrayList<>();
        dbUsers.add(new OBDatabaseUser());

        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);

        OBTenant tenant = new OBTenant();
        tenant.setId("tenantId");
        tenant.setName("tenantName");
        tenant.setEndpoint(endpoint);

        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        sysTenantUser.setUsername("sysUser");
        sysTenantUser.setPassword("sysPassword");

        assertEquals(instances, beanConfiguration.listInstances());
        assertEquals(tenants, beanConfiguration.listTenants(null));
        assertEquals(tenant, beanConfiguration.getTenant(null, "tenantId"));
        assertEquals(dbUsers, beanConfiguration.listDatabaseUsers("instanceId", "tenantId"));
        assertEquals(sysTenantUser, beanConfiguration.getSysTenantUser("instanceId"));
    }
}