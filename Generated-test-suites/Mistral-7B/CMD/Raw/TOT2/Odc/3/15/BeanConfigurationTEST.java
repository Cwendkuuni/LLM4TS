package com.oceanbase.odc.config;

import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);

        assertEquals(originalPassword, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(Mockito.mock(SensitivePropertyHandler.class));

        Internationalizable internationalizable = new Internationalizable() {
            @Override
            public String getKey() {
                return "test.key";
            }

            @Override
            public String getMessage() {
                return "test message";
            }
        };

        CustomOutputSerializer customOutputSerializer = Mockito.spy(new CustomOutputSerializer());
        Mockito.doReturn(new I18nOutputSerializer()).when(customOutputSerializer).getSerializerFor(Internationalizable.class);
        JacksonModules jacksonModules = Mockito.spy(new JacksonModules());
        Mockito.doReturn(customOutputSerializer).when(jacksonModules).customOutputHandling();

        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(jacksonModules.sensitiveInputHandling(Mockito.mock(SensitivePropertyHandler.class)::decrypt))
                .registerModule(jacksonModules.customOutputHandling(customOutputSerializer));

        assertEquals(expectedObjectMapper, objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();

        String specialUrl = "http://www.example.com/%25test%25";

        assertTrue(httpFirewall.testUrlContext("GET", specialUrl));
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();

        String sessionId = "session123";
        byte[] serializedCookie = cookieSerializer.serialize(sessionId);
        String deserializedSessionId = (String) cookieSerializer.deserialize(serializedCookie);

        assertEquals(sessionId, deserializedSessionId);
    }
}

//