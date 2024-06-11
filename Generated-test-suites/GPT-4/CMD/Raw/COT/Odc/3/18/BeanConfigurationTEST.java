
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import static org.mockito.Mockito.*;

public class BeanConfigurationTest {

    @Mock
    SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder result = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(result);
    }

    @Test
    public void testObjectMapper() {
        Assert.assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall result = beanConfiguration.defaultHttpFirewall();
        Assert.assertNotNull(result);
    }

    @Test
    public void testCloudMetadataClient() {
        Assert.assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer result = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientMethods() {
        BeanConfiguration.NullCloudMetadataClient client = new BeanConfiguration.NullCloudMetadataClient();
        client.listInstances();
        client.listTenants("");
        client.getTenant("", "");
        client.listDatabaseUsers("", "");
        client.getSysTenantUser("");
    }
}
