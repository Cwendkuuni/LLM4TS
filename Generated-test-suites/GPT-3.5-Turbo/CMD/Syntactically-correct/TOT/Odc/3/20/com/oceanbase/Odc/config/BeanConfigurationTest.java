
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BeanConfiguration.class})
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        assertNotNull(beanConfiguration.passwordEncoder());
    }

    @Test
    public void testObjectMapper() {
        assertNotNull(beanConfiguration.objectMapper(Mockito.mock(SensitivePropertyHandler.class)));
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        assertNotNull(beanConfiguration.cookieSerializer());
    }

}
