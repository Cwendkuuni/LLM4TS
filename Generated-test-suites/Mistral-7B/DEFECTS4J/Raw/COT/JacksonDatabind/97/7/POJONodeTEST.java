package com.example.pojonode;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Test class for POJONode.
 */
public class POJONodeTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // 1. Test constructor
    @Test
    public void testPOJONodeConstructor() {
        POJONode pojoNode = new POJONode(new Object());
        assertNotNull(pojoNode);
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // 2. Test getNodeType()
    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // 3. Test asText()
    @Test
    public void testAsText() {
        POJONode pojoNode1 = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode1.asText());

        POJONode pojoNode2 = new POJONode(new Object());
        assertEquals("null", pojoNode2.asText());
    }

    // 4. Test asText(String defaultValue)
    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode1 = new POJONode("Hello World");
        assertEquals("Hello World", pojoNode1.asText());

        POJONode pojoNode2 = new POJONode(null);
        assertEquals("null", pojoNode2.asText());

        POJONode pojoNode3 = new POJONode(new Object());
        assertEquals("", pojoNode3.asText(""));
    }

    // 5. Test asBoolean(boolean defaultValue)
    @Test
    public void testAsBoolean() {
        POJONode pojoNode1 = new POJONode(true);
        assertTrue(pojoNode1.asBoolean(false));

        POJONode pojoNode2 = new POJONode(false);
        assertFalse(pojoNode2.asBoolean(true));

        POJONode pojoNode3 = new POJONode(null);
        assertEquals(false, pojoNode3.asBoolean(true));
    }

    // 6. Test asInt(int defaultValue)
    @Test
    public void testAsInt() {
        POJONode pojoNode1 = new POJONode(1);
        assertEquals(1, pojoNode1.asInt(0));

        POJONode pojoNode2 = new POJONode(2.5);
        assertEquals(0, pojoNode2.asInt(0));

        POJONode pojoNode3 = new POJONode(null);
        assertEquals(0, pojoNode3.asInt(0));
    }

    // 7. Test asLong(long defaultValue)
    @Test
    public void testAsLong() {
        POJONode pojoNode1 = new POJONode(1L);
        assertEquals(1L, pojoNode1.asLong(0L));

        POJONode pojoNode2 = new POJONode(2.5);
        assertEquals(0L, pojoNode2.asLong(0L));

        POJONode pojoNode3 = new POJONode(null);
        assertEquals(0L, pojoNode3.asLong(0L));
    }

    // 8. Test asDouble(double defaultValue)
    @Test
    public void testAsDouble() {
        POJONode pojoNode1 = new POJONode(1.0);
        assertEquals(1.0, pojoNode1.asDouble(0.0), 0.001);

        POJONode pojoNode2 = new POJONode(2.5);
        assertEquals(2.5, pojoNode2.asDouble(0.0), 0.001);

        POJONode pojoNode3 = new POJONode(null);
        assertEquals(0.0, pojoNode3.asDouble(0.0), 0.001);
    }

    // 9. Test serialize()
    @Test
    public void testSerialize() throws IOException {
        Object obj = new Object();
        POJONode pojoNode = new POJONode(obj);

        String json = "{\"class\":\"com.example.pojo.POJO\",\"value\":";
        json += objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        json += "}";

        String expectedJson = json;
        String actualJson = "";

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out, objectMapper.getSerializationConfig());
        pojoNode.serialize(jsonGenerator, objectMapper.getSerializerProvider());
        actualJson = jsonGenerator.getOutputContentAsString();

        assertEquals(expectedJson, actualJson);
    }

    // 10. Test getPojo()
    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode(new Object());
        Object pojo = pojoNode.getPojo();
        assertNotNull(pojo);
        assertEquals(pojoNode._value, pojo);
    }

    // 11. Test equals(Object o)
    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(new Object());
        POJONode pojoNode2 = new POJONode(new Object());
        assertFalse(pojoNode1.equals(pojoNode2));

        POJONode pojoNode3 = new POJONode(pojoNode1);
        assertTrue(pojoNode1.equals(pojoNode3));
    }

    // 12. Test hashCode()
    @Test
    public void testHashCode() {
        Object obj = new Object();
        POJONode pojoNode1 = new POJONode(obj);
        POJONode pojoNode2 = new POJONode(obj);
        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    // 13. Test toString()
    @Test
    public void testToString() {
        POJONode pojoNode1 = new POJONode(new Object());
        assertEquals("(POJONode [_value=java.lang.Object@52f4e6])", pojoNode1.toString());
    }

    // 14. Test binaryValue()
    @Test(expected = IOException.class)
    public void testBinaryValueThrowsIOException() throws IOException {
        POJONode pojoNode = new POJONode("Hello World");
        pojoNode.binaryValue();
    }

    // 15. Test binaryValue() with byte[]
    @Test
    public void testBinaryValueWithByteArray() {
        byte[] byteArray = new byte[]{1, 2, 3};
        POJONode pojoNode = new POJONode(byteArray);
        byte[] actualByteArray = pojoNode.binaryValue();
        assertArrayEquals(byteArray, actualByteArray);
    }
}