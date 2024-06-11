package com.oceanbase.odc.config;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BeanConfigurationTest {
    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

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

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        cloudMetadataClient.listInstances();
    }
}