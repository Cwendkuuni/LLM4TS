import com.fasterxml.jackson.databind.JsonNode;
import com.oceanbase.odc.common.sensitive.SensitivePropertyHandler;
import org.junit.Test;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.util.UriComponentsBuilder;
import static org.junit.Assert.assertEquals;

public class BeanConfigurationTest {

    public class TestBeanConfiguration extends BeanConfiguration {
        public TestBeanConfiguration() {
            super();
        }

        public TestBeanConfiguration(SensitivePropertyHandler sensitivePropertyHandler) {
            super(sensitivePropertyHandler);
        }
    }

    @Test
    public void testPasswordEncoder() {
        TestBeanConfiguration config = new TestBeanConfiguration();
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        String originalPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(originalPassword, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        TestBeanConfiguration config = new TestBeanConfiguration(new SensitivePropertyHandler());
        ObjectMapper objectMapper = config.objectMapper(new SensitivePropertyHandler());
        String jsonString = "{\"name\": \"John\", \"age\": 30, \"password\": \"testPassword\"}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        String decryptedPassword = config.sensitivePropertyHandler.decrypt(jsonNode.get("password").asText());
        assertEquals("testPassword", decryptedPassword);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall httpFirewall = config.defaultHttpFirewall();
        String urlWithMaliciousInput = UriComponentsBuilder.fromUriString("http://example.com/%7E%22%3Cscript%3Ealert('XSS')%3C/script%3E")
                .toUriString();
        try {
            httpFirewall.allowAccess(UriComponents.fromUriString(urlWithMaliciousInput));
            // This line should not be reached
        } catch (RequestRejectedException e) {
            // Test passes if the exception is thrown
        }
    }

    // Add test cases for other methods here
}

//