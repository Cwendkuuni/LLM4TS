package com.example.test; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDeserializeValidStrings() throws IOException {
        // Test deserialization of valid strings for various types
        testDeserialize("file.txt", File.class);
        testDeserialize("http://example.com", URL.class);
        testDeserialize("http://example.com/path", URI.class);
        testDeserialize("java.util.Locale[en_US]", Locale.class);
        testDeserialize("UTF-8", Charset.class);
        testDeserialize("America/New_York", TimeZone.class);

        // Custom tests for specific methods
        testDeserialize("2023-02-28", Date.class);
        testDeserialize("EUR", Currency.class);
        testDeserialize("en_US", Locale.class);
        testDeserialize("UTF-8", Charset.class);
        testDeserialize("America/New_York", TimeZone.class);
        testDeserialize("192.168.1.1:8080", InetSocketAddress.class);
        testDeserialize("[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:80", InetSocketAddress.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyStrings() throws IOException {
        // Test deserialization of empty strings for various types
        testDeserialize("", File.class);
        testDeserialize("", URL.class);
        testDeserialize("", URI.class);
        testDeserialize("", Locale.class);
        testDeserialize("", Charset.class);
        testDeserialize("", TimeZone.class);
        testDeserialize("", InetAddress.class);
        testDeserialize("", InetSocketAddress.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidStrings() throws IOException {
        // Test deserialization of invalid strings for various types
        testDeserialize("invalidFile", File.class);
        testDeserialize("invalidUrlScheme://example.com", URL.class);
        testDeserialize("invalidUriScheme://example.com", URI.class);
        testDeserialize("invalidLocale[en]", Locale.class);
        testDeserialize("invalidCharset", Charset.class);
        testDeserialize("invalidTimeZone", TimeZone.class);
        testDeserialize("invalidIpAddress", InetAddress.class);
        testDeserialize("invalidIpAddress:port", InetSocketAddress.class);
    }

    private <T> void testDeserialize(String value, Class<T> expectedType) throws IOException {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser parser = jsonFactory.createParser(new StringReader(String.format("{\"value\":\"%s\"}", value)));

        T result = new FromStringDeserializer<T>(expectedType).deserialize(parser, objectMapper.getDeserializationContext());

        if (expectedType.isInstance(result)) {
            assertEquals(expectedType.getCanonicalName(), result.getClass().getCanonicalName());
        } else {
            assertEquals(expectedType.getName(), result.getClass().getName());
        }
    }

    // Replace StringReader with an appropriate InputStreamReader if needed
    private static class StringReader extends InputStreamReader {
        public StringReader(String s) {
            super(new ByteArrayInputStream(s.getBytes()));
        }
    }
}