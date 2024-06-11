package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.TestBase;

public class JsonNodeDeserializerTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeNullValue() throws Exception {
        JsonParser p = mapper.getFactory().createParser("null");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonNode result = deserializer.deserialize(p, null);
        assertTrue(result instanceof NullNode);
    }

    @Test
    public void testDeserializeEmptyObject() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        JsonParser p = mapper.getFactory().createParser("[]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ArrayNode result = (ArrayNode) deserializer.deserialize(p, null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeSimpleObject() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testDeserializeSimpleArray() throws Exception {
        JsonParser p = mapper.getFactory().createParser("[-1, 0, 1]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ArrayNode result = (ArrayNode) deserializer.deserialize(p, null);
        assertEquals(3, result.size());
        assertEquals(-1, result.get(0).asInt());
        assertEquals(0, result.get(1).asInt());
        assertEquals(1, result.get(2).asInt());
    }

    @Test
    public void testDeserializeObjectWithEmptyArrayProperty() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"hobbies\": []}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertEquals("John", result.get("name").asText());
        assertNotNull(result.get("hobbies"));
        assertTrue(result.get("hobbies").isEmpty());
    }

    @Test
    public void testDeserializeObjectWithArrayProperty() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"hobbies\": [\"reading\", \"painting\"]}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertEquals("John", result.get("name").asText());
        ArrayNode hobbies = (ArrayNode) result.get("hobbies");
        assertEquals(2, hobbies.size());
        assertEquals("reading", hobbies.get(0).asText());
        assertEquals("painting", hobbies.get(1).asText());
    }

    @Test
    public void testDeserializeObjectWithNullProperty() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"hobbies\": null}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertEquals("John", result.get("name").asText());
        assertNull(result.get("hobbies"));
    }

    @Test
    public void testDeserializeComplexObject() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30,\"address\": {\"street\": \"123 Main St\", \"city\": \"Anytown\", \"state\": \"CA\", \"zip\": 12345}}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
        ObjectNode address = (ObjectNode) result.get("address");
        assertEquals("123 Main St", address.get("street").asText());
        assertEquals("Anytown", address.get("city").asText());
        assertEquals("CA", address.get("state").asText());
        assertEquals(12345, address.get("zip").asInt());
    }

    @Test
    public void testDeserializeComplexArray() throws Exception {
        JsonParser p = mapper.getFactory().createParser("[1, 2, 3, [4, 5], {\"name\": \"John\"}]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        ArrayNode result = (ArrayNode) deserializer.deserialize(p, null);
        assertEquals(5, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
        ArrayNode nestedArray = (ArrayNode) result.get(3);
        assertEquals(2, nestedArray.size());
        assertEquals(4, nestedArray.get(0).asInt());
        assertEquals(5, nestedArray.get(1).asInt());
        ObjectNode nestedObject = (ObjectNode) result.get(4);
        assertEquals("John", nestedObject.get("name").asText());
    }

    @Test
    public void testDeserializeObjectWithCustomClassId() throws Exception {
        JsonParser p = mapper.getFactory().createParser("{\"@class\": \"MyClass\", \"name\": \"John\"}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        JsonDeserializer<ObjectNode> myClassDeserializer = new JsonDeserializer<ObjectNode>() {
            @Override
            public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                ObjectNode result = super.deserialize(p, ctxt);
                result.set("@class", TextNode.valueOf("MyClass"));
                return result;
            }
        };
        JsonNode node = deserializer.deserialize(p, JsonNodeDeserializer.getClassId(), myClassDeserializer);
        assertTrue(node instanceof ObjectNode);
        ObjectNode obj = (ObjectNode) node;
        assertEquals("MyClass", obj.get("@class").asText());
        assertEquals("John", obj.get("name").asText());
    }
}