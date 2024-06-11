package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetDeserializer() throws Exception {
        JsonNode node1 = mapper.readValue(createJsonResource("/empty.json"), JsonNode.class);
        JsonDeserializer<?> deserializer1 = JsonNodeDeserializer.getDeserializer(node1.getClass());
        assertSame(JsonNodeDeserializer.class, deserializer1.getClass());

        ObjectNode node2 = mapper.createObjectNode();
        JsonDeserializer<?> deserializer2 = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertSame(ObjectDeserializer.class, deserializer2.getClass());

        ArrayNode node3 = mapper.createArrayNode();
        JsonDeserializer<?> deserializer3 = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertSame(ArrayDeserializer.class, deserializer3.getClass());
    }

    @Test
    public void testDeserializeNullValue() throws Exception {
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonParser p = mapper.getFactory().createParser("null");
        assertEquals(NullNode.getInstance(), deserializer.deserialize(p, null));
    }

    @Test
    public void testDeserializeObject() throws Exception {
        JsonParser p = mapper.getFactory().createParser(createJsonResource("/object.json"));
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(p, null);

        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals("name1", objNode.get("name1").asText());
        assertEquals("value1", objNode.get("value1").asText());
        assertEquals("name2", objNode.get("name2").asText());
        assertEquals("value2", objNode.get("value2").asText());
        assertEquals(JsonNodeType.NULL, objNode.get("nullValue").getNodeType());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        JsonParser p = mapper.getFactory().createParser(createJsonResource("/array.json"));
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(p, null);

        assertEquals(JsonNodeType.ARRAY, node.getNodeType());
        ArrayNode arrayNode = (ArrayNode) node;
        assertEquals(2, arrayNode.size());
        assertEquals(JsonNodeType.NUMBER, arrayNode.get(0).getNodeType());
        assertEquals(JsonNodeType.OBJECT, arrayNode.get(1).getNodeType());

        ObjectNode objNode = (ObjectNode) arrayNode.get(1);
        assertEquals("name1", objNode.get("name1").asText());
        assertEquals("value1", objNode.get("value1").asText());
    }

    @Test
    public void testDeserializeAny() throws Exception {
        JsonParser p = mapper.getFactory().createParser(createJsonResource("/any.json"));
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getInstance();
        JsonNode node = deserializer.deserialize(p, null);

        assertEquals(JsonNodeType.OBJECT, node.getNodeType());
        ObjectNode objNode = (ObjectNode) node;
        assertEquals("name1", objNode.get("name1").asText());
        assertEquals("value1", objNode.get("value1").asText());
        assertEquals("name2", objNode.get("name2").asText());
        assertEquals("value2", objNode.get("value2").asText());
        assertEquals(JsonNodeType.NULL, objNode.get("nullValue").getNodeType());
        assertEquals(JsonNodeType.NUMBER, objNode.get("number").getNodeType());
        assertEquals(JsonNodeType.ARRAY, objNode.get("array").getNodeType());
        assertEquals(JsonNodeType.STRING, objNode.get("string").getNodeType());
        assertEquals(JsonNodeType.BOOLEAN, objNode.get("boolean").getNodeType());
    }
}