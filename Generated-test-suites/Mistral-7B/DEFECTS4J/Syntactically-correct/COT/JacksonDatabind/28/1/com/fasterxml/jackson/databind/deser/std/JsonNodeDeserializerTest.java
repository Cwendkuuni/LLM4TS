package com.fasterxml.jackson.databind.deser.std; // Import necessary packages

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    

    @Test
    public void testJsonNodeDeserializer_getDeserializer() throws Exception {
        JsonDeserializer<?> objectNodeDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<?> arrayNodeDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonDeserializer<?> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);

        assertNotNull(objectNodeDeserializer);
        assertNotNull(arrayNodeDeserializer);
        assertNotNull(jsonNodeDeserializer);

        assertTrue(objectNodeDeserializer instanceof ObjectDeserializer);
        assertTrue(arrayNodeDeserializer instanceof ArrayDeserializer);
        assertFalse(objectNodeDeserializer instanceof ArrayDeserializer);
        assertFalse(arrayNodeDeserializer instanceof ObjectDeserializer);
        assertFalse(jsonNodeDeserializer instanceof ObjectDeserializer);
        assertFalse(jsonNodeDeserializer instanceof ArrayDeserializer);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedObject = mapper.readTree("{\"name\": \"John\", \"age\": 30}");

        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        JsonNode actualObject = jsonNodeDeserializer.deserialize(mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}"), null);

        assertEquals(expectedObject, actualObject);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializer_deserializeObject_InvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("{invalid_json}");
        jsonNodeDeserializer.deserialize(parser, null);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode expectedArray = mapper.readTree("[1, 2, 3]");

        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.instance;
        JsonNode actualArray = jsonNodeDeserializer.deserialize(mapper.getFactory().createParser("[1, 2, 3]"), null);

        assertEquals(expectedArray, actualArray);
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonNodeDeserializer_deserializeArray_InvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("[invalid_json]");
        jsonNodeDeserializer.deserialize(parser, null);
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_String() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\"}");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isTextual());
        assertEquals("John", result.asText());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_Number() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("42");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isNumber());
        assertEquals(42, result.asInt());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_Boolean() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("true");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isBoolean());
        assertTrue(result.asBoolean());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_Null() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("null");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isNull());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_Object() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isObject());
        assertEquals("John", result.path("name").asText());
        assertEquals(30, result.path("age").asInt());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeAny_Array() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        JsonNode result = jsonNodeDeserializer.deserializeAny(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isArray());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testJsonNodeDeserializer_deserializeObject_DuplicateKey() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;

        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"name\": 30}");
        JsonNode result = jsonNodeDeserializer.deserializeObject(parser, null, null);

        assertNotNull(result);
        assertTrue(result.isObject());
        assertEquals(2, result.size());
        assertEquals("John", result.path("name").get(0).asText());
        assertEquals(30, result.path("name").get(1).asInt());
    }

    
}