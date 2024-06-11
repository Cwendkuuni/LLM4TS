package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setup() throws IOException {
        firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        secondParser = new JsonFactory().createParser("{\"key2\": \"value2\"}");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testNextToken_BasicUseCase() throws IOException, JsonParseException {
        JsonToken token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value1", token.getText());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key2", token.getText());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value2", token.getText());

        token = jsonParserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_EdgeCases() throws IOException, JsonParseException {
        // Test when parsers contain empty JSON
        firstParser = new JsonFactory().createParser("");
        secondParser = new JsonFactory().createParser("");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token = jsonParserSequence.nextToken();
        assertNull(token);

        // Test when parsers contain JSON with different structures
        firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        secondParser = new JsonFactory().createParser("[]");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token1 = jsonParserSequence.nextToken();
        assertNotNull(token1);
        assertEquals(JsonToken.START_OBJECT, token1.getId());

        JsonToken token2 = jsonParserSequence.nextToken();
        assertNotNull(token2);
        assertEquals(JsonToken.FIELD_NAME, token2.getId());
        assertEquals("key1", token2.getText());

        JsonToken token3 = jsonParserSequence.nextToken();
        assertNull(token3);

        // Test when parsers contain JSON with the same structure but different values
        firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        secondParser = new JsonFactory().createParser("{\"key1\": \"value2\"}");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token4 = jsonParserSequence.nextToken();
        assertNotNull(token4);
        assertEquals(JsonToken.FIELD_NAME, token4.getId());
        assertEquals("key1", token4.getText());

        JsonToken token5 = jsonParserSequence.nextToken();
        assertNotNull(token5);
        assertEquals(JsonToken.VALUE_STRING, token5.getId());

        String expectedValue1 = jsonParserSequence.toString();
        assertEquals("\"value1\"", expectedValue1);

        JsonToken token6 = jsonParserSequence.nextToken();
        assertNotNull(token6);
        assertEquals(JsonToken.VALUE_STRING, token6.getId());

        String expectedValue2 = jsonParserSequence.toString();
        assertEquals("\"value2\"", expectedValue2);

        JsonToken token7 = jsonParserSequence.nextToken();
        assertNull(token7);
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_ExceptionScenarios() throws IOException, JsonParseException {
        firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        secondParser = new JsonFactory().createParser("{\"key1\": \"value2\"}");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        // Test when one parser throws a JsonParseException
        JsonParserException exception = expectThrows(JsonParseException.class, () -> {
            jsonParserSequence.nextToken();
            jsonParserSequence.nextToken(); // Consume first token
            jsonParserSequence.nextToken(); // Consume second token
            jsonParserSequence.nextToken(); // Throw exception
        });
        assertEquals("Unexpected character ('a') at line 1, column 2", exception.getMessage());
    }

    @Test
    public void testClose() throws IOException {
        firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        secondParser = new JsonFactory().createParser("{\"key2\": \"value2\"}");
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        jsonParserSequence.close();
        firstParser.close();
        secondParser.close();
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser firstParser = new JsonFactory().createParser("{\"key1\": \"value1\"}");
        JsonParser secondParser = new JsonFactory().createParser("{\"key2\": \"value2\"}");
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
}