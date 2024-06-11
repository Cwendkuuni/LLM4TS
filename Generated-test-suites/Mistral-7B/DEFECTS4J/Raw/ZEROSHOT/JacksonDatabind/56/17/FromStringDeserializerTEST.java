package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Test class for FromStringDeserializer.
 */
public class FromStringDeserializerTest {

    /**
     * Test deserialization of different types.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserialization() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        // Test serialization and deserialization of different types
        testDeserialization(jsonFactory, File.class);
        testDeserialization(jsonFactory, URL.class);
        testDeserialization(jsonFactory, URI.class);
        testDeserialization(jsonFactory, Class.class);
        testDeserialization(jsonFactory, JavaType.class);
        testDeserialization(jsonFactory, Currency.class);
        testDeserialization(jsonFactory, Pattern.class);
        testDeserialization(jsonFactory, Locale.class);
        testDeserialization(jsonFactory, Charset.class);
        testDeserialization(jsonFactory, TimeZone.class);
        testDeserialization(jsonFactory, InetAddress.class);
        testDeserialization(jsonFactory, InetSocketAddress.class);
    }

    /**
     * Helper method for testing deserialization of a specific type.
     * @param jsonFactory JsonFactory instance
     * @param clazz Class to deserialize
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void testDeserialization(JsonFactory jsonFactory, Class<?> clazz) throws IOException, JsonProcessingException {
        String json = "\"some-value\"";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();
        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(clazz);
        Object result = deserializer.deserialize(parser, ctxt);
        assertEquals(clazz, result.getClass());

        // Test deserialization from empty string
        parser = jsonFactory.createParser("\"\"");
        result = deserializer.deserialize(parser, ctxt);
        if (clazz == URI.class || clazz == Locale.class) {
            assertEquals(clazz.getDeclaredMethod("getEmptyInstance").invoke(null), result);
        } else {
            assertEquals(null, result);
        }

        // Test deserialization from null string
        parser = jsonFactory.createParser(null);
        result = deserializer.deserialize(parser, ctxt);
        assertEquals(null, result);

        // Test deserialization from malformed string
        json = "{\"some-value\": 42}";
        parser = jsonFactory.createParser(json);
        try {
            result = deserializer.deserialize(parser, ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    /**
     * Test deserialization of an embedded object.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "{\"value\": {\"class\": \"java.net.URI\", \"value\": \"http://example.com\"}}";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals(URI.class, result.getClass());
        assertEquals("http://example.com", result.toString());
    }

    /**
     * Test deserialization of an empty embedded object.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationEmptyEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "{\"value\": {\"class\": \"java.net.URI\"}}";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals(URI.class, result.getClass());
        assertEquals(URI.class.getDeclaredMethod("getEmptyInstance").invoke(null), result);
    }

    /**
     * Test deserialization of an invalid embedded object.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationInvalidEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "{\"value\": {\"class\": \"java.lang.String\", \"value\": \"some-value\"}}";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        try {
            Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    /**
     * Test deserialization of a null embedded object.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationNullEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "{\"value\": null}";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI.class);
        Object result = deserializer.deserializeEmbedded(parser.readValueAsTree(), ctxt);
        assertEquals(null, result);
    }

    /**
     * Test deserialization of a JSON array containing an embedded object.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationArrayEmbeddedObject() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "[{\"class\": \"java.net.URI\", \"value\": \"http://example.com\"}]";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI[].class);
        Object[] result = deserializer.deserialize(parser, ctxt);
        assertEquals(1, result.length);
        assertEquals(URI.class, result[0].getClass());
        assertEquals("http://example.com", result[0].toString());
    }

    /**
     * Test deserialization of a JSON array containing multiple embedded objects.
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Test
    public void testDeserializationArrayMultipleEmbeddedObjects() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();

        String json = "[{\"class\": \"java.net.URI\", \"value\": \"http://example1.com\"}, {\"class\": \"java.net.URI\", \"value\": \"http://example2.com\"}]";
        JsonParser parser = jsonFactory.createParser(json);
        DeserializationContext ctxt = parser.getCodec().getDeserializationContext();

        StdScalarDeserializer<Object> deserializer = FromStringDeserializer.findDeserializer(URI[].class);
        Object[] result = deserializer.deserialize(parser, ctxt);
        assertEquals(2, result.length);
        for (Object obj : result) {
            assertEquals(URI.class, obj.getClass());
            if (obj instanceof URI) {
                URI uri = (URI) obj;
                assertTrue(uri.isAbsolute());
            }
        }
    }
}