package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.StringWriter;
import java.io.IOException;
import java.io.Writer;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testgetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testBinaryValueThrowsIOException() throws Exception {
        POJONode pojoNode = new POJONode(new String("test"));
        try {
            pojoNode.binaryValue();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testAsTextWithNullValue() {
        POJONode pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsBooleanWithNonBooleanValue() {
        POJONode pojoNode = new POJONode(new Integer(0));
        assertEquals(false, pojoNode.asBoolean(true));
    }

    @Test
    public void testAsIntWithNonNumberValue() {
        POJONode pojoNode = new POJONode(new String("test"));
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLongWithNonNumberValue() {
        POJONode pojoNode = new POJONode(new String("test"));
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDoubleWithNonNumberValue() {
        POJONode pojoNode = new POJONode(new String("test"));
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
    }

    @Test
    public void testEqualsWithSamePOJO() {
        Object obj1 = new Object();
        POJONode pojoNode1 = new POJONode(obj1);
        Object obj2 = obj1;
        POJONode pojoNode2 = new POJONode(obj2);
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testEqualsWithDifferentPOJO() {
        Object obj1 = new Object();
        POJONode pojoNode1 = new POJONode(obj1);
        Object obj2 = new Object();
        POJONode pojoNode2 = new POJONode(obj2);
        assertFalse(pojoNode1.equals(pojoNode2));
    }

    @Test
    public void testPojoEqualsWithSamePOJO() {
        Object obj1 = new Object();
        POJONode pojoNode1 = new POJONode(obj1);
        Object obj2 = obj1;
        POJONode pojoNode2 = new POJONode(obj2);
        assertTrue(pojoNode1._pojoEquals(pojoNode2));
    }

    @Test
    public void testPojoEqualsWithDifferentPOJO() {
        Object obj1 = new Object();
        POJONode pojoNode1 = new POJONode(obj1);
        Object obj2 = new Object();
        POJONode pojoNode2 = new POJONode(obj2);
        assertFalse(pojoNode1._pojoEquals(pojoNode2));
    }

    @Test
    public void testHashCodeWithSamePOJO() {
        Object obj = new Object();
        POJONode pojoNode1 = new POJONode(obj);
        POJONode pojoNode2 = new POJONode(obj);
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    @Test
    public void testToString() {
        POJONode pojoNode = new POJONode(new String("test"));
        assertEquals("(String) \"test\"", pojoNode.toString());

        pojoNode = new POJONode(new Integer(1));
        assertEquals("(Integer) 1", pojoNode.toString());

        pojoNode = new POJONode(new Double(3.14));
        assertEquals("(Double) 3.14", pojoNode.toString());

        pojoNode = new POJONode(new RawValue("test raw value"));
        assertEquals("(raw value 'test raw value')", pojoNode.toString());
    }

    @Test
    public void testSerializeWithJsonSerializablePOJO() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(Object.class, null);
        POJONode pojoNode = new POJONode(new Object());

        String json = mapper.writeValueAsString(pojoNode);
        JsonNode jsonNode = mapper.readTree(json);

        JsonNode expectedJsonNode = mapper.valueToTree(pojoNode.getPojo());
        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testSerializeWithNullPOJO() throws IOException {
        POJONode pojoNode = new POJONode(null);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        pojoNode.serialize(gen, new JsonFactory(), null);
        gen.close();

        String json = gen.getOutputContent();
        JsonNode jsonNode = mapper.readTree(json);
        assertEquals(JsonNodeType.NULL, jsonNode.getNodeType());
    }
}