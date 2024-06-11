package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        // Test empty sequence
        JsonParserSequence emptySequence = new JsonParserSequence(new JsonParser[0]);
        assertNull(emptySequence.nextToken());
        emptySequence.close();

        // Test single parser sequence
        JsonParser parser1 = createMockParser("{'name': 'John'}:");
        JsonParserSequence singleParserSequence = new JsonParserSequence(new JsonParser[] { parser1 });
        JsonToken token = singleParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        singleParserSequence.close();
        parser1.close();

        // Test sequence with multiple parsers
        JsonParser parser2 = createMockParser("{'age': 30}");
        JsonParser parser3 = createMockParser("{'city': 'New York'}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText());
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("city", token.getText());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("New York", token.getText());
        token = sequence.nextToken();
        assertNull(token);
        sequence.close();
        parser1.close();
        parser2.close();
        parser3.close();
    }

    private JsonParser createMockParser(String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        return parser;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() {
        new JsonParserSequence(new JsonParser[1]);
    }
}