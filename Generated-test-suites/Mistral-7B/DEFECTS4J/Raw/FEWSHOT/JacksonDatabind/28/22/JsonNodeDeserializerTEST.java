package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserialize(p, null);
        ObjectNode expected = mapper.readValueAs(ObjectNode.class, p);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[\"apple\", \"banana\", \"orange\"]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserialize(p, null);
        ArrayNode expected = mapper.readValueAs(ArrayNode.class, p);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"number\": 42}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserialize(p, null);
        ObjectNode expected = mapper.readValueAs(ObjectNode.class, p);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30, \"name\": \"Jane\"}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        try {
            deserializer.deserializeObject(p, null, null);
            fail("Expected JsonMappingException with message: Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserializeArrayWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[\"apple\", null, \"orange\"]");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserializeArray(p, null, null);
        ArrayNode expected = mapper.readValueAs(ArrayNode.class, p);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithNestedObjects() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"address\": {\"street\": \"123 Main St\", \"city\": \"Anytown\"}}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserializeObject(p, null, null);
        ObjectNode expected = mapper.readValueAs(ObjectNode.class, p);

        ObjectNode address = (ObjectNode) expected.get("address");
        assertEquals(address.get("street").asText(), actual.get("address").get("street").asText());
        assertEquals(address.get("city").asText(), actual.get("address").get("city").asText());
    }

    @Test
    public void testDeserializeObjectWithNestedArrays() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"hobbies\": [\"swimming\", \"reading\", \"programming\"]}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserializeObject(p, null, null);
        ObjectNode expected = mapper.readValueAs(ObjectNode.class, p);

        ArrayNode hobbies = (ArrayNode) expected.get("hobbies");
        assertEquals(hobbies.size(), actual.get("hobbies").size());
        for (int i = 0; i < hobbies.size(); i++) {
            assertEquals(hobbies.get(i).asText(), actual.get("hobbies").get(i).asText());
        }
    }

    @Test
    public void testDeserializeObjectWithEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"hobbies\": []}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        JsonNode actual = deserializer.deserializeObject(p, null, null);
        ObjectNode expected = mapper.readValueAs(ObjectNode.class, p);

        ArrayNode hobbies = expected.get("hobbies");
        assertEquals(hobbies.size(), actual.get("hobbies").size());
        assertTrue(actual.get("hobbies").isMissingNode());
    }

    @Test
    public void testDeserializeObjectWithEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"\": {}}");
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

        try {
            deserializer.deserializeObject(p, null, null);
            fail("Expected JsonMappingException with message: Unexpected token (END_OBJECT), expected START_OBJECT");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }
}