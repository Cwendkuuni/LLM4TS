import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;

public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    private BeanConfiguration testClass = new BeanConfiguration();

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = testClass.passwordEncoder();
        assertNotEquals(null, passwordEncoder);
    }

    @Test
    public void objectMapperTest() {
        ObjectMapper objectMapper = testClass.objectMapper(sensitivePropertyHandler);
        assertNotEquals(null, objectMapper);
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall defaultHttpFirewall = testClass.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall.isAllowUrlEncodedSlash());
    }

    @Test
    public void cloudMetadataClientTest() {
        CloudMetadataClient cloudMetadataClient = testClass.cloudMetadataClient();
        assertFalse(cloudMetadataClient.supportsCloudMetadata());
    }

    // More tests could be added in a similar manner to test the remaining bean configurations 
}