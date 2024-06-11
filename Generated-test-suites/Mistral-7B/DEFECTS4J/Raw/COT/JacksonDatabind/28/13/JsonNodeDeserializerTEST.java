package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * Test cases for JsonNodeDeserializer class.
 */
public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode expected = JsonNodeFactory.instance.nullNode();
        JsonNode actual = deserializer.getNullValue(new TestJsonFactory().getParser());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNullValueDeprecated() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode expected = JsonNodeFactory.instance.nullNode();
        JsonNode actual = deserializer.getNullValue();
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("key1", JsonNodeFactory.instance.numberNode(1));
        expected.put("key2", JsonNodeFactory.instance.textNode("value2"));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_OBJECT
        parser.nextFieldName(); // consume "key1"
        parser.nextValue(); // consume 1
        parser.nextFieldName(); // consume "key2"
        parser.nextValue(); // consume "value2"
        parser.nextToken(); // consume END_OBJECT

        ObjectNode actual = (ObjectNode) deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeObjectInvalidJson() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume END_OBJECT (without any fields)
        deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(JsonNodeFactory.instance.numberNode(1));
        expected.add(JsonNodeFactory.instance.textNode("value2"));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_ARRAY
        parser.nextValue(); // consume 1
        parser.nextValue(); // consume "value2"
        parser.nextToken(); // consume END_ARRAY

        ArrayNode actual = (ArrayNode) deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeArrayInvalidJson() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume END_ARRAY (without any elements)
        deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
    }

    @Test
    public void testDeserializeAnyObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("key", JsonNodeFactory.instance.numberNode(1));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_OBJECT
        parser.nextFieldName(); // consume "key"
        parser.nextValue(); // consume 1
        parser.nextToken(); // consume END_OBJECT

        JsonNode actual = deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeAnyArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(JsonNodeFactory.instance.numberNode(1));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_ARRAY
        parser.nextValue(); // consume 1
        parser.nextToken(); // consume END_ARRAY

        JsonNode actual = deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAnyInvalidJsonObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_OBJECT
        parser.nextToken(); // consume END_OBJECT (without any fields)
        deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAnyInvalidJsonArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_ARRAY
        parser.nextToken(); // consume END_ARRAY (without any elements)
        deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("key", JsonNodeFactory.instance.numberNode(1));
        expected.put("key", JsonNodeFactory.instance.numberNode(2));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_OBJECT
        parser.nextFieldName(); // consume "key"
        parser.nextValue(); // consume 1
        parser.nextFieldName(); // consume "key"
        parser.nextValue(); // consume 2
        parser.nextToken(); // consume END_OBJECT

        ObjectNode actual = (ObjectNode) deserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeObjectWithDuplicateFieldError() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectDeserializer objectDeserializer = new ObjectDeserializer();
        TestDeserializationContext context = new TestDeserializationContext();
        context.putBoolean(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);

        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("key", JsonNodeFactory.instance.numberNode(1));

        JsonParser parser = new TestJsonFactory().getParser();
        parser.nextValue(); // consume START_OBJECT
        parser.nextFieldName(); // consume "key"
        parser.nextValue(); // consume 1
        parser.nextFieldName(); // consume "key"
        parser.nextValue(); // consume 2
        parser.nextToken(); // consume END_OBJECT

        try {
            deserializer.deserialize(parser, context, JsonNodeFactory.instance);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Duplicate field 'key' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled";
            String actualMessage = e.getMessage();
            assertTrue(expectedMessage.contains(actualMessage));
        }

        ObjectNode actual = (ObjectNode) objectDeserializer.deserialize(parser, new TestDeserializationContext(), JsonNodeFactory.instance);
        assertEquals(expected, actual);
    }
}

class TestJsonFactory extends JsonFactory {
    @Override
    public JsonParser createParser() {
        return new TestJsonParser();
    }
}

class TestJsonParser extends JsonParserBase {
    @Override
    public JsonToken nextToken() throws IOException {
        // Implement your custom parsing logic here
        // For testing purposes, just return a predefined sequence of JSON tokens
        return nextJsonToken();
    }

    private JsonToken nextJsonToken() {
        switch (tokenIndex++) {
            case 0:
                return JsonToken.START_OBJECT;
            case 1:
                return JsonToken.FIELD_NAME;
            case 2:
                return JsonToken.START_OBJECT;
            case 3:
                return JsonToken.FIELD_NAME;
            case 4:
                return JsonToken.VALUE_NUMBER_INT;
            case 5:
                return JsonToken.FIELD_NAME;
            case 6:
                return JsonToken.VALUE_NUMBER_INT;
            case 7:
                return JsonToken.END_OBJECT;
            case 8:
                return JsonToken.END_OBJECT;
            default:
                throw new JsonMappingException("No more JSON tokens");
        }
    }

    private int tokenIndex = 0;
}