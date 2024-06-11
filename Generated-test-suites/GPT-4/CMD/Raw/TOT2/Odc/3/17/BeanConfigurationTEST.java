import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        Object actualPasswordEncoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(actualPasswordEncoder);
        Assert.assertTrue(actualPasswordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(null);
        Assert.assertNotNull(actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        Object actualDefaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(actualDefaultHttpFirewall);
        Assert.assertTrue(actualDefaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        Object actualCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(actualCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        Object actualCookieSerializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(actualCookieSerializer);
        Assert.assertTrue(actualCookieSerializer instanceof DefaultCookieSerializer);
    }
  
    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        beanConfiguration.cloudMetadataClient().getTenant(null, null);
    }
}