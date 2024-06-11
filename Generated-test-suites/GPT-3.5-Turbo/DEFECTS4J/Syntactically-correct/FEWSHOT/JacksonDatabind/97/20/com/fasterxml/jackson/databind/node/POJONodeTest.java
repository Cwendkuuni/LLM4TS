
package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

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
        byte[] value = {10, 20, 30};
        POJONode pojoNode = new POJONode(value);
        assertArrayEquals(value, pojoNode.binaryValue());
        
        POJONode pojoNode2 = new POJONode("test");
        assertNull(pojoNode2.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
        
        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText());
    }
    
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("default", pojoNode.asText("default"));
        
        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.asText("default"));
    }
    
    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(false));
        
        POJONode pojoNode2 = new POJONode(true);
        assertTrue(pojoNode2.asBoolean(false));
    }
    
    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(10, pojoNode.asInt(10));
        
        POJONode pojoNode2 = new POJONode(20);
        assertEquals(20, pojoNode2.asInt(10));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(100L, pojoNode.asLong(100L));
        
        POJONode pojoNode2 = new POJONode(200L);
        assertEquals(200L, pojoNode2.asLong(100L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(null);
        assertEquals(1.5, pojoNode.asDouble(1.5), 0.001);
        
        POJONode pojoNode2 = new POJONode(2.5);
        assertEquals(2.5, pojoNode2.asDouble(1.5), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator jsonGenerator = null; // Provide a valid instance of JsonGenerator
        SerializerProvider serializerProvider = null; // Provide a valid instance of SerializerProvider

        pojoNode.serialize(jsonGenerator, serializerProvider);
        // assert the expected behavior
    }

    @Test
    public void testGetPojo() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        
        assertEquals(value, pojoNode.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(null);
        POJONode pojoNode2 = new POJONode(null);
        assertTrue(pojoNode1.equals(pojoNode1));

        assertFalse(pojoNode1.equals(null));
        assertFalse(pojoNode1.equals(new Object()));
        assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testHashCode() {
        Object value = new Object();
        POJONode pojoNode = new POJONode(value);
        assertEquals(value.hashCode(), pojoNode.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode1 = new POJONode(null);
        assertEquals("null", pojoNode1.toString());

        POJONode pojoNode2 = new POJONode("test");
        assertEquals("test", pojoNode2.toString());

        byte[] value = {10, 20, 30};
        POJONode pojoNode3 = new POJONode(value);
        assertEquals("(binary value of 3 bytes)", pojoNode3.toString());
        
        POJONode pojoNode4 = new POJONode(new RawValue("test"));
        assertEquals("(raw value 'test')", pojoNode4.toString());
    }
}
