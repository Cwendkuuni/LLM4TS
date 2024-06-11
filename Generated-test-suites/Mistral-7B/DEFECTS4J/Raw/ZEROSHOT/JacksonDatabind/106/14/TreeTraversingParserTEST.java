package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_Array() throws Exception {
        String json = "[1, 2, 3, 4, 5]";
        JsonParser parser = new ObjectMapper().treeAsTokens(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.getCodec().readTree(json), parser.getCodec());

        JsonToken token = null;
        while ((token = treeParser.nextToken()) != null) {
            switch (token) {
                case START_ARRAY:
                    assertTrue(token.isStartStructureElement());
                    break;
                case VALUE_NUMBER_INT:
                    int value = treeParser.getIntValue();
                    assertEquals(value, token.asInt());
                    break;
                case END_ARRAY:
                    assertTrue(token.isEndStructureElement());
                    break;
                default:
                    fail("Unexpected token: " + token);
            }
        }

        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testTreeTraversingParser_Object() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30}";
        JsonParser parser = new ObjectMapper().treeAsTokens(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.getCodec().readTree(json), parser.getCodec());

        JsonToken token = null;
        while ((token = treeParser.nextToken()) != null) {
            switch (token) {
                case START_OBJECT:
                    assertTrue(token.isStartStructureElement());
                    break;
                case FIELD_NAME:
                    String fieldName = treeParser.getCurrentName();
                    assertEquals("name", fieldName);
                    break;
                case VALUE_STRING:
                    String value = treeParser.getText();
                    assertEquals("John", value);
                    break;
                case START_OBJECT:
                    // nested object should be treated as a new container
                    break;
                case FIELD_NAME:
                    fieldName = treeParser.getCurrentName();
                    assertEquals("age", fieldName);
                    break;
                case VALUE_NUMBER_INT:
                    value = Integer.toString(treeParser.getIntValue());
                    assertEquals("30", value);
                    break;
                case END_OBJECT:
                    assertTrue(token.isEndStructureElement());
                    break;
                case END_ARRAY:
                    assertTrue(token.isEndStructureElement());
                    break;
                default:
                    fail("Unexpected token: " + token);
            }
        }

        assertTrue(treeParser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJson() throws Exception {
        String json = "{1,2}";
        JsonParser parser = new ObjectMapper().treeAsTokens(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.getCodec().readTree(json), parser.getCodec());

        JsonToken token = null;
        while ((token = treeParser.nextToken()) != null) {
            // This should fail as the JSON is invalid
            treeParser.getIntValue();
        }

        fail("Expected JsonParseException, but none was thrown.");
    }
}