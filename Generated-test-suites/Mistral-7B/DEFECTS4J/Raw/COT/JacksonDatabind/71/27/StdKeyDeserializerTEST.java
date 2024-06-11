package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForTypeMethod() throws JsonMappingException, IOException {
        Class<?> raw = String.class;
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(raw);
        assertEquals(StdKeyDeserializer.TYPE_STRING, keyDeserializer._kind);
        assertEquals(String.class, keyDeserializer._keyClass);
        assertNull(keyDeserializer._deser);
    }

    @Test
    public void testDeserializeKeyMethodBooleanTrue() throws JsonMappingException, IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("key", "true");
        Boolean result = keyDeserializer.deserializeKey("key", new DeserializationContext(objectMapper));
        assertTrue(result);
    }

    @Test
    public void testDeserializeKeyMethodBooleanFalse() throws JsonMappingException, IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("key", "false");
        Boolean result = keyDeserializer.deserializeKey("key", new DeserializationContext(objectMapper));
        assertFalse(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKeyMethodNonBoolean() throws JsonMappingException, IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("key", "invalid");
        keyDeserializer.deserializeKey("key", new DeserializationContext(objectMapper));
    }

    @Test
    public void testDeserializeKeyMethodByte() throws JsonMappingException, IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("key", "127");
        Byte result = (Byte) keyDeserializer.deserializeKey("key", new DeserializationContext(objectMapper));
        assertEquals((byte) 127, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKeyMethodByteOutOfRange() throws JsonMappingException, IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("key", "128");
        keyDeserializer.deserializeKey("key", new DeserializationContext(objectMapper));
    }

    // Repeat the test cases for the other method types (SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, LOCALE, DATE, CALENDAR, UUID, URI, URL, and CLASS)

    @Test
    public void testParseMethodBooleanTrue() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        Object result = keyDeserializer._parse(key, new DeserializationContext(objectMapper));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testParseMethodBooleanFalse() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "false";
        Object result = keyDeserializer._parse(key, new DeserializationContext(objectMapper));
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    // Repeat the test cases for the other method types (BYTE, SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, LOCALE, DATE, CALENDAR, UUID, URI, URL, and CLASS)
}