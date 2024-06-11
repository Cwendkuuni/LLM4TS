

import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @InjectMocks
    BeanConfiguration beanConfiguration;

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        when(sensitivePropertyHandler.decrypt(null)).thenReturn(null);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertNotNull(serializer);
    }

}

