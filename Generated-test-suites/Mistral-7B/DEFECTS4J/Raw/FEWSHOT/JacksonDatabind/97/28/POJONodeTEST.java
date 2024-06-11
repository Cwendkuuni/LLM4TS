package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testGetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    @Test
    public void testAsToken() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());
    }

    @Test
    public void testBinaryValue() throws IOException {
        POJONode pojoNode = new POJONode(new byte[0]);
        byte[] binaryValue = pojoNode.binaryValue();
        assertArrayEquals(new byte[0], binaryValue);

        pojoNode = new POJONode("testString");
        assertNull(pojoNode.binaryValue());
    }

    @Test
    public void testAsText() {
        POJONode pojoNode = new POJONode("testString");
        assertEquals("testString", pojoNode.asText());

        pojoNode = new POJONode(null);
        assertEquals("null", pojoNode.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode pojoNode = new POJONode("testString");
        assertEquals("testString", pojoNode.asText("defaultValue"));

        pojoNode = new POJONode(null);
        assertEquals("defaultValue", pojoNode.asText("defaultValue"));
    }

    @Test
    public void testAsBoolean() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));

        pojoNode = new POJONode(false);
        assertFalse(pojoNode.asBoolean(true));

        pojoNode = new POJONode(new Boolean(true));
        assertTrue(pojoNode.asBoolean(false));

        pojoNode = new POJONode(new Boolean(false));
        assertFalse(pojoNode.asBoolean(true));

        pojoNode = new POJONode(null);
        assertFalse(pojoNode.asBoolean(true));
        assertFalse(pojoNode.asBoolean(false));
    }

    @Test
    public void testAsInt() {
        POJONode pojoNode = new POJONode(1);
        assertEquals(1, pojoNode.asInt(0));

        pojoNode = new POJONode(new Integer(2));
        assertEquals(2, pojoNode.asInt(0));

        pojoNode = new POJONode(null);
        assertEquals(0, pojoNode.asInt(0));
    }

    @Test
    public void testAsLong() {
        POJONode pojoNode = new POJONode(1L);
        assertEquals(1L, pojoNode.asLong(0L));

        pojoNode = new POJONode(new Long(2L));
        assertEquals(2L, pojoNode.asLong(0L));

        pojoNode = new POJONode(null);
        assertEquals(0L, pojoNode.asLong(0L));
    }

    @Test
    public void testAsDouble() {
        POJONode pojoNode = new POJONode(1.0);
        assertEquals(1.0, pojoNode.asDouble(0.0), 0.001);

        pojoNode = new POJONode(new Double(2.0));
        assertEquals(2.0, pojoNode.asDouble(0.0), 0.001);

        pojoNode = new POJONode(null);
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
        POJONode pojoNode = new POJONode(new Object());

        pojoNode.serialize(gen, new SerializerProvider() {
            @Override
            public void defaultSerializeNull(JsonGenerator g) throws IOException {
                g.writeNull();
            }

            @Override
            public void defaultSerializeValue(JsonGenerator g, SerializerProvider p, Object v) throws IOException {
                g.writeValue(v);
            }
        });

        gen.flush();
        gen.close();

        String expectedJson = "{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":{}}";
        assertTrue(expectedJson.equals(readTreeFromString(mapper.readTree(new StringReader(new StringReader(System.out.toString()).toString()))). toString()));
    }

    @Test
    public void testGetPojo() {
        POJONode pojoNode = new POJONode(new Object());
        assertEquals(pojoNode.getPojo().getClass(), Object.class);
    }

    @Test
    public void testEquals() {
        POJONode pojoNode1 = new POJONode(new Object());
        POJONode pojoNode2 = new POJONode(new Object());

        assertTrue(pojoNode1.equals(pojoNode2));
        assertTrue(pojoNode2.equals(pojoNode1));

        POJONode pojoNode3 = new POJONode(new Object());
        POJONode pojoNode4 = new POJONode(new Object());
        POJONode pojoNode5 = new POJONode(new Object());

        assertNotEquals(pojoNode1, pojoNode3);
        assertNotEquals(pojoNode2, pojoNode4);
        assertNotEquals(pojoNode3, pojoNode5);

        POJONode pojoNode6 = new POJONode(null);
        assertNotEquals(pojoNode1, pojoNode6);
        assertNotEquals(pojoNode6, pojoNode2);

        POJONode pojoNode7 = new POJONode("testString");
        assertNotEquals(pojoNode1, pojoNode7);
        assertNotEquals(pojoNode7, pojoNode2);
    }

    @Test
    public void testHashCode() {
        POJONode pojoNode1 = new POJONode(new Object());
        POJONode pojoNode2 = new POJONode(new Object());

        int hashCode1 = pojoNode1.hashCode();
        int hashCode2 = pojoNode2.hashCode();

        assertEquals(hashCode1, hashCode2);

        POJONode pojoNode3 = new POJONode(new Object());
        int hashCode3 = pojoNode3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        POJONode pojoNode4 = new POJONode("testString");
        int hashCode4 = pojoNode4.hashCode();

        assertNotEquals(hashCode1, hashCode4);

        POJONode pojoNode5 = new POJONode(null);
        int hashCode5 = pojoNode5.hashCode();

        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        POJONode pojoNode1 = new POJONode(new Object());
        assertEquals("(POJONode)", pojoNode1.toString());

        POJONode pojoNode2 = new POJONode("testString");
        assertEquals("(POJONode (raw value 'testString'))", pojoNode2.toString());

        POJONode pojoNode3 = new POJONode(new byte[0]);
        assertEquals("(POJONode (binary value of 0 bytes))", pojoNode3.toString());

        POJONode pojoNode4 = new POJONode(null);
        assertEquals("(POJONode)", pojoNode4.toString());
    }

    private JsonNode readTreeFromString(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new StringReader(jsonString));
    }
}