package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for {@link FromStringDeserializer} and its subclass {@link Std}.
 */
public class FromStringDeserializerTest {

    /**
     * Test deserialization of a valid String representation.
     */
    @Test
    public void testDeserializeValidString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = findDeserializer(URI.class);
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = findDeserializer(TimeZone.class);
        FromStringDeserializer<Currency> currencyDeserializer = findDeserializer(Currency.class);

        // Test deserialization of valid URL string
        URL url = mapper.readValue(mapper.writeValueAsString(new URL("http://example.com")),
                urlDeserializer.getType(URL.class));
        assertEquals("http://example.com", url.toString());

        // Test deserialization of valid URI string
        URI uri = mapper.readValue(mapper.writeValueAsString(new URI("http://example.com")),
                uriDeserializer.getType(URI.class));
        assertEquals("http", uri.getScheme());
        assertEquals("example.com", uri.getHost());

        // Test deserialization of valid Locale string
        Locale locale = mapper.readValue(mapper.writeValueAsString(Locale.ENGLISH),
                localeDeserializer.getType(Locale.class));
        assertEquals("en_US", locale.toString());

        // Test deserialization of valid TimeZone string
        TimeZone timeZone = mapper.readValue(mapper.writeValueAsString("UTC"),
                timeZoneDeserializer.getType(TimeZone.class));
        assertEquals("UTC", timeZone.getID());

