import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        BCryptPasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());

        // Then
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();

        // Then
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        // Then
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();

        // Then
        assertNotNull(cookieSerializer);
    }
}