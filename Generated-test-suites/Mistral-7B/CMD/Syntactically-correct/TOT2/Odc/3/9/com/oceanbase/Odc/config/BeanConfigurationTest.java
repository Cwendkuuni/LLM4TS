import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);

        // Assert the encoder and decoder work correctly
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertEquals(rawPassword, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);

        // Test with null sensitive property handler
        objectMapper = new BeanConfiguration().objectMapper(null);
        assertNotNull(objectMapper);

        // Test with valid sensitive property handler
        String jsonString = "{\"name\": \"test\", \"password\": \"password\"}";
        ObjectMapper jsonMapper = Mockito.spy(objectMapper);
        ObjectMapper deserializedMapper = jsonMapper.readValue(jsonString, ObjectMapper.class);
        assertNotNull(deserializedMapper);

        // Test with sensitive property handler decrypt method
        String decryptedJsonString = jsonMapper.writeValueAsString(deserializedMapper);
        String decryptedPassword = new BeanConfiguration().objectMapper(sensitivePropertyHandler)
                .readValue(decryptedJsonString, String.class);
        assertEquals("password", decryptedPassword);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String allowedUrl = "%{name}/test";
        defaultHttpFirewall.setAllowedMethods(allowedUrl);

        // Test URLs with allowed special characters
        String allowedUrl1 = "%{name}/test%25";
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash(allowedUrl1));

        String allowedUrl2 = "%{name}/test%22";
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash(allowedUrl2));

        String allowedUrl3 = "%{name}/test%27";
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash(allowedUrl3));

        String allowedUrl4 = "%{name}/test%60";
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash(allowedUrl4));

        // Test URLs with not allowed special characters
        String disallowedUrl1 = "%{name}/test%61";
        assertFalse(defaultHttpFirewall.isAllowUrlEncodedSlash(disallowedUrl1));

        String disallowedUrl2 = "%{name}/test%62";
        assertFalse(defaultHttpFirewall.isAllowUrlEncodedSlash(disallowedUrl2));

        String disallowedUrl3 = "%{name}/test%63";
        assertFalse(defaultHttpFirewall.isAllowUrlEncodedSlash(disallowedUrl3));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = configuration.cloudMetadataClient();

        // Test instanceId is null
        assertNull(cloudMetadataClient.listInstances());

        // Test tenantId is null
        assertNull(cloudMetadataClient.listTenants(null));

        // Test getTenant with null values
        assertNull(cloudMetadataClient.getTenant(null, "tenantId"));

        // Test getTenantEndpoint with null values
        OBTenantEndpoint tenantEndpoint = cloudMetadataClient.getTenantEndpoint(null, "tenantId");
        assertNull(tenantEndpoint);

        // Test listDatabaseUsers with null values
        assertNull(cloudMetadataClient.listDatabaseUsers(null, "tenantId"));

        // Test getSysTenantUser with null values
        assertNull(cloudMetadataClient.getSysTenantUser(null));

        // Test supportsCloudMetadata returns false
        assertFalse(cloudMetadataClient.supportsCloudMetadata());

        // Test supportsTenantInstance returns false
        assertFalse(cloudMetadataClient.supportsTenantInstance());

        // Test needsOBTenantName returns false
        assertFalse(cloudMetadataClient.needsOBTenantName());

        // Test needsSysTenantUser returns false
        assertFalse(cloudMetadataClient.needsSysTenantUser());

        // Test includeClusterNameForJdbcUsername returns true
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());

        // Test oceanbaseAccessMode returns OceanBaseAccessMode.DIRECT
        OceanBaseAccessMode oceanbaseAccessMode = cloudMetadataClient.oceanbaseAccessMode();
        assertEquals(OceanBaseAccessMode.DIRECT, oceanbaseAccessMode);

    }

}