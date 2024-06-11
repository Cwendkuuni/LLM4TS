package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.AbstractJsonNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode("Test POJO");
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Test POJO");
        assertEquals("Test POJO", node.asText());
    }

    @Test
    public void testAsText_whenNullValue_thenReturnDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("null", node.asText());
    }

    @Test
    public void testAsText_whenStringDefaultValue_thenReturnDefaultValue() {
        POJONode node = new POJONode(null);
        assertEquals("", node.asText(""));
    }

    @Test
    public void testAsBoolean() {
        POJONode node1 = new POJONode(Boolean.TRUE);
        assertTrue(node1.asBoolean(false));

        POJONode node2 = new POJONode(Boolean.FALSE);
        assertFalse(node2.asBoolean(true));

        POJONode node3 = new POJONode(null);
        assertFalse(node3.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode node1 = new POJONode(Integer.valueOf(1));
        assertEquals(1, node1.asInt(0));

        POJONode node2 = new POJONode(Integer.valueOf(-1));
        assertEquals(-1, node2.asInt(0));

        POJONode node3 = new POJONode(null);
        assertEquals(0, node3.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode node1 = new POJONode(Long.valueOf(1L));
        assertEquals(1L, node1.asLong(0L));

        POJONode node2 = new POJONode(Long.valueOf(-1L));
        assertEquals(-1L, node2.asLong(0L));

        POJONode node3 = new POJONode(null);
        assertEquals(0L, node3.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode node1 = new POJONode(Double.valueOf(1.5));
        assertEquals(1.5, node1.asDouble(0.0), 0.001);

        POJONode node2 = new POJONode(Double.valueOf(-1.5));
        assertEquals(-1.5, node2.asDouble(0.0), 0.001);

        POJONode node3 = new POJONode(null);
        assertEquals(0.0, node3.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerializeToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(new POJONode("Test POJO"));
        assertEquals("{\"@class\":\"POJONode\",\"_value\":{\"@class\":\"com.fasterxml.jackson.databind.JsonMappingException$Reference\"}}", json);

        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out, mapper.getSerializationConfig());
        POJONode pojoNode = new POJONode(new Object());
        pojoNode.serialize(gen, mapper.getSerializerProvider());
        gen.flush();
        assertEquals("{\"@class\":\"POJONode\",\"_value\":{\"@class\":\"Object\"}}", mapper.readTree(System.in).toString());
    }

    @Test
    public void testSerializeToJson_whenJsonSerializableObject_thenSerializedCorrectly() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass myClass = new MyClass();
        String json = mapper.writeValueAsString(new POJONode(myClass));
        assertEquals("{\"@class\":\"POJONode\",\"_value\":{\"name\":\"Test\"}}", json);
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode node = new POJONode(new byte[] { 1, 2, 3 });
        byte[] result = node.binaryValue();
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);

        node = new POJONode(null);
        assertNull(node.binaryValue());
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj1);
        POJONode node3 = new POJONode(new Object());

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
        assertFalse(node1.equals(null));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj1);
        POJONode node3 = new POJONode(new Object());

        assertEquals(obj1.hashCode(), node1.hashCode());
        assertEquals(obj1.hashCode(), node2.hashCode());
        assertNotEquals(obj1.hashCode(), node3.hashCode());
    }

    @Test
    public void testToString() {
        POJONode node1 = new POJONode("Test POJO");
        assertEquals("POJONode(Test POJO)", node1.toString());

        POJONode node2 = new POJONode(new Object());
        assertEquals("POJONode(Object)", node2.toString());

        POJONode node3 = new POJONode(null);
        assertEquals("POJONode(null)", node3.toString());

        POJONode node4 = new POJONode(new RawValue("Test String"));
        assertEquals("POJONode(raw value 'Test String')", node4.toString());

        POJONode node5 = new POJONode(new byte[] { 1, 2, 3 });
        assertEquals("POJONode(binary value of 3 bytes)", node5.toString());
    }

    @Test
    public void testGetPojo() {
        String testString = "Test POJO";
        POJONode node = new POJONode(testString);
        assertEquals(testString, node.getPojo());
    }

    static class MyClass {
        String name = "Test";
    }

}