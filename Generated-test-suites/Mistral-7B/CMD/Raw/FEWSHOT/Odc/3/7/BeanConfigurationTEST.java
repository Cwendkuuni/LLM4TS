package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;

@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        assertTrue(encodedPassword.length() > password.length());
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = ctx.getBean(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = ctx.getBean(ObjectMapper.class);
        String sensitiveData = "sensitive data";
        String encryptedData = sensitivePropertyHandler.encrypt(sensitiveData);
        assertNotNull(objectMapper);
        // Test serialization with sensitive data
        String json = objectMapper.writeValueAsString(new Object() {
            @NotBlank
            private String sensitiveField = encryptedData;

            public String getSensitiveField() {
                return sensitiveField;
            }
        });
        // Test deserialization with sensitive data
        Object deserializedObject = objectMapper.readValue(json, Object.class);
        String deserializedSensitiveField = deserializedObject.getClass().getDeclaredField("sensitiveField")
                .get(deserializedObject).toString();
        assertTrue(deserializedSensitiveField.equals(encryptedData));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = ctx.getBean(HttpFirewall.class);
        String urlWithSlash = "/test%25";
        assertTrue(defaultHttpFirewall.allowUrlEncoded(urlWithSlash));
    }

    @Test
    public void testCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = ctx.getBean(NullCloudMetadataClient.class);
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = ctx.getBean(CookieSerializer.class);
        assertNotNull(cookieSerializer);
        String jsessionId = "JSESSIONID";
        assertEquals(jsessionId, cookieSerializer.getName());
    }
}

@SuppressWarnings("unused")
class CustomTest {

    @Test
    public void testPass() {
        // This test case is intentionally empty and only serves as a placeholder
    }
}