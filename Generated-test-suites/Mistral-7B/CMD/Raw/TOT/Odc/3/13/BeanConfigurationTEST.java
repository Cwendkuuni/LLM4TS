import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerStub;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

import static org.mockito.Mockito.*;

@SpringJUnitConfig(BeanConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        Assertions.assertNotEquals(originalPassword, encodedPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        Assertions.assertEquals(originalPassword, decodedPassword);
    }

    @Test
    void objectMapperTest() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String jsonString = "{\"name\": \"Alice\", \"age\": 30, \"address\": {\"city\": \"Shanghai\"}}";
        OBTenant tenant = new OBTenant();
        tenant.setName("tenant1");

        ObjectMapper stubObjectMapper = mock(ObjectMapper.class);
        when(stubObjectMapper.readValue(jsonString, OBTenant.class)).thenReturn(tenant);

        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        OBTenant readTenant = actualObjectMapper.readValue(jsonString, OBTenant.class);
        Assertions.assertNotNull(readTenant);
        Assertions.assertEquals(tenant.getName(), readTenant.getName());

        verify(stubObjectMapper).readValue(jsonString, OBTenant.class);
    }

    @Test
    void defaultHttpFirewallTest() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        // Add your test cases for URL filtering and validation here
    }

    @Test
    void cloudMetadataClientTest() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        // Add your test cases for methods in CloudMetadataClient here
        verify(cloudMetadataClient, atLeastOnce()).getTenantEndpoint(anyString(), anyString());
    }
}