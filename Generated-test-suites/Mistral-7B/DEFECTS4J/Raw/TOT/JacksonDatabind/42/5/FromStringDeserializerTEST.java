package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Test cases for {@link FromStringDeserializer}.
 */
public class FromStringDeserializerTest {

    /**
     * Test deserialization of simple types.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testSimpleDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserialization of various types
        testDeserialization(mapper, "file.txt", File.class);
        testDeserialization(mapper, "http://example.com/", URL.class);
        testDeserialization(mapper, "http://example.com/path/to/file.txt", URI.class);
        testDeserialization(mapper, "java.util.Locale", Locale.class);
        testDeserialization(mapper, "UTF-8", Charset.class);
        testDeserialization(mapper, "Europe/Paris", TimeZone.class);
        testDeserialization(mapper, "1.2", Double.class);
        testDeserialization(mapper, "true", Boolean.class);
        testDeserialization(mapper, "\"Hello World\"", String.class);

        testDeserialization(mapper, "EUR", Currency.class);
        testDeserialization(mapper, "[1,2]", int[].class);
        testDeserialization(mapper, "[1,2]", java.util.List.class);
        testDeserialization(mapper, "[1,2]", JavaType.constructParamType(int[].class, 2));

        testDeserialization(mapper, "\\.test.com$", Pattern.class);
    }

    /**
     * Test deserialization of null values.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testNullDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String inputJson = "null";
        JsonParser parser = new JsonFactory().createParser(new StringReader(inputJson));

        // Test deserialization of various types
        testDeserializationWithJSONParser(parser, null, File.class);
        testDeserializationWithJSONParser(parser, null, URL.class);
        testDeserializationWithJSONParser(parser, null, URI.class);
        testDeserializationWithJSONParser(parser, null, Locale.class);
        testDeserializationWithJSONParser(parser, null, Charset.class);
        testDeserializationWithJSONParser(parser, null, TimeZone.class);
        testDeserializationWithJSONParser(parser, null, Double.class);
        testDeserializationWithJSONParser(parser, null, Boolean.class);
        testDeserializationWithJSONParser(parser, null, String.class);

        testDeserializationWithJSONParser(parser, null, Currency.class);
        testDeserializationWithJSONParser(parser, null, int[].class);
        testDeserializationWithJSONParser(parser, null, java.util.List.class);
        testDeserializationWithJSONParser(parser, null, JavaType.constructParamType(int[].class, 2));

        testDeserializationWithJSONParser(parser, null, Pattern.class);
    }

    /**
     * Test deserialization of empty strings.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testEmptyStringDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String inputJson = "\"\"";
        JsonParser parser = new JsonFactory().createParser(new StringReader(inputJson));

        // Test deserialization of various types
        testDeserializationWithJSONParser(parser, "", File.class);
        testDeserializationWithJSONParser(parser, "", URL.class);
        testDeserializationWithJSONParser(parser, "", URI.class);
        testDeserializationWithJSONParser(parser, "", Locale.class);
        testDeserializationWithJSONParser(parser, "", Charset.class);
        testDeserializationWithJSONParser(parser, "", TimeZone.class);
        testDeserializationWithJSONParser(parser, "", Double.class);
        testDeserializationWithJSONParser(parser, "", Boolean.class);
        testDeserializationWithJSONParser(parser, "", String.class);

        testDeserializationWithJSONParser(parser, "", Currency.class);
        testDeserializationWithJSONParser(parser, "", int[].class);
        testDeserializationWithJSONParser(parser, "", java.util.List.class);
        testDeserializationWithJSONParser(parser, "", JavaType.constructParamType(int[].class, 2));

        testDeserializationWithJSONParser(parser, "", Pattern.class);

        // Special cases for URI and Locale
        testDeserializationWithJSONParser(parser, "", URI.class);
        testDeserializationWithJSONParser(parser, "", Locale.class);
    }

    /**
     * Test deserialization of invalid strings.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testInvalidStringDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String inputJson = "invalidInput";
        JsonParser parser = new JsonFactory().createParser(new StringReader(inputJson));

        // Test deserialization of various types
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, File.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, URL.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, URI.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Locale.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Charset.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, TimeZone.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Double.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Boolean.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, String.class));

        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Currency.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, int[].class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, java.util.List.class));
        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, JavaType.constructParamType(int[].class, 2)));

        assertThrows(JsonMappingException.class, () -> testDeserializationWithJSONParser(parser, inputJson, Pattern.class));
    }

    /**
     * Test deserialization with embedded objects.
     *
     * @throws Exception if any error occurs
     */
    @Test
    public void testEmbeddedObjectDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String inputJson = "{\"value\": 42}";
        JsonParser parser = new JsonFactory().createParser(new StringReader(inputJson));

        // Test deserialization of various types
        Object result = mapper.readValue(parser, Object.class);
        assertEquals(42, result.hashCode());
        assertEquals(Integer.TYPE, result.getClass().getComponentType());
    }

    /**
     * Helper method to test deserialization with JSONParser.
     *
     * @param parser   JSONParser to use
     * @param input    input value
     * @param expectedType expected type
     * @throws JsonProcessingException if deserialization fails
     */
    private <T> void testDeserializationWithJSONParser(JsonParser parser, String input, Class<T> expectedType) throws JsonProcessingException {
        testDeserializationWithJSONParser(parser, input, expectedType, null);
    }

    /**
     * Helper method to test deserialization with JSONParser.
     *
     * @param parser   JSONParser to use
     * @param input    input value
     * @param expectedType expected type
     * @param expectedValue expected value
     * @throws JsonProcessingException if deserialization fails
     */
    private <T> void testDeserializationWithJSONParser(JsonParser parser, String input, Class<T> expectedType, T expectedValue) throws JsonProcessingException {
        ObjectReader reader = new ObjectMapper().readerForType(expectedType);
        T result = reader.readValue(parser);

        if (expectedValue != null) {
            assertEquals(expectedValue.getClass(), result.getClass());
            assertEquals(expectedValue, result);
        } else {
            assertNotNull(result);
        }
    }

    /**
     * Helper method to test deserialization with ObjectMapper.
     *
     * @param mapper ObjectMapper to use
     * @param input  input value
     * @param expectedType expected type
     * @throws IOException if deserialization fails
     */
    private <T> void testDeserialization(ObjectMapper mapper, String input, Class<T> expectedType) throws IOException {
        StringReader reader = new StringReader(input);
        T result = mapper.readValue(reader, expectedType);

        assertNotNull(result);
        assertEquals(expectedType, result.getClass());
    }

    /**
     * Helper class for reading strings.
     */
    static class StringReader implements Reader {

        private final String content;

        public StringReader(String content) {
            this.content = content;
        }

        @Override
        public int read(char[] cbuf, int off, int length) throws IOException {
            int bytesRead = 0;

            if (off < 0 || length < 0) {
                throw new IndexOutOfBoundsException("Bad length");
            }

            int remainingLength = content.length() - off;

            if (length > remainingLength) {
                length = remainingLength;
            }

            for (int i = 0; i < length; i++) {
                cbuf[off + i] = content.charAt(off + i);
                bytesRead++;
            }

            return bytesRead;
        }

        @Override
        public void close() throws IOException {
            // No-op
        }
    }
}