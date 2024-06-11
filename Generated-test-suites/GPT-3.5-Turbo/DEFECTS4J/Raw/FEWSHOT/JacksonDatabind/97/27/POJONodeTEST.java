package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class POJONodeTest {
    private POJONode pojoNode;

    @Before
    public void setUp() {
        pojoNode = new POJONode("test");
    }

    @Test
    public void testGetNodeType() {
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = "test".getBytes();
        POJONode byteArrayNode = new POJONode(bytes);
        assertArrayEquals(bytes, byteArrayNode.binaryValue());
        
        String stringValue = "test";
        POJONode stringNode = new POJONode(stringValue);
        assertNull(stringNode.binaryValue());
    }

    @Test
    public void testAsText() {
        assertEquals("test", pojoNode.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        assertEquals("test", pojoNode.asText("default"));
    }

    @Test
    public void testAsBoolean() {
        assertEquals(false, pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        assertEquals(0.0, pojoNode.asDouble(0.0), 0);
    }

    @Test
    public void testSerialize() throws IOException {
        // test for null _value
        POJONode nullValueNode = new POJONode(null);
        JsonGenerator nullValueGenerator = null; // your JsonGenerator instantiation here
        SerializerProvider nullValueProvider = null; // your SerializerProvider instantiation here
        nullValueNode.serialize(nullValueGenerator, nullValueProvider);
        
        // test for _value being instance of JsonSerializable
        POJONode jsonSerializableNode = new POJONode(new CustomJsonSerializable());
        JsonGenerator jsonSerializableGenerator = null; // your JsonGenerator instantiation here
        SerializerProvider jsonSerializableProvider = null; // your SerializerProvider instantiation here
        jsonSerializableNode.serialize(jsonSerializableGenerator, jsonSerializableProvider);
        
        // test for _value not being instance of JsonSerializable
        POJONode objectNode = new POJONode(new Object());
        JsonGenerator objectGenerator = null; // your JsonGenerator instantiation here
        SerializerProvider objectProvider = null; // your SerializerProvider instantiation here
        objectNode.serialize(objectGenerator, objectProvider);
    }

    @Test
    public void testGetPojo() {
        assertEquals("test", pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        assertFalse(pojoNode.equals(null));
        assertTrue(pojoNode.equals(pojoNode));
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", pojoNode.toString());
        
        byte[] byteArray = "test".getBytes();
        POJONode byteArrayNode = new POJONode(byteArray);
        String byteArrayToString = String.format("(binary value of %d bytes)", byteArray.length);
        assertEquals(byteArrayToString, byteArrayNode.toString());
        
        RawValue rawValue = new RawValue("test");
        POJONode rawValueNode = new POJONode(rawValue);
        String rawValueToString = String.format("(raw value '%s')", rawValue.toString());
        assertEquals(rawValueToString, rawValueNode.toString());
    }
    
    private class CustomJsonSerializable implements JsonSerializable {
        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // implementation details
        }
    }
}