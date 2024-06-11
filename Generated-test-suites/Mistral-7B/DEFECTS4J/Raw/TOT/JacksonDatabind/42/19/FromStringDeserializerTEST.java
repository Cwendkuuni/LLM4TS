package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class FromStringDeserializerTest {

    private static ObjectMapper objectMapper = new JsonMapper();

    @org.junit.Test
    public void testFileDeserialization() throws IOException, URISyntaxException {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        String jsonString = "{\"className\":\"java.io.File\",\"path\":\"/path/to/file.txt\"}";
        File expected = new File("/path/to/file.txt");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @org.junit.Test
    public void testURLDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "{\"className\":\"java.net.URL\",\"string\":\"https://example.com/path/to\"}";
        URL expected = new URL("https://example.com/path/to");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testURIDeserialization() throws IOException, URISyntaxException {
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String jsonString = "{\"className\":\"java.net.URI\",\"string\":\"https://example.com/path/to\"}";
        URI expected = new URI("https://example.com/path/to");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.toASCIIString(), result.toString());
    }

    @org.junit.Test
    public void testClassDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(String.class);
        String jsonString = "{\"className\":\"java.lang.String\",\"value\":\"Hello World\"}";
        String expected = "Hello World";

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testJavaTypeDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(JavaType.class);
        String jsonString = "{\"className\":\"com.fasterxml.jackson.databind.type.TypeFactory$DefaultTypeResolver$1\",\"rawClass\":\"java.net.URI\",\"typeIdentifier\":\"URI\"}";
        JavaType expected = TypeFactory.defaultInstance().constructType(URI.class);

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.getClass(), result.getClass());
    }

    @org.junit.Test
    public void testCurrencyDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(Currency.class);
        String jsonString = "{\"className\":\"java.util.Currency\",\"currencyCode\":\"EUR\"}";
        Currency expected = Currency.getInstance("EUR");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.toString(), result.toString());
    }

    @org.junit.Test
    public void testPatternDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(java.util.regex.Pattern.class);
        String jsonString = "{\"className\":\"java.util.regex.Pattern\",\"pattern\":\"\\w+\\.\\w+@\\w+\\.\\w+\"}";
        Pattern expected = Pattern.compile("\\w+\\.\\w+@\\w+\\.\\w+");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected.pattern(), result.pattern());
    }

    @org.junit.Test
    public void testLocaleDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        String jsonString = "{\"className\":\"java.util.Locale\",\"language\":\"en\",\"country\":\"US\"}";
        Locale expected = new Locale("en", "US");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testCharsetDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(Charset.class);
        String jsonString = "{\"className\":\"java.nio.charset.Charset\",\"name\":\"UTF-8\"}";
        Charset expected = Charset.forName("UTF-8");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testTimeZoneDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        String jsonString = "{\"className\":\"java.util.TimeZone\",\"id\":\"America/Los_Angeles\"}";
        TimeZone expected = TimeZone.getTimeZone("America/Los_Angeles");

        Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testEmptyStringDeserialization() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        String jsonString = "{}";

        try {
            Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
            fail("Expected JsonMappingException for empty JSON object");
        } catch(JsonMappingException expected) {
            // expected
        }
    }

    @org.junit.Test
    public void testInvalidFormatException() throws IOException {
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        String jsonString = "{\"className\":\"java.net.URL\",\"string\":\"invalid\"}";

        try {
            Object result = deserializer.deserialize(objectMapper.getFactory().createParser(jsonString), objectMapper.getDeserializationContext());
            fail("Expected InvalidFormatException for invalid URL string");
        } catch(InvalidFormatException expected) {
            // expected
        }
    }
}