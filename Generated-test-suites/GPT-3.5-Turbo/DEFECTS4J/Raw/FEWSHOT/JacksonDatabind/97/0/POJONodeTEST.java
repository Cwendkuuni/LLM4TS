package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode node = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, node.asToken());
    }

    @Test
    public void testBinaryValue() throws Exception {
        POJONode node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("default", node.asText("default"));
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(true, node.asBoolean(true));
    }

    @Test
    public void testAsIntWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(10, node.asInt(10));
    }

    @Test
    public void testAsLongWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(100L, node.asLong(100L));
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals(3.14, node.asDouble(3.14), 0.001);
    }

    @Test
    public void testSerialize() throws Exception {
        // Test serialization of null value
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        POJONode node = new POJONode(null);
        node.serialize(gen, ctxt);
        verify(ctxt).defaultSerializeNull(gen);
        
        // Test serialization of JsonSerializable value
        JsonSerializable value = mock(JsonSerializable.class);
        node = new POJONode(value);
        node.serialize(gen, ctxt);
        verify(value).serialize(gen, ctxt);
        
        // Test serialization of non-JsonSerializable value
        Object nonSerializableValue = new Object();
        node = new POJONode(nonSerializableValue);
        node.serialize(gen, ctxt);
        verify(gen).writeObject(nonSerializableValue);
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value, node.getPojo());
    }

    @Test
    public void testEquals() {
        Object value = new Object();
        POJONode node1 = new POJONode(value);
        POJONode node2 = new POJONode(value);
        assertEquals(true, node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value.hashCode(), node.hashCode());
    }

    @Test
    public void testToString() {
        Object value = new Object();
        POJONode node = new POJONode(value);
        assertEquals(value.toString(), node.toString());
    }
}