package com.fasterxml.jackson.databind.deser.std; // adjust the package name if necessary

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for JsonNodeDeserializer class.
 */
public class JsonNodeDeserializerTest {

    private JsonNodeFactory nodeFactory;
    private JsonParser testParser;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
        testParser = mapper.getFactory().createParser("{}");
    }

    @Test
    public void testGetNullValue() throws Exception {
        JsonNode result = JsonNodeDeserializer.instance.getNullValue(testParser.getDeserializationContext());
        assertEquals(JsonNode.Null.class, result.getClass());
    }

    @Test
    public void testDeserializeObject() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30}";
        testParser.readValueAsTree(json);
        ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(testParser, testParser.getDeserializationContext());
        assertEquals("John", result.get("name").asText());
        assertEquals(30, result.get("age").asInt());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        String json = "[1, 2, 3]";
        testParser.readValueAsTree(json);
        ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(testParser, testParser.getDeserializationContext());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testDeserializeAny() throws Exception {
        String json = "{\"name\": \"John\"}";
        testParser.readValueAsTree(json);
        JsonNode result = JsonNodeDeserializer.instance.deserializeAny(testParser, testParser.getDeserializationContext());
        assertTrue(result instanceof ObjectNode);
        assertEquals("John", ((ObjectNode) result).get("name").asText());

        json = "[1, 2, 3]";
        testParser.readValueAsTree(json);
        result = JsonNodeDeserializer.instance.deserializeAny(testParser, testParser.getDeserializationContext());
        assertTrue(result instanceof ArrayNode);
        assertEquals(3, ((ArrayNode) result).size());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidJson() throws Exception {
        testParser.readValueAsTree("{invalid: JSON"); // missing closing bracket
        JsonNodeDeserializer.instance.deserialize(testParser, testParser.getDeserializationContext());
    }

    @Test
    public void testJsonDeserializerFactory() throws Exception {
        JsonDeserializer<ObjectNode> objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertNotNull(objectDeserializer);
        assertNotNull(arrayDeserializer);
        assertEquals(ObjectNode.class, objectDeserializer.getValueClass());
        assertEquals(ArrayNode.class, arrayDeserializer.getValueClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testInvalidNodeType() throws Exception {
        testParser.readValueAsTree("{invalid: JSON}"); // missing closing bracket
        JsonNodeDeserializer.instance.deserialize(testParser, testParser.getDeserializationContext());
    }
}