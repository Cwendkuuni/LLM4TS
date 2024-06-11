import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@Import(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        String decodedPassword = passwordEncoder.decode(encodedPassword);

        assertEquals(rawPassword, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);

        String input = "{\"name\":\"John Doe\",\"age\":30,\"address\":\"123 Main St\"}";
        String expectedOutput = "{\"name\":\"John Doe\",\"age\":30,\"address\":\"123 Main St\"}";

        String output = objectMapper.writeValueAsString(new Object());
        ReflectionTestUtils.setFieldValue(objectMapper, "serializer", new I18nOutputSerializer());
        String mappedOutput = objectMapper.writeValueAsString(new Object());

        assertEquals(expectedOutput, output);
        assertEquals(expectedOutput, mappedOutput);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        String urlWithSlash = "http://example.com/bad%25example";
        String urlWithoutSlash = "http://example.com/bad%25example/";

        boolean isAllowed = httpFirewall.allowRequest(urlWithSlash);
        boolean isAllowedWithoutSlash = httpFirewall.allowRequest(urlWithoutSlash);

        assertTrue(isAllowedWithSlash);
        assertFalse(isAllowedWithoutSlash);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        List<OBInstance> instances = cloudMetadataClient.listInstances();
        List<OBTenant> tenants = cloudMetadataClient.listTenants("instanceId");
        OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
        List<OBDatabaseUser> databaseUsers = cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        OBTenantEndpoint tenantEndpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        OBDatabaseUser sysTenantUser = cloudMetadataClient.getSysTenantUser("instanceId");

        assertNotNull(instances);
        assertNotNull(tenants);
        assertNotNull(tenant);
        assertNotNull(databaseUsers);
        assertNotNull(tenantEndpoint);
        assertNotNull(sysTenantUser);
    }
}