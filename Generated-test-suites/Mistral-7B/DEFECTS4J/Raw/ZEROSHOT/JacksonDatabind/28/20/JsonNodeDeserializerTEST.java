package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonParser p = createParser("{}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser p = createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());

        ObjectNode obj = (ObjectNode) result;
        assertEquals("John", obj.get("name").asText());
        assertEquals(30, obj.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = createParser("[1, 2, 3]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());

        ArrayNode arr = (ArrayNode) result;
        assertEquals(3, arr.size());
        assertEquals(1, arr.get(0).asInt());
        assertEquals(2, arr.get(1).asInt());
        assertEquals(3, arr.get(2).asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        JsonParser p = createParser("{\"foo\": [1, 2, 3]}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
        deserializer.deserialize(p, new TestContext());
    }

    @Test
    public void testDeserializeDuplicateFields() throws IOException {
        JsonParser p = createParser("{\"name\": \"John\", \"name\": 30}");
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(ObjectNode.class);
        ObjectNode result = reader.readValue(p);

        assertEquals("John", result.get("name").asText());
    }

    @Test
    public void testDeserializeEmptyObject() throws IOException {
        JsonParser p = createParser("{}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());
        assertTrue(result instanceof ObjectNode);
        assertTrue(result.isNull());
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser p = createParser("[]");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());
        assertTrue(result instanceof ArrayNode);
        assertTrue(result.isNull());
    }

    @Test
    public void testDeserializeComplexJson() throws IOException {
        JsonParser p = createParser("{\"employees\": [{\"firstName\": \"John\", \"lastName\": \"Doe\"}, " +
                "{\"firstName\": \"Jane\", \"lastName\": \"Doe\"}, {\"firstName\": \"Bob\", \"lastName\": \"Smith\"}]}");
        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = deserializer.deserialize(p, new TestContext());

        ObjectNode obj = (ObjectNode) result;
        ArrayNode employees = (ArrayNode) obj.get("employees");
        assertEquals(3, employees.size());
        for (JsonNode employee : employees) {
            ObjectNode emp = (ObjectNode) employee;
            assertEquals(2, emp.size());
            String firstName = emp.get("firstName").asText();
            String lastName = emp.get("lastName").asText();
            assertTrue(firstName.contains("John") || firstName.contains("Jane") || firstName.contains("Bob"));
            assertTrue(lastName.contains("Doe") || lastName.contains("Smith"));
        }
    }

    private JsonParser createParser(String json) throws IOException {
        return new ObjectMapper().getFactory().createParser(json);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonFactory _getFactory() {
            return new JsonFactory();
        }

        @Override
        public JsonNodeFactory nodeFactory() {
            return new StandardJsonNodeFactory();
        }
    }
}