package com.oceanbase.odc.config;
import org.junit.Assert;
import org.junit.Test;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Assert.assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper objectMapper = new ObjectMapper(sensitivePropertyHandler);
        Assert.assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        Assert.assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
        Assert.assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        Assert.assertNotNull(cookieSerializer);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapper_NullSensitivePropertyHandler() {
        SensitivePropertyHandler sensitivePropertyHandler = null;
        ObjectMapper objectMapper = new ObjectMapper(sensitivePropertyHandler);
    }

    @Test
    public void testCloudMetadataClient_DifferentInstance() {
        Assert.assertThrows(ClassCastException.class, () -> {
            CloudMetadataClient cloudMetadataClient = (DifferentInstanceClass) new NullCloudMetadataClient();
        });
    }
}