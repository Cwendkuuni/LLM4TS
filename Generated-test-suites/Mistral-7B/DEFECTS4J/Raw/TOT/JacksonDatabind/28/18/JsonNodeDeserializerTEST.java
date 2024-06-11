package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.getFactory().createParser("null"), null);
        assertNull(node);
    }

    @Test
    public void testDeserializeObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree("{\"name\": \"John\", \"age\": 25}");
        JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 25}"), ObjectNode.class);
        assertEquals(expected, node);
    }

    @Test
    public void testDeserializeArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expected = mapper.readTree("[1, 2, 3]");
        JsonNode node = JsonNodeDeserializer.instance.deserialize(mapper.getFactory().createParser("[1, 2, 3]"), ArrayNode.class);
        assertEquals(expected, node);
    }

    @Test
    public void testDeserializeDuplicateField() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode expected = mapper.readTree("{\"name\": \"John\", \"age\": 25, \"name\": \"Jane\"}");
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 25, \"name\": \"Jane\"}");
        JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ObjectNode.class);
        ObjectNode actual = (ObjectNode) node;
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get("name").asText(), actual.get("name").get(0).asText());
        assertEquals(expected.get("name").get(1).asText(), actual.get("name").get(1).asText());
        assertEquals(expected.get("age").asInt(), actual.get("age").asInt());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeDuplicateFieldWithErrorHandling() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapperWithErrorHandling = new ObjectMapper();
        mapperWithErrorHandling.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
        ObjectNode expected = mapper.readTree("{\"name\": \"John\", \"age\": 25, \"name\": \"Jane\"}");
        JsonParser p = mapperWithErrorHandling.getFactory().createParser("{\"name\": \"John\", \"age\": 25, \"name\": \"Jane\"}");
        JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ObjectNode.class);
    }

    @Test
    public void testDeserializeUnexpectedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode expected = mapper.readTree("{\"name\": \"John\", \"age\": 25, \"city\": \"New York\"}");
        JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 25, \"city\": \"New York\"}");
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode node = deserializer.deserialize(p, null);
        assertEquals(expected, node);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnexpectedArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.getFactory().getParser("{\"name\": \"John\", \"age\": 25}");
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeUnexpectedString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.getFactory().getParser("{\"name\": \"John\", \"age\": 25}");
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
        deserializer.deserialize(parser, null);
    }

    @Test
    public void testDeserializeUnexpectedNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.getFactory().getParser("42");
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(int.class);
        int actual = deserializer.deserialize(parser, null).asInt();
        assertEquals(42, actual);
    }

    @Test   
    public void testDeserializeUnexpectedBoolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectParser parser = mapper.getFactory().getParser("true");
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(boolean.class);
        boolean actual = deserializer.deserialize(parser, null).asBoolean();
        assertTrue(actual);
    }
}