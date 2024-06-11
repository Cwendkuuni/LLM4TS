package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FromStringDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFileDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"test.txt\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = (File) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testURLDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"http://test.com\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = (URL) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("http://test.com", result.toString());
    }

    @Test
    public void testURIDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"urn:test:com\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("urn:test:com", result.toString());
    }

    @Test
    public void testClassDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"java.lang.String.class\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(String.class);
        String result = (String) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("java.lang.String.class", result);
    }

    @Test
    public void testJavaTypeDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"com.fasterxml.jackson.databind.JsonNode.class\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(JsonNode.class);
        JavaType result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(JsonNode.class, result.getRawClass());
    }

    @Test
    public void testCurrencyDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"USD\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency result = (Currency) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testPatternDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"/^\\[a-z]+$/\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern result = (Pattern) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("/^[a-z]+$/", result.pattern());
    }

    @Test
    public void testLocaleDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"en_US\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale result = (Locale) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Locale.US, result);
    }

    @Test
    public void testCharsetDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"UTF-8\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset result = (Charset) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testTimeZoneDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"America/New_York\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("America/New_York", result.getID());
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"invalid\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        assertNull(deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testEmptyDeserialization() throws IOException, JsonProcessingException {
        JsonParser jsonParser = objectMapper.getFactory().createParser("\"\"");
        FromStringDeserializer deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(URI.class, result.getClass());
        assertEquals("", result.toString());
    }
}