package com.example.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.Std;

public class FromStringDeserializerTest {

    private static JsonParser parser;
    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() throws IOException {
        mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{}");
    }

    @Test
    public void testDeserialize_File() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        File result = (File) deserializer.deserialize(parser, ctxt);
        assertNull(result);

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "/path/to/file.txt");
        String jsonString = jsonNode.toString();
        result = (File) deserializer.deserialize(parser, ctxt);
        assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testDeserialize_URL() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL result = (URL) deserializer.deserialize(parser, ctxt);
        assertNull(result);

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "http://example.com:8080/test");
        String jsonString = jsonNode.toString();
        result = (URL) deserializer.deserialize(parser, ctxt);
        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());
        assertEquals(8080, result.getPort());
        assertEquals("/test", result.getPath());
    }

    @Test
    public void testDeserialize_URI() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI result = (URI) deserializer.deserialize(parser, ctxt);
        assertNull(result);

        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "http://example.com:8080/test{?a=1&b=2}");
        String jsonString = jsonNode.toString();
        result = (URI) deserializer.deserialize(parser, ctxt);
        assertEquals("http", result.getScheme());
        assertEquals("example.com", result.getHost());
        assertEquals(8080, result.getPort());
        assertEquals("/test", result.getPath());
        assertEquals("a=1&b=2", result.getQuery());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_UnsupportedType() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(Integer.class);
        deserializer.deserialize(parser, ctxt);
        fail("Deserialization should have failed");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidString() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(URL.class);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume null value
        parser.nextToken(); // consume END_OBJECT
        deserializer.deserialize(parser, ctxt);
        fail("Deserialization should have failed");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidValue() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(Locale.class);
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "en_US_InvalidLocale");
        String jsonString = jsonNode.toString();
        deserializer.deserialize(parser, ctxt);
        fail("Deserialization should have failed");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_EmptyString() throws IOException {
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Std deserializer = FromStringDeserializer.findDeserializer(URI.class);
        ObjectNode jsonNode = mapper.createObjectNode();
        jsonNode.put("value", "");
        String jsonString = jsonNode.toString();
        deserializer.deserialize(parser, ctxt);
        fail("Deserialization should have failed");
    }
}