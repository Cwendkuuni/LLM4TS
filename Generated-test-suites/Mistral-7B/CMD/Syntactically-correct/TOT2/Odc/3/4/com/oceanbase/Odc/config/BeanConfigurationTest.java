import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);

        assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration beanConfiguration = new BeanConfiguration(new SensitivePropertyHandler());
        ObjectMapper objectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());

        String jsonString = "{\"name\": \"test\", \"password\": \"testPassword\"}";
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        Internationalizable internationalizable = defaultObjectMapper.readValue(jsonString, Internationalizable.class);

        ObjectMapper expectedObjectMapper = beanConfiguration.objectMapper(new SensitivePropertyHandler());
        Internationalizable actualObjectMapper = expectedObjectMapper.readValue(jsonString, Internationalizable.class);

        assertEquals(internationalizable, actualObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        DefaultHttpFirewall defaultDefaultHttpFirewall = new DefaultHttpFirewall();
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();

        String url1 = "http://localhost:8080/test%25";
        String url2 = "http://localhost:8080/test%";
        String url3 = "http://localhost:8080/test";

        Assert.assertTrue(defaultHttpFirewall.isUrlPatternMatch(url1));
        Assert.assertTrue(defaultHttpFirewall.isUrlPatternMatch(url2));
        Assert.assertFalse(strictHttpFirewall.isUrlPatternMatch(url1));
        Assert.assertFalse(strictHttpFirewall.isUrlPatternMatch(url2));
        Assert.assertFalse(strictHttpFirewall.isUrlPatternMatch(url3));
        Assert.assertTrue(defaultDefaultHttpFirewall.isUrlPatternMatch(url1));
        Assert.assertTrue(defaultDefaultHttpFirewall.isUrlPatternMatch(url2));
        Assert.assertTrue(defaultDefaultHttpFirewall.isUrlPatternMatch(url3));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        Mockito.when(cloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new OBTenant());

        OBTenant tenant = cloudMetadataClient.getTenant("instanceId", "tenantId");

        Assert.assertNotNull(tenant);
    }

    @Test
    public void testCloudMetadataClientReturnsNull() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        Assert.assertNull(cloudMetadataClient.getTenant("instanceId", "tenantId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeanConfigurationConstructorWithNullSensitivePropertyHandler() {
        new BeanConfiguration(null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperWithNullSensitivePropertyHandler() {
        BeanConfiguration beanConfiguration = new BeanConfiguration(null);

        Assert.assertThrows(NullPointerException.class, () -> beanConfiguration.objectMapper(null));
    }
}