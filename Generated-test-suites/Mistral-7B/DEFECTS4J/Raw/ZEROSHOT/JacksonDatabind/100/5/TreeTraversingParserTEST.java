package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_EmptyJsonNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueNull());
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ValueNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.value("Test Value"));

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Test Value", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ObjectNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.objectNode());

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("field1", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("value1", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("field2", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ArrayNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.arrayNode(JsonNode.value("value1"), JsonNode.value("value2")));

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("value1", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("value2", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_EmptyArrayNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.arrayNode());
        parser.nextToken();
        parser.nextToken(); // this should throw an exception
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJsonNode() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new Object()));
        parser.nextToken(); // this should throw an exception
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_SkipChildren() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.objectNode(JsonNode.fieldName("field1"), JsonNode.value("value1"), JsonNode.fieldName("field2"), JsonNode.arrayNode(JsonNode.value("value3"), JsonNode.value("value4"))));

        parser.nextToken();
        parser.nextToken(); // skip field1

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentName());
        assertEquals("field2", parser.getCurrentName());

        parser.nextToken(); // skip field2

        parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        parser.skipChildren();

        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testTreeTraversingParser_CloseParser() throws JsonParseException, IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.objectNode());
        parser.nextToken();
        parser.close();

        try {
            parser.nextToken();
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }
}