import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Assert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

public class BeanConfigurationTest {

    private final BeanConfiguration config = new BeanConfiguration();

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder encoder = config.passwordEncoder();
        String original = "password";
        String encoded = encoder.encode(original);

        assertTrue(encoded != null);
        assertTrue(encoder.matches(original, encoded));
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void cloudMetadataClientTest() {
        CloudMetadataClient client = config.cloudMetadataClient();
        assertNotNull(client);
        assertFalse(client.supportsCloudMetadata());
    }

    // Additional tests for other methods in the class will go here...
    // For each method tests for typical use cases, edge cases, and error scenarios should be written. 

}