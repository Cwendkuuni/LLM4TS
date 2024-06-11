package com.oceanbase.odc.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        String originalPassword = "password123";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(originalPassword, decodedPassword);

        // Edge case: Testing the encoding and decoding of different characters, special characters, and long passwords.
        String passwordWithSpecialCharacters = "password#123!";
        String encodedPasswordWithSpecialCharacters = passwordEncoder.encode(passwordWithSpecialCharacters);
        String decodedPasswordWithSpecialCharacters = passwordEncoder.decode(encodedPasswordWithSpecialCharacters);
        assertEquals(passwordWithSpecialCharacters, decodedPasswordWithSpecialCharacters);

        // Exception handling: Testing the encoding and decoding of null passwords and empty strings.
        String nullPassword = null;
        String emptyPassword = "";
        assertThrows(NullPointerException.class, () -> passwordEncoder.encode(nullPassword));
        assertEquals("", passwordEncoder.decode(""));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mockObjectMapper = Mockito.mock(ObjectMapper.class);
        SensitivePropertyHandler mockSensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(mockSensitivePropertyHandler);
        String jsonString = "{\"name\": \"John\", \"age\": 30}";
        Object object = objectMapper.readValue(jsonString, Object.class);
        assertEquals(jsonString, objectMapper.writerWithView(View.DEFAULT_VIEW).writeValueAsString(object));

        // Edge case: Testing the serialization and deserialization of complex objects with nested structures.
        Object complexObject = new Object() {{
            String name = "John Doe";
            String address = new Address() {{
                String city = "New York";
                String country = "USA";
            }};
        }};
        String complexJsonString = "{\"name\": \"John Doe\", \"address\": {\"city\": \"New York\", \"country\": \"USA\"}}";
        Object complexObjectDeserialized = objectMapper.readValue(complexJsonString, Object.class);
        assertEquals(complexObject, complexObjectDeserialized);

        // Exception handling: Testing the serialization and deserialization of null objects and empty collections.
        Object nullObject = null;
        List<String> emptyList = Collections.emptyList();
        assertThrows(NullPointerException.class, () -> objectMapper.writeValueAsString(nullObject));
        assertEquals("[]", objectMapper.writeValueAsString(emptyList));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new BeanConfiguration().defaultHttpFirewall();
        String urlWithSlash = "http://example.com/%25";
        String urlWithoutSlash = "http://example.com";
        assertTrue(defaultHttpFirewall.isUrlSafe(urlWithoutSlash));
        assertFalse(defaultHttpFirewall.isUrlSafe(urlWithSlash));

        // Edge case: Testing the validation of special characters, long URLs, and URLs with query parameters.
        String longUrl = "http://example.com/longURL/with%25query%25parameter?name=John&age=30";
        String urlWithQueryParameters = "http://example.com/urlWithQueryParameters%3Fname%3DJohn%26age%3D30";
        assertTrue(defaultHttpFirewall.isUrlSafe(urlWithoutSlash));
        assertTrue(defaultHttpFirewall.isUrlSafe(longUrl));
        assertFalse(defaultHttpFirewall.isUrlSafe(urlWithQueryParameters));

        // Exception handling: Testing the validation of null URLs and empty strings.
        String nullUrl = null;
        String emptyUrl = "";
        assertThrows(NullPointerException.class, () -> defaultHttpFirewall.isUrlSafe(nullUrl));
        assertTrue(defaultHttpFirewall.isUrlSafe(emptyUrl));
    }
}