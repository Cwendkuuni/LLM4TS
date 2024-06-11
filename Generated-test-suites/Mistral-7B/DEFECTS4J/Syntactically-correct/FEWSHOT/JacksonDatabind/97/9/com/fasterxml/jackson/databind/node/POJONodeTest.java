package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testEmptyInstance() {
        POJONode node = new POJONode(null);
        assertEquals(JsonNodeType.POJO, node.getNodeType());
        assertEquals("null", node.asText());
    }

    @Test
    public void testSerialize() throws IOException {
        Object obj = new Object();
        POJONode node = new POJONode(obj);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node.serialize(gen, null);
        assertTrue(gen.getOutputBuffer().toString().contains(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)));
    }

    @Test
    public void testSerializeWithDefaultNullValue() throws IOException {
        POJONode node = new POJONode(null);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node.serialize(gen, null);
        assertTrue(gen.getOutputBuffer().toString().contains("null"));
    }

    @Test
    public void testSerializeJsonSerializable() throws IOException {
        TestJsonSerializable testObj = new TestJsonSerializable();
        POJONode node = new POJONode(testObj);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node.serialize(gen, null);
        assertTrue(gen.getOutputBuffer().toString().contains(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testObj)));
    }

    @Test
    public void testEquals() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        POJONode node1 = new POJONode(obj1);
        POJONode node2 = new POJONode(obj2);
        assertFalse(node1.equals(node2));

        POJONode node3 = new POJONode(obj1);
        assertTrue(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode node1 = new POJONode(obj);
        POJONode node2 = new POJONode(obj);
        assertEquals(node1.hashCode(), node2.hashCode());
    }

    @Test
    public void testToString() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(String.valueOf(obj), node.toString());
    }

    @Test
    public void testGetPojo() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals(obj, node.getPojo());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] array = new byte[]{1, 2, 3};
        POJONode node = new POJONode(array);
        byte[] result = node.binaryValue();
        assertSame(array, result);
    }

    @Test(expected = IOException.class)
    public void testBinaryValueForNonByteArray() throws IOException {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        node.binaryValue();
    }

    @Test
    public void testAsText() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals("" + obj, node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        Object obj = new Object();
        POJONode node = new POJONode(obj);
        assertEquals("", node.asText(""));
    }

    @Test
    public void testAsBoolean() {
        Object obj = Boolean.TRUE;
        POJONode node = new POJONode(obj);
        assertTrue(node.asBoolean(false));
        assertEquals(obj, node.getPojo());

        Object objFalse = Boolean.FALSE;
        POJONode nodeFalse = new POJONode(objFalse);
        assertFalse(nodeFalse.asBoolean(true));
        assertEquals(objFalse, nodeFalse.getPojo());
    }

    @Test
    public void testAsBooleanWithDefaultValue() {
        Object obj = Boolean.FALSE;
        POJONode node = new POJONode(obj);
        assertFalse(node.asBoolean(true));
        assertEquals(obj, node.getPojo());

        Object objTrue = Boolean.TRUE;
        POJONode nodeTrue = new POJONode(objTrue);
        assertTrue(nodeTrue.asBoolean(false));
        assertEquals(objTrue, nodeTrue.getPojo());
    }

    @Test
    public void testAsInt() {
        Object obj = Integer.valueOf(1);
        POJONode node = new POJONode(obj);
        assertEquals(1, node.asInt(0));
        assertEquals(obj, node.getPojo());

        Object objNegative = Integer.valueOf(-1);
        POJONode nodeNegative = new POJONode(objNegative);
        assertEquals(-1, nodeNegative.asInt(0));
        assertEquals(objNegative, nodeNegative.getPojo());
    }

    @Test
    public void testAsIntWithDefaultValue() {
        Object obj = Integer.valueOf(-1);
        POJONode node = new POJONode(obj);
        assertEquals(0, node.asInt(1));
        assertEquals(obj, node.getPojo());

        Object objZero = Integer.valueOf(0);
        POJONode nodeZero = new POJONode(objZero);
        assertEquals(0, nodeZero.asInt(1));
        assertEquals(objZero, nodeZero.getPojo());
    }

    @Test
    public void testAsLong() {
        Object obj = Long.valueOf(1L);
        POJONode node = new POJONode(obj);
        assertEquals(1L, node.asLong(0L));
        assertEquals(obj, node.getPojo());

        Object objNegative = Long.valueOf(-1L);
        POJONode nodeNegative = new POJONode(objNegative);
        assertEquals(-1L, nodeNegative.asLong(0L));
        assertEquals(objNegative, nodeNegative.getPojo());
    }

    @Test
    public void testAsLongWithDefaultValue() {
        Object obj = Long.valueOf(-1L);
        POJONode node = new POJONode(obj);
        assertEquals(0L, node.asLong(1L));
        assertEquals(obj, node.getPojo());

        Object objZero = Long.valueOf(0L);
        POJONode nodeZero = new POJONode(objZero);
        assertEquals(0L, nodeZero.asLong(1L));
        assertEquals(objZero, nodeZero.getPojo());
    }

    @Test
    public void testAsDouble() {
        Object obj = Double.valueOf(1.0);
        POJONode node = new POJONode(obj);
        assertEquals(1.0, node.asDouble(0.0), 0.0);
        assertEquals(obj, node.getPojo());

        Object objNegative = Double.valueOf(-1.0);
        POJONode nodeNegative = new POJONode(objNegative);
        assertEquals(-1.0, nodeNegative.asDouble(0.0), 0.0);
        assertEquals(objNegative, nodeNegative.getPojo());
    }

    @Test
    public void testAsDoubleWithDefaultValue() {
        Object obj = Double.valueOf(-1.0);
        POJONode node = new POJONode(obj);
        assertEquals(0.0, node.asDouble(1.0), 0.0);
        assertEquals(obj, node.getPojo());

        Object objZero = Double.valueOf(0.0);
        POJONode nodeZero = new POJONode(objZero);
        assertEquals(0.0, nodeZero.asDouble(1.0), 0.0);
        assertEquals(objZero, nodeZero.getPojo());
    }

    @Test
    public void testSerializationForJsonSerializableObjectsWithSameValues() throws IOException {
        TestJsonSerializable obj1 = new TestJsonSerializable("Obj1");
        POJONode node1 = new POJONode(obj1);

        TestJsonSerializable obj2 = new TestJsonSerializable("Obj1");
        POJONode node2 = new POJONode(obj2);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node1.serialize(gen, null);
        node2.serialize(gen, null);
        assertEquals(gen.getOutputBuffer().toString(), gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializationForJsonSerializableObjectsWithDifferentValues() throws IOException {
        TestJsonSerializable obj1 = new TestJsonSerializable("Obj1");
        POJONode node1 = new POJONode(obj1);

        TestJsonSerializable obj2 = new TestJsonSerializable("Obj2");
        POJONode node2 = new POJONode(obj2);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node1.serialize(gen, null);
        node2.serialize(gen, null);
        assertNotSame(gen.getOutputBuffer().toString(), gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializationForJsonSerializableObjectsWithSameClassButDifferentValues() throws IOException {
        TestJsonSerializable obj1 = new TestJsonSerializable("Obj1");
        POJONode node1 = new POJONode(obj1);

        TestJsonSerializable obj2 = new TestJsonSerializable("Obj2");
        POJONode node2 = new POJONode(obj2);

        TestJsonSerializable obj3 = new TestJsonSerializable("Obj1");
        POJONode node3 = new POJONode(obj3);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        node1.serialize(gen, null);
        node2.serialize(gen, null);
        node3.serialize(gen, null);
        assertNotSame(gen.getOutputBuffer().toString(), gen.getOutputBuffer().toString());
    }

    static class TestJsonSerializable implements JsonSerializable {
        private final String name;

        public TestJsonSerializable(String name) {
            this.name = name;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(name);
        }
    }
}