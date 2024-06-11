package com.example; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws Exception {
        String json = "{\"@class\":\"File\",\"path\":\"test.txt\"}";
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        File result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext());
        File expected = new File("test.txt");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeUrl() throws Exception {
        String json = "{\"@class\":\"URL\",\"string\":\"http://example.com\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        URL result = deserializer.deserialize(new JsonParser(new StringReader(json)), mapper.getDeserializationContext());
        URL expected = new URL("http://example.com");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeClass() throws Exception {
        String json = "{\"@class\":\"java.lang.String\",\"value\":\"Hello World\"}";
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class);
        String result = deserializer.deserialize(new JsonParser(new StringReader(json)), mapper.getDeserializationContext());
        String expected = "Hello World";
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        FromStringDeserializer<File> deserializer = new FromStringDeserializer<File>(File.class);
        File result = deserializer.deserialize(_deserializeFromEmptyString(), mapper.getDeserializationContext());
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws Exception {
        String json = "{\"@class\":\"java.net.URL\",\"string\":\"abc\"}";
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        deserializer.deserialize(new JsonParser(new StringReader(json)), mapper.getDeserializationContext());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws Exception {
        String json = "{\"@class\":\"java.net.URL\",\"string\":\"http://example.com\"}";
        Object obj = new Object();
        FromStringDeserializer<URL> deserializer = new FromStringDeserializer<URL>(URL.class);
        URL result = deserializer._deserializeEmbedded(obj, mapper.getDeserializationContext());
        URL expected = new URL("http://example.com");
        assertEquals(expected, result);
    }

    private Object _deserializeFromEmptyString() throws IOException {
        return null;
    }
}