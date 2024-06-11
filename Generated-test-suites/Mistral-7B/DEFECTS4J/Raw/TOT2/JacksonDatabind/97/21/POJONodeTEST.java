package com.example;

import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.mockito.*;

public class POJONodeTest {

    @Test
    public void testgetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Hello");
        assertEquals("Hello", node.asText());
    }

    @Test
    public void testAsTextDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText("Default"));
    }

    @Test
    public void testAsBoolean() {
        POJONode node = new POJONode(true);
        assertTrue(node.asBoolean(false));
    }

    @Test
    public void testAsBooleanDefaultValue() {
        POJONode node = new POJONode(new Object());
        assertFalse(node.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode node = new POJONode(1);
        assertEquals(1, node.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node = new POJONode(42L);
        assertEquals(42L, node.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node = new POJONode(3.1415);
        assertEquals(3.1415, node.asDouble(0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode(new Object());
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        when(ctxt.getActiveContext()).thenReturn(mock(SerializationConfig.class));
        node.serialize(gen, ctxt);
        verify(gen).writeObject(_isA(Object.class));
    }

    @Test(expected = IOException.class)
    public void testBinaryValueIOException() throws IOException {
        POJONode node = new POJONode(new byte[0]);
        node.binaryValue(); // This should throw IOException
    }

    @Test
    public void testSerializeIOException() throws IOException {
        POJONode node = new POJONode(new Object());
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider ctxt = mock(SerializerProvider.class);
        when(ctxt.getActiveContext()).thenReturn(mock(SerializationConfig.class));
        doThrow(IOException.class).when(gen).writeObject(_isA(Object.class));
        node.serialize(gen, ctxt);
    }

    @Test
    public void testGetPojo() {
        POJONode node = new POJONode(new Object());
        assertEquals(node._value, node.getPojo());
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        assertTrue(node1.equals(node2));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new Object());
        POJONode node2 = new POJONode(new Object());
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new Object());
        assertEquals("POJONode{_value=..." + node._value.getClass().getName() + "}", node.toString());
    }
}

//