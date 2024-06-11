import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        // Mock sensitivePropertyHandler
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper mapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = new BeanConfiguration().defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration().cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new BeanConfiguration().cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    public void testNullCloudMetadataClientInit() {
        NullCloudMetadataClient client = new NullCloudMetadataClient();
        client.init();
    }

    // Add more test cases for NullCloudMetadataClient methods with appropriate edge cases and exceptions
    // @Test
    // public void testListInstances() { ... }

    // @Test
    // public void testListTenants() { ... }

    // @Test
    // public void testGetTenant() { ... }

    // @Test
    // public void testListDatabaseUsers() { ... }

    // @Test
    // public void testGetTenantEndpoint() { ... }

    // @Test
    // public void testGetSysTenantUser() { ... }

    // Add more test cases for handling exceptions and edge cases

}