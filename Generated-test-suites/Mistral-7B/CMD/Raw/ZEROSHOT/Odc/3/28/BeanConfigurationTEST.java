package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

/**
 * Test class for BeanConfiguration.java
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-23 10:45
 */
public class BeanConfigurationTest {

    private AnnotationConfigApplicationContext ctx;

    @Before
    public void setup() {
        ctx = new AnnotationConfigApplicationContext(BeanConfiguration.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
        String plainText = "password";
        String encodedText = passwordEncoder.encode(plainText);
        assertTrue(passwordEncoder.matches(plainText, encodedText));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = ctx.getBean(ObjectMapper.class);
        String json = "{\"name\": \"John Doe\", \"age\": 30}";
        Internationalizable internationalizable = new Internationalizable();
        internationalizable.setName("John Doe");
        internationalizable.setAge(30);
        internationalizable.setMessage("Hello World");
        try {
            String result = objectMapper.writeValueAsString(internationalizable);
            assertEquals(json, result);
        } catch (Exception e) {
            fail("JSON serialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = ctx.getBean(HttpFirewall.class);
        // Test with valid URL
        String validUrl = "http://example.com/path%2Fto%2Fresource";
        assertTrue(defaultHttpFirewall.isUrlSafe(validUrl));

        // Test with malicious URL
        String maliciousUrl = "http://example.com/<script>alert('XSS')";
        assertFalse(defaultHttpFirewall.isUrlSafe(maliciousUrl));
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = ctx.getBean(CloudMetadataClient.class);
        assertSame(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = ctx.getBean(CookieSerializer.class);
        assertEquals("JSESSIONID", cookieSerializer.getName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = ctx.getBean(NullCloudMetadataClient.class);
        assertTrue(nullCloudMetadataClient instanceof NullCloudMetadataClient);

        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        assertNull(nullCloudMetadataClient.listInstances());
        assertNull(nullCloudMetadataClient.listTenants(""));
        assertNull(nullCloudMetadataClient.getTenant("", ""));
        assertNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
        assertNull(nullCloudMetadataClient.getTenantEndpoint("", ""));
        assertNull(nullCloudMetadataClient.getSysTenantUser(""));
    }
}