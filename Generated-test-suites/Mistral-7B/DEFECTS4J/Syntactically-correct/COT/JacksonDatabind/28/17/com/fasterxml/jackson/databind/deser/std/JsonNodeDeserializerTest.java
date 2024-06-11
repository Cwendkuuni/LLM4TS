package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Test suite for JsonNodeDeserializer class
 */
public class JsonNodeDeserializerTest {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    @Test
    public void testGetNullValue() throws IOException {
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        JsonNode expectedResult = JsonNodeFactory.instance.nullNode();
        JsonNode actualResult = jsonNodeDeserializer.deserialize(createTestParser(), new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetNullValueDeprecated() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode expectedResult = JsonNodeFactory.instance.nullNode();
        JsonNode actualResult = jsonNodeDeserializer.getNullValue();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser testParser = createTestParser();
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode expectedResult = JsonNodeFactory.instance.objectNode();
        expectedResult.put("name", JsonNodeFactory.instance.textNode("John"));
        expectedResult.put("age", JsonNodeFactory.instance.numberNode(30));

        ObjectNode actualResult = (ObjectNode) objectDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser testParser = createTestParser();
        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        ArrayNode expectedResult = JsonNodeFactory.instance.arrayNode();
        expectedResult.add(JsonNodeFactory.instance.numberNode(1));
        expectedResult.add(JsonNodeFactory.instance.textNode("two"));

        ArrayNode actualResult = (ArrayNode) arrayDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonParser testParser = createTestParser(); // { "invalid": "json" }
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        jsonNodeDeserializer.deserialize(testParser, new DeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser testParser = createTestParser("[ ]");
        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        arrayDeserializer.deserialize(testParser, new DeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyObject() throws IOException {
        JsonParser testParser = createTestParser("{}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        objectDeserializer.deserialize(testParser, new DeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeDuplicateFields() throws IOException {
        JsonParser testParser = createTestParser("{\"name\": \"John\", \"name\": \"Jane\"}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        objectDeserializer._handleDuplicateField(testParser, new DeserializationContext(), JsonNodeFactory.instance, "name", JsonNodeFactory.instance.objectNode(), JsonNodeFactory.instance.textNode("John"), JsonNodeFactory.instance.textNode("Jane"));
    }

    @Test
    public void testDeserializeObjectWithEmptyArray() throws IOException {
        JsonParser testParser = createTestParser("{\"name\": \"John\", \"array\": []}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode expectedResult = JsonNodeFactory.instance.objectNode();
        expectedResult.put("name", JsonNodeFactory.instance.textNode("John"));
        expectedResult.put("array", JsonNodeFactory.instance.arrayNode());

        ObjectNode actualResult = (ObjectNode) objectDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeObjectWithEmptyObject() throws IOException {
        JsonParser testParser = createTestParser("{\"name\": \"John\", \"object\": {}}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode expectedResult = JsonNodeFactory.instance.objectNode();
        expectedResult.put("name", JsonNodeFactory.instance.textNode("John"));
        expectedResult.put("object", JsonNodeFactory.instance.objectNode());

        ObjectNode actualResult = (ObjectNode) objectDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeObjectWithSimpleValues() throws IOException {
        JsonParser testParser = createTestParser("{\"boolean\": true, \"number\": 1, \"string\": \"Hello\", \"null\": null}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode expectedResult = JsonNodeFactory.instance.objectNode();
        expectedResult.put("boolean", JsonNodeFactory.instance.booleanNode(true));
        expectedResult.put("number", JsonNodeFactory.instance.numberNode(1));
        expectedResult.put("string", JsonNodeFactory.instance.textNode("Hello"));
        expectedResult.put("null", JsonNodeFactory.instance.nullNode());

        ObjectNode actualResult = (ObjectNode) objectDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDeserializeObjectWithNestedObjectsAndArrays() throws IOException {
        JsonParser testParser = createTestParser("{\"name\": \"John\", \"obj\": {\"name\": \"Jane\", \"arr\": [\"apple\", \"banana\"]}}");
        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        ObjectNode expectedResult = JsonNodeFactory.instance.objectNode();
        ObjectNode expectedObj = JsonNodeFactory.instance.objectNode();
        expectedObj.put("name", JsonNodeFactory.instance.textNode("Jane"));
        ArrayNode expectedArr = JsonNodeFactory.instance.arrayNode();
        expectedArr.add(JsonNodeFactory.instance.textNode("apple"));
        expectedArr.add(JsonNodeFactory.instance.textNode("banana"));

        expectedResult.put("name", JsonNodeFactory.instance.textNode("John"));
        expectedResult.put("obj", expectedObj);
        expectedResult.put("arr", expectedArr);

        ObjectNode actualResult = (ObjectNode) objectDeserializer.deserialize(testParser, new DeserializationContext());
        assertEquals(expectedResult, actualResult);
    }

    private JsonParser createTestParser() throws IOException {
        return JSON_FACTORY.createParser(new StringReader(""));
    }

    private JsonParser createTestParser(String jsonString) throws IOException {
        return JSON_FACTORY.createParser(new StringReader(jsonString));
    }
}