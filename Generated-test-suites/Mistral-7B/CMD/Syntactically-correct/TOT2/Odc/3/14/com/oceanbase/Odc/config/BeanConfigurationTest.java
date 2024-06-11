// BeanConfigurationTest.java

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringJUnitConfig(BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    // Test for passwordEncoder method
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String encryptedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encryptedPassword));
    }

    // Test for objectMapper method with valid input
    @Test
    public void testObjectMapperWithValidInput() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        Object object = objectMapper.readValue(jsonString, Object.class);
        assertEquals(new HashMap<String, Object>() {{
            put("name", "John");
            put("age", 30);
        }}, object);
    }

    // Test for objectMapper method with sensitive input
    @Test
    public void testObjectMapperWithSensitiveInput() {
        String sensitiveJsonString = "{\"password\": \"password123\"}";
        try {
            ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
            Object object = objectMapper.readValue(sensitiveJsonString, Object.class);
            fail("Expected UnsupportedOperationException");
        } catch (IOException e) {
            assertInstanceOf(UnsupportedOperationException.class, e.getCause());
        }
    }

    // Test for defaultHttpFirewall method
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    // Test for cloudMetadataClient method with null argument
    @Test
    public void testCloudMetadataClientWithNullArgument() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        Mockito.verify(cloudMetadataClient, Mockito.never()).listInstances();
        Mockito.verify(cloudMetadataClient, Mockito.never()).listTenants(Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.never()).getTenant(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.never()).listDatabaseUsers(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.never()).getTenantEndpoint(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.never()).getSysTenantUser(Mockito.anyString());
    }

    // Test for cloudMetadataClient method with valid argument
    @Test
    public void testCloudMetadataClientWithValidArgument() {
        CloudMetadataClient cloudMetadataClient = Mockito.mock(CloudMetadataClient.class);
        Mockito.when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.setCloudMetadataClient(cloudMetadataClient);
        CloudMetadataClient cloudMetadataClientObj = beanConfiguration.cloudMetadataClient();
        assertSame(cloudMetadataClient, cloudMetadataClientObj);
    }

    // Test for cookieSerializer method
    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }
}