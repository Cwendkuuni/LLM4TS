package com.example.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> JavaType getJavaType(Class<T> type) {
        return new ObjectMapper().getTypeFactory().constructType(type);
    }

    @org.junit.Test
    public void testDeserialize_String_Class() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("\"test\"");

        String result = deserializer.deserialize(parser, ctxt);
        assertEquals("test", result);
    }

    @org.junit.Test
    public void testDeserialize_String_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("http://example.com");

        URL result = deserializer.deserialize(parser, ctxt);
        assertEquals("http://example.com", result.toString());
    }

    @org.junit.Test
    public void testDeserialize_String_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("http://example.com");

        URI result = deserializer.deserialize(parser, ctxt);
        assertEquals("http", result.getScheme());
        assertEquals("example.com", result.getHost());
    }

    @org.junit.Test
    public void testDeserialize_String_ClassWithEmptyString() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("\"\"");

        String result = deserializer.deserialize(parser, ctxt);
        assertEquals("", result);
    }

    @org.junit.Test
    public void testDeserialize_String_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("en_US");

        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals("en_US", result.toString());
    }

    @org.junit.Test
    public void testDeserialize_String_LocaleWithTwoArguments() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("en_US_POSIX");

        Locale result = deserializer.deserialize(parser, ctxt);
        assertEquals("en_US_POSIX", result.toString());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString_Class() throws IOException {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("\"test\"");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_NullString_Class() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(null);

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyArray_Class() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("[ ]");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_Array_Class() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("[\"test\", \"test2\"]");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString_URL() throws IOException {
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{ \"invalid\": \"format\" }");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{ \"invalid\": \"format\" }");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserialize_NullString_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser(null);

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserialize_EmptyString_URI() throws IOException {
        FromStringDeserializer<URI> deserializer = new FromStringDeserializer<URI>(URI.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("\"\"");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidString_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("invalid_locale");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidStringWithTwoArguments_Locale() throws IOException {
        FromStringDeserializer<Locale> deserializer = new FromStringDeserializer<Locale>(Locale.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("invalid_locale_with_two_arguments");

        deserializer.deserialize(parser, ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_InvalidType() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": \"test\"}");

        deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject_String() throws IOException {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": \"test\"}");

        String result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals("test", result);
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject_Integer() throws IOException {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": 123}");

        Integer result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals(123, result.intValue());
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject_Double() throws IOException {
        FromStringDeserializer<Double> deserializer = new FromStringDeserializer<Double>(Double.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": 123.45}");

        Double result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject_Boolean() throws IOException {
        FromStringDeserializer<Boolean> deserializer = new FromStringDeserializer<Boolean>(Boolean.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": true}");

        Boolean result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertTrue(result.booleanValue());
    }

    @org.junit.Test
    public void testDeserializeEmbeddedObject_Null() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": null}");

        Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertNull(result);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_InvalidJson() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\":");

        deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_EmptyJson() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{}");

        deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_UnknownType() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("{\"object\": [1, 2, 3]}");

        deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testDeserializeEmbeddedObject_Array() throws IOException {
        FromStringDeserializer<Object> deserializer = new FromStringDeserializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");

        deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
    }
}