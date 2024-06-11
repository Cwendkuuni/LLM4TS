import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        // Given
        BeanConfiguration beanConfiguration = new BeanConfiguration();

        // When
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

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

    @Test
    public void testNullCloudMetadataClient() {
        // Given
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();

        // When
        boolean isSupportsCloudMetadata = nullCloudMetadataClient.supportsCloudMetadata();

        // Then
        assertFalse(isSupportsCloudMetadata);
    }
}