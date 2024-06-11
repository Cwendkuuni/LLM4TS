package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test class for the FromStringDeserializer abstract class.
 */
public class FromStringDeserializerTest {

    /**
     * Test deserialization of a URL from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testUrlDeserialization() throws IOException {
        String json = "{\"value\": \"https://www.example.com/path?query=parameter\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        UrlDeserializer urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL url = urlDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("https://www.example.com/path?query=parameter", url.toString());
    }

    /**
     * Test deserialization of a Locale from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testLocaleDeserialization() throws IOException {
        String json = "{\"value\": \"en_US\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        LocaleDeserializer localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale locale = localeDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("en_US", locale.toString());
    }

    /**
     * Test deserialization of a TimeZone from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testTimeZoneDeserialization() throws IOException {
        String json = "{\"value\": \"America/New_York\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TimeZoneDeserializer timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone timeZone = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("America/New_York", timeZone.getID());
    }

    /**
     * Test deserialization of a Currency from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testCurrencyDeserialization() throws IOException {
        String json = "{\"value\": \"USD\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        CurrencyDeserializer currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency currency = currencyDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals(Currency.getInstance("USD"), currency);
    }

    /**
     * Test deserialization of a URI from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testUriDeserialization() throws IOException {
        String json = "{\"value\": \"http://www.example.com\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        UriDeserializer uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI uri = uriDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("http://www.example.com", uri.toString());
    }

    /**
     * Test deserialization of a Class from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testClassDeserialization() throws IOException {
        String json = "{\"value\": \"java.lang.String\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ClassDeserializer classDeserializer = FromStringDeserializer.findDeserializer(String.class);
        Class<?> clazz = classDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals(String.class, clazz);
    }

    /**
     * Test deserialization of a File from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testFileDeserialization() throws IOException {
        String json = "{\"value\": \"/path/to/example.txt\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FileDeserializer fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File file = fileDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("/path/to/example.txt", file.getAbsolutePath());
    }

    /**
     * Test deserialization of a StringBuilder from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testStringBuilderDeserialization() throws IOException {
        String json = "{\"value\": \"Hello, world!\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        StringBuilderserializer stringBuilderDeserializer = FromStringDeserializer.findDeserializer(StringBuilder.class);
        StringBuilder stringBuilder = stringBuilderDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertEquals("Hello, world!", stringBuilder.toString());
    }

    /**
     * Test deserialization of a JSON Array from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testJsonArrayDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> jsonDeserializer = mapper.findValueDeserializer(Object.class, rootNode);
        Object obj = jsonDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertTrue(obj instanceof ArrayNode);
        ObjectMapper arrayMapper = new ObjectMapper();
        JsonNode arrayNode = ((ArrayNode) obj).get(0);
        int number = arrayMapper.readValue(arrayNode, Integer.class);
        assertEquals(1, number);
    }

    /**
     * Test deserialization of a JSON Object from a String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testJsonObjectDeserialization() throws IOException {
        String json = "{\"a\": 1, \"b\": 2}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonDeserializer<Object> jsonDeserializer = mapper.findValueDeserializer(Object.class, rootNode);
        Object obj = jsonDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertTrue(obj instanceof ObjectNode);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = (ObjectNode) obj;
        int a = objectMapper.readValue(objectNode.get("a"), Integer.class);
        assertEquals(1, a);
        int b = objectMapper.readValue(objectNode.get("b"), Integer.class);
        assertEquals(2, b);
    }

    /**
     * Test deserialization of an empty String.
     *
     * @throws IOException if deserialization fails
     */
    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "\"\"";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Object> fromStringDeserializer = new FromStringDeserializer<Object>(Object.class) {
            @Override
            protected Object _deserialize(String value, DeserializationContext context) throws IOException {
                return null;
            }
        };
        Object obj = fromStringDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        assertNull(obj);
    }

    /**
     * Test deserialization of an unknown type.
     *
     * @throws IOException if deserialization fails
     */
    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeDeserialization() throws IOException {
        String json = "{\"value\": \"Xyz\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        FromStringDeserializer<Object> fromStringDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        Object obj = fromStringDeserializer.deserialize(mapper.getFactory().createParser(rootNode), ctxt);
        fail("Expected JsonMappingException");
    }
}