        // Test deserialization of valid Currency string
        Currency currency = mapper.readValue(mapper.writeValueAsString("USD"),
                currencyDeserializer.getType(Currency.class));
        assertEquals("USD", currency.getCurrencyCode());
    }

    /**
     * Test deserialization of an empty String.
     */
    @Test
    public void testDeserializeEmptyString() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = findDeserializer(URI.class);
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = findDeserializer(TimeZone.class);
        FromStringDeserializer<Currency> currencyDeserializer = findDeserializer(Currency.class);

        // Test deserialization of empty URL string
        try {
            mapper.readValue(mapper.writeValueAsString(""), urlDeserializer.getType(URL.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of empty URI string
        try {
            mapper.readValue(mapper.writeValueAsString(""), uriDeserializer.getType(URI.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of empty Locale string
        try {
            mapper.readValue(mapper.writeValueAsString(""), localeDeserializer.getType(Locale.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of empty TimeZone string
        try {
            mapper.readValue(mapper.writeValueAsString(""), timeZoneDeserializer.getType(TimeZone.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of empty Currency string
        try {
            mapper.readValue(mapper.writeValueAsString(""), currencyDeserializer.getType(Currency.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }
    }

    /**
     * Test deserialization of an invalid String representation for various types.
     */
    @Test
    public void testDeserializeInvalidStrings() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = findDeserializer(URI.class);
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = findDeserializer(TimeZone.class);
        FromStringDeserializer<Currency> currencyDeserializer = findDeserializer(Currency.class);

        // Test deserialization of invalid URL string
        try {
            mapper.readValue(mapper.writeValueAsString("invalidURL"), urlDeserializer.getType(URL.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to parse/deserialze value of type java.net.URL to java.net.URL", e.getMessage());
        }

        // Test deserialization of invalid URI string
        try {
            mapper.readValue(mapper.writeValueAsString("invalidURI"), uriDeserializer.getType(URI.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Failed to parse/deserialze value of type java.net.URI to java.net.URI", e.getMessage());
        }

        // Test deserialization of invalid Locale string
        try {
            mapper.readValue(mapper.writeValueAsString("en_US_invalid"), localeDeserialializer.getType(Locale.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Invalid format: \"en_US_invalid\": invalid country code: \"invalid\"", e.getMessage());
        }

        // Test deserialization of invalid TimeZone string
        try {
            mapper.readValue(mapper.writeValueAsString("invalidTimeZone"), timeZoneDeserializer.getType(TimeZone.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Invalid time zone ID: 'invalidTimeZone'", e.getMessage());
        }

        // Test deserialization of invalid Currency string
        try {
            mapper.readValue(mapper.writeValueAsString("invalidCurrency"), currencyDeserializer.getType(Currency.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Invalid currency code: 'invalidCurrency'", e.getMessage());
        }
    }

    /**
     * Test deserialization of null value for various types.
     */
    @Test
    public void testDeserializeNull() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = findDeserializer(URI.class);
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = findDeserializer(TimeZone.class);
        FromStringDeserializer<Currency> currencyDeserializer = findDeserializer(Currency.class);

        // Test deserialization of null URL string
        try {
            mapper.readValue(mapper.writeValueAsString(null), urlDeserializer.getType(URL.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of null URI string
        try {
            mapper.readValue(mapper.writeValueAsString(null), uriDeserializer.getType(URI.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of null Locale string
        try {
            mapper.readValue(mapper.writeValueAsString(null), localeDeserializer.getType(Locale.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of null TimeZone string
        try {
            mapper.readValue(mapper.writeValueAsString(null), timeZoneDeserializer.getType(TimeZone.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }

        // Test deserialization of null Currency string
        try {
            mapper.readValue(mapper.writeValueAsString(null), currencyDeserializer.getType(Currency.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Not a valid textual representation", e.getMessage());
        }
    }

    /**
     * Test deserialization of an invalid JSON representation.
     */
    @Test
    public void testDeserializeInvalidJson() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);
        FromStringDeserializer<URI> uriDeserializer = findDeserializer(URI.class);
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);
        FromStringDeserializer<TimeZone> timeZoneDeserializer = findDeserializer(TimeZone.class);
        FromStringDeserializer<Currency> currencyDeserializer = findDeserializer(Currency.class);

        // Test deserialization of invalid JSON for URL type
        String json = "{\"invalid\": \"JSON\"}";
        try {
            mapper.readValue(mapper.writeValueAsString(json), urlDeserializer.getType(URL.class));
            fail("Expected JsonMappingException or JsonProcessingException");
        } catch (JsonMappingException | JsonProcessingException e) {
            // Expected
        }

        // Test deserialization of invalid JSON for URI type
        json = "{\"invalid\": \"JSON\", \"scheme\": \"http\"}";
        try {
            mapper.readValue(mapper.writeValueAsString(json), uriDeserializer.getType(URI.class));
            fail("Expected JsonMappingException or JsonProcessingException");
        } catch (JsonMappingException | JsonProcessingException e) {
            // Expected
        }

        // Test deserialization of invalid JSON for Locale type
        json = "{\"locale\": \"en_US\", \"invalid\": \"JSON\"}";
        try {
            mapper.readValue(mapper.writeValueAsString(json), localeDeserializer.getType(Locale.class));
            fail("Expected JsonMappingException or JsonProcessingException");
        } catch (JsonMappingException | JsonProcessingException e) {
            // Expected
        }

        // Test deserialization of invalid JSON for TimeZone type
        json = "{\"timeZone\": \"invalidTimeZone\", \"gmtOffset\": 0, \"dstSavings\": 0}";
        try {
            mapper.readValue(mapper.writeValueAsString(json), timeZoneDeserializer.getType(TimeZone.class));
            fail("Expected JsonMappingException or JsonProcessingException");
        } catch (JsonMappingException | JsonProcessingException e) {
            // Expected
        }

        // Test deserialization of invalid JSON for Currency type
        json = "{\"currency\": \"invalidCurrency\", \"currencyCode\": \"USD\", \"symbol\": \"$\"}";
        try {
            mapper.readValue(mapper.writeValueAsString(json), currencyDeserializer.getType(Currency.class));
            fail("Expected JsonMappingException or JsonProcessingException");
        } catch (JsonMappingException | JsonProcessingException e) {
            // Expected
        }
    }

    /**
     * Test deserialization of an array of String representations.
     */
    @Test
    public void testDeserializeArray() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<URL> urlDeserializer = findDeserializer(URL.class);

        // Test deserialization of an array of URL strings
        String json = "[\"http://example1.com\", \"http://example2.com\"]";
        JavaType javaType = mapper.getTypeFactory().constructArrayType(URL.class);
        URL[] urls = mapper.readValue(mapper.writeValueAsString(json), javaType);
        assertEquals(2, urls.length);
        assertEquals("http://example1.com", urls[0].toString());
        assertEquals("http://example2.com", urls[1].toString());
    }

    /**
     * Test deserialization of an embedded Object.
     */
    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);

        // Test deserialization of an embedded Locale object
        String json = "{\"locale\": \"fr_FR\"}";
        JsonNode node = mapper.readTree(json);
        Object obj = node.get("locale");
        Locale locale = mapper.readValue(mapper.writeValueAsString(obj.toString()), localeDeserializer.getType(Locale.class));
        assertEquals("fr_FR", locale.toString());
    }

    /**
     * Test deserialization of a null embedded Object.
     */
    @Test
    public void testDeserializeNullEmbeddedObject() throws IOException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        FromStringDeserializer<Locale> localeDeserializer = findDeserializer(Locale.class);

        // Test deserialization of a null embedded Locale object
        String json = "{\"locale\": null}";
        JsonNode node = mapper.readTree(json);
        Object obj = node.get("locale");
        try {
            mapper.readValue(mapper.writeValueAsString(obj.toString()), localeDeserializer.getType(Locale.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Helper method to find the deserializer for a given class.
     */
    private static FromStringDeserializer findDeserializer(Class<?> rawType) {
        return (FromStringDeserializer<?>) Std.findDeserializer(rawType);
    }
}