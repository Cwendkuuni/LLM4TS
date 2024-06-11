package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.RawValue;

public class POJONodeTest {
    
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }
    
    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }
    
    @Test
    public void testBinaryValue() throws IOException {
        byte[] bytes = new byte[1];
        POJONode pojoNode = new POJONode(bytes);
        assertEquals(bytes, pojoNode.binaryValue());
    }
    
    @Test
    public void testBinaryValueNotBytes() throws IOException {
        POJONode pojoNode = new POJONode(null);
        assertNull(pojoNode.binaryValue());
    }
    
    @Test
    public void testAsText() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals("test", pojoNode.asText());
    }
    
    @Test
    public void testAsTextDefaultValue() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals("test", pojoNode.asText("default"));
    }
    
    @Test
    public void testAsTextNull() {
        Object value = null;
        POJONode pojoNode = new POJONode(value);
        assertEquals("null", pojoNode.asText());
    }
    
    @Test
    public void testAsBoolean() {
        Object value = true;
        POJONode pojoNode = new POJONode(value);
        assertEquals(true, pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsBooleanNotBoolean() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(false, pojoNode.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        Object value = 5;
        POJONode pojoNode = new POJONode(value);
        assertEquals(5, pojoNode.asInt(0));
    }
    
    @Test
    public void testAsIntNotNumber() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(0, pojoNode.asInt(0));
    }
    
    @Test
    public void testAsLong() {
        Object value = 10L;
        POJONode pojoNode = new POJONode(value);
        assertEquals(10L, pojoNode.asLong(0L));
    }
    
    @Test
    public void testAsLongNotNumber() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(0L, pojoNode.asLong(0L));
    }
    
    @Test
    public void testAsDouble() {
        Object value = 1.2;
        POJONode pojoNode = new POJONode(value);
        assertEquals(1.2, pojoNode.asDouble(0.0), 0);
    }
    
    @Test
    public void testAsDoubleNotNumber() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(0.0, pojoNode.asDouble(0.0), 0);
    }
    
    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        POJONode pojoNode = new POJONode(null);
        pojoNode.serialize(gen, ctxt);
        
        verify(ctxt).defaultSerializeNull(gen);
    }
    
    @Test
    public void testSerializeJsonSerializable() throws IOException {
        JsonSerializable value = mock(JsonSerializable.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        POJONode pojoNode = new POJONode(value);
        pojoNode.serialize(gen, ctxt);
        
        verify(value).serialize(gen, ctxt);
    }
    
    @Test
    public void testSerializeObject() throws IOException {
        Object value = "test";
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        
        POJONode pojoNode = new POJONode(value);
        pojoNode.serialize(gen, ctxt);
        
        verify(gen).writeObject(value);
    }
    
    @Test
    public void testGetPojo() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(value, pojoNode.getPojo());
    }
    
    @Test
    public void testEqualsSameObject() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(true, pojoNode.equals(pojoNode));
    }
    
    @Test
    public void testEqualsNull() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(false, pojoNode.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        POJONode pojoNode = new POJONode("test");
        assertEquals(false, pojoNode.equals("test"));
    }
    
    @Test
    public void testEqualsSameValue() {
        POJONode pojoNode1 = new POJONode("test");
        POJONode pojoNode2 = new POJONode("test");
        assertEquals(true, pojoNode1.equals(pojoNode2));
    }
    
    @Test
    public void testEqualsDifferentValue() {
        POJONode pojoNode1 = new POJONode("test1");
        POJONode pojoNode2 = new POJONode("test2");
        assertEquals(false, pojoNode1.equals(pojoNode2));
    }
    
    @Test
    public void testHashCode() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }
    
    @Test
    public void testToStringBytes() {
        byte[] bytes = new byte[1];
        POJONode pojoNode = new POJONode(bytes);
        assertEquals("(binary value of 1 bytes)", pojoNode.toString());
    }
    
    @Test
    public void testToStringRawValue() {
        RawValue rawValue = mock(RawValue.class);
        POJONode pojoNode = new POJONode(rawValue);
        assertEquals("(raw value '", pojoNode.toString());
    }
    
    @Test
    public void testToString() {
        Object value = "test";
        POJONode pojoNode = new POJONode(value);
        assertEquals("test", pojoNode.toString());
    }
}