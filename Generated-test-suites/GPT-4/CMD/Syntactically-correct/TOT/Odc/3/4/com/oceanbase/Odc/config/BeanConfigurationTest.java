package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.session.web.http.DefaultCookieSerializer;

public class BeanConfigurationTest {
      private NullCloudMetadataClient client;

     @Before
     public void setup() {
         client = (new BeanConfiguration()).new NullCloudMetadataClient();
     }

    @Test
    public void testPasswordHash() {
        assertNotNull(new BeanConfiguration().passwordEncoder());
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test
    public void testDefaultHttpFirewall() {
        assertNotNull(new BeanConfiguration().defaultHttpFirewall().getFirewalledRequest());
    }

    @Test
    public void testNullCloudMetadataClient() {
        assertFalse(client.supportsCloudMetadata());
        assertFalse(client.supportsTenantInstance());
        assertFalse(client.needsOBTenantName());
        assertTrue(client.includeClusterNameForJdbcUsername());
        assertNotNull(client.oceanbaseAccessMode());
    }

    @Test(expected=UnsupportedException.class)
    public void testGetTenantException() {
        client.getTenant("any_instance","any_tenant");
    }

    @Test
    public void testGetTenantEndpoint() {
        assertNotNull(client.getTenantEndpoint("any_instance","any_tenant").getAccessMode());
    }
}