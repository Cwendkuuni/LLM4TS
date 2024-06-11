
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private BeanConfiguration beanConfiguration;

    @BeforeEach
    public void setup() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
    }

    @Test
    @DisplayName("Test that password encoder returns accurate not null")
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    @DisplayName("Test that object mapper returns accurate not null")
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    @DisplayName("Test that default http firewall returns accurate not null")
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    @DisplayName("Test that cloud metadata client returns accurate not null")
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    @DisplayName("Test that cookie serializer returns accurate not null")
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }
}
