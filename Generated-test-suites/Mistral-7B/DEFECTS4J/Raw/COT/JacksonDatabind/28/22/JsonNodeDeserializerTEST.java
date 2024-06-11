package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.*;

/**
 * Test class for JsonNodeDeserializer
 */
public class JsonNodeDeserializerTest {

    /**
     * Test method for {@link JsonNodeDeserializer#getDeserializer(Class)}
     */
    @Test
    public void testGetDeserializer() {
        JsonDeserializer<?> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<?> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonDeserializer<?> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        assertNotNull(objectDeserializer);
        assertInstanceOf(ObjectDeserializer.class, objectDeserializer);

        assertNotNull(arrayDeserializer);
        assertInstanceOf(ArrayDeserializer.class, arrayDeserializer);

        assertNotNull(jsonNodeDeserializer);
        assertInstanceOf(JsonNodeDeserializer.class, jsonNodeDeserializer);
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserialize(JsonParser, DeserializationContext)}
     */
    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30}");

        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) objectDeserializer.deserialize(parser, new DeserializationContext());

        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserialize(JsonParser, DeserializationContext)}
     */
    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[-1, 0, 1]");

        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) arrayDeserializer.deserialize(parser, new DeserializationContext());

        assertEquals(-1, result.get(0).asInt());
        assertEquals(0, result.get(1).asInt());
        assertEquals(1, result.get(2).asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserialize(JsonParser, DeserializationContext)}
     */
    @Test
    public void testDeserializeAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("42");

        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = jsonNodeDeserializer.deserialize(parser, new DeserializationContext());

        assertEquals(42, result.asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeObject(JsonParser, DeserializationContext, JsonNodeFactory)}
     */
    @Test
    public void testDeserializeObjectWithFactory() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\", \"age\":30}");
        JsonNodeFactory nodeFactory = mapper.getFactory().getNodeFactory();

        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = objectDeserializer.deserializeObject(parser, new DeserializationContext(), nodeFactory);

        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeArray(JsonParser, DeserializationContext, JsonNodeFactory)}
     */
    @Test
    public void testDeserializeArrayWithFactory() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[-1, 0, 1]");
        JsonNodeFactory nodeFactory = mapper.getFactory().getNodeFactory();

        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = arrayDeserializer.deserializeArray(parser, new DeserializationContext(), nodeFactory);

        assertEquals(-1, result.get(0).asInt());
        assertEquals(0, result.get(1).asInt());
        assertEquals(1, result.get(2).asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeAny(JsonParser, DeserializationContext, JsonNodeFactory)}
     */
    @Test
    public void testDeserializeAnyWithFactory() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("42");
        JsonNodeFactory nodeFactory = mapper.getFactory().getNodeFactory();

        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, new DeserializationContext(), nodeFactory);

        assertEquals(42, result.asInt());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeObject(JsonParser, DeserializationContext)}
     * with empty object
     */
    @Test
    public void testDeserializeEmptyObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");

        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = (ObjectNode) objectDeserializer.deserialize(parser, new DeserializationContext());

        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeArray(JsonParser, DeserializationContext)}
     * with empty array
     */
    @Test
    public void testDeserializeEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");

        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode result = (ArrayNode) arrayDeserializer.deserialize(parser, new DeserializationContext());

        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link JsonNodeDeserializer#deserializeObject(JsonParser, DeserializationContext, JsonNodeFactory)}
     * with duplicate keys
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectWithDuplicateKeys() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\", \"name\":\"Jane\"}");

        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode result = objectDeserializer.deserializeObject(parser, new DeserializationContext(), null);

        fail("Expected JsonMappingException, but no exception was thrown.");
    }
}