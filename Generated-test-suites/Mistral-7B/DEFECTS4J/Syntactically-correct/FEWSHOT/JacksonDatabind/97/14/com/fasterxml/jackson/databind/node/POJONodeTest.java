package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class POJONodeTest {

    @Test
    public void testPOJONode_Basic() {
        Object obj = new Object() {};
        POJONode pojoNode = new POJONode(obj);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, pojoNode.asToken());

        assertEquals("POJO", pojoNode.getClass().getSimpleName());
        assertEquals(JsonNodeType.POJO, pojoNode.getPojo().getClass().getAnnotation(JsonNodeType.class).value());

        assertEquals(obj, pojoNode.getPojo());

        assertEquals("POJO", pojoNode.toString());
    }

    @Test
    public void testPOJONode_Serialize() throws IOException {
        Object obj = new Object() {
            @JsonProperty
            public String getName() {
                return "test";
            }
        };
        POJONode pojoNode = new POJONode(obj);

        StringWriter sw = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(sw);
        pojoNode.serialize(gen, new SerializerProvider());
        gen.close();

        String json = sw.toString();
        JsonParser parser = new JsonFactory().getParser(new StringReader(json));
        JsonNode node = parser.readValueAsTree();

        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        JsonNode objNode = node.get("test");
        assertEquals(JsonNodeType.POJO, objNode.getNodeType());
        assertEquals("test", objNode.getPojo().getClass().getDeclaredMethod("getName").invoke(objNode.getPojo()));
    }

    @Test
    public void testPOJONode_Equals_Hashcode() {
        Object obj1 = new Object() {};
        Object obj2 = new Object() {};
        POJONode pojoNode1 = new POJONode(obj1);
        POJONode pojoNode2 = new POJONode(obj2);

        assertNotEquals(pojoNode1, pojoNode2);
        assertEquals(pojoNode1.hashCode(), pojoNode1.hashCode());
        assertEquals(pojoNode1.hashCode(), pojoNode1.hashCode());

        Object obj3 = obj1;
        POJONode pojoNode3 = new POJONode(obj3);
        assertEquals(pojoNode1, pojoNode3);
        assertEquals(pojoNode1.hashCode(), pojoNode3.hashCode());
    }

    @Test
    public void testPOJONode_BinaryValue() throws IOException {
        byte[] binaryData = new byte[] { 0x01, 0x02, 0x03 };
        POJONode pojoNode = new POJONode(binaryData);

        assertEquals(binaryData.length, pojoNode.asBinaryLength());
        assertArrayEquals(binaryData, pojoNode.binaryValue());
    }

    @Test(expected = IOException.class)
    public void testPOJONode_BinaryValue_Null() throws IOException {
        POJONode pojoNode = new POJONode(null);
        pojoNode.binaryValue();
    }

    @Test
    public void testPOJONode_StringValue() {
        POJONode pojoNode = new POJONode("test");

        assertEquals("test", pojoNode.asText());
        assertEquals("test", pojoNode.asText(null));
    }

    @Test
    public void testPOJONode_BooleanValue() {
        POJONode pojoNode = new POJONode(true);
        assertTrue(pojoNode.asBoolean(false));
    }

    @Test
    public void testPOJONode_IntValue() {
        POJONode pojoNode = new POJONode(123);
        assertEquals(123, pojoNode.asInt(0));
    }

    @Test
    public void testPOJONode_LongValue() {
        POJONode pojoNode = new POJONode(123L);
        assertEquals(123L, pojoNode.asLong(0L));
    }

    @Test
    public void testPOJONode_DoubleValue() {
        POJONode pojoNode = new POJONode(123.45);
        assertEquals(123.45, pojoNode.asDouble(0), 0.001);
    }
}