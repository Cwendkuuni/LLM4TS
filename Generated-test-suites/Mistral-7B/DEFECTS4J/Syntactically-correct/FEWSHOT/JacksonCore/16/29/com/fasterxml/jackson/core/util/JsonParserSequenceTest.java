package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException, JsonProcessingException {
        JsonParser firstParser = createTestParser("{\"name\":\"John\"}");
        JsonParser secondParser = createTestParser("{\"age\":30}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = sequence.nextToken();
        assertEquals("name", token.getName());
        assertEquals("John", token.getValueAsString());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getValueAsString());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        assertNull(sequence.nextToken());

        // Test with null input
        JsonParser nullParser = null;
        JsonParser nullSequence = JsonParserSequence.createFlattened(firstParser, nullParser);
        assertNull(nullSequence.nextToken());
    }

    @Test
    public void testSwitchToNext() throws IOException, JsonProcessingException {
        JsonParser firstParser = createTestParser("{\"name\":\"John\"}");
        JsonParser secondParser = createTestParser("{\"age\":30}");
        JsonParser[] parsers = {firstParser, secondParser};
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        sequence.switchToNext();
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getValueAsString());

        sequence.switchToNext();
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        sequence.switchToNext();
        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        assertNull(sequence.nextToken());
    }

    @Test
    public void testContainedParsersCount() throws IOException, JsonProcessingException {
        JsonParser firstParser = createTestParser("{\"name\":\"John\"}");
        JsonParser secondParser = createTestParser("{\"age\":30}");
        JsonParser[] parsers = {firstParser, secondParser};
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        int containedParsersCount = sequence.containedParsersCount();
        assertEquals(2, containedParsersCount);
    }

    private JsonParser createTestParser(String json) throws IOException {
        StringResource jsonResource = new StringResource(json);
        return new JsonFactory().createParser(jsonResource);
    }
}