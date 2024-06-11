
package com.fasterxml.jackson.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        // Arrange
        JsonParser parser1 = new MockJsonParser();
        JsonParser parser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Act
        sequence.close();

        // Assert
        assertTrue(((MockJsonParser) parser1).isCloseCalled());
        assertTrue(((MockJsonParser) parser2).isCloseCalled());
    }

    @Test
    public void testNextToken_SuccessfullyParsesNextToken() throws IOException {
        // Arrange
        JsonParser parser1 = new StringJsonParser("[1,2,3]");
        JsonParser parser2 = new StringJsonParser("{\"key\": \"value\"}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Act
        JsonToken token1 = sequence.nextToken();
        JsonToken token2 = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.START_ARRAY, token1);
        assertEquals(JsonToken.FIELD_NAME, token2);
        assertTrue(sequence.switchToNext());
        assertEquals('1', sequence.getText().charAt(0));
    }

    @Test
    public void testNextToken_SequenceEndsWithNull() throws IOException {
        // Arrange
        JsonParser parser1 = new StringJsonParser("{\"key\": \"value\"}");
        JsonParser parser2 = new StringJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Act
        JsonToken token1 = sequence.nextToken();
        JsonToken token2 = sequence.nextToken();

        // Assert
        assertEquals(JsonToken.FIELD_NAME, token1);
        assertNull(token2);
    }

    @Test
    public void testContainedParsersCount_ReturnsCorrectCount() {
        // Arrange
        JsonParser parser1 = new StringJsonParser("{\"key\": \"value\"}");
        JsonParser parser2 = new StringJsonParser("[1,2,3]");
        List<JsonParser> parsers = new ArrayList<>();
        parsers.add(parser1);
        parsers.add(parser2);

        // Act
        JsonParserSequence sequence = new JsonParserSequence(parsers.toArray(new JsonParser[0]));

        // Assert
        assertEquals(2, sequence.containedParsersCount());
    }

    private static class MockJsonParser extends JsonParser {

        private boolean closeCalled = false;

        public boolean isCloseCalled() {
            return closeCalled;
        }

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }

        // Other abstract methods should be implemented, but they are not relevant for this test

    }

    private static class StringJsonParser extends JsonParser {
    
        private final String json;
        private int index = 0;

        public StringJsonParser(String json) {
            this.json = json;
        }

        @Override
        public void close() throws IOException {
            // No close logic needed for this test
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (index >= json.length()) {
                return null;
            }
            char currentChar = json.charAt(index++);
            switch (currentChar) {
                case '{':
                    return JsonToken.START_OBJECT;
                case '}':
                    return JsonToken.END_OBJECT;
                case '[':
                    return JsonToken.START_ARRAY;
                case ']':
                    return JsonToken.END_ARRAY;
                case ':':
                    return JsonToken.FIELD_NAME;
                case '1':
                    return JsonToken.VALUE_NUMBER_INT;
                case '2':
                    return JsonToken.VALUE_NUMBER_INT;
                case '3':
                    return JsonToken.VALUE_NUMBER_INT;
                default:
                    return null;
            }
        }

        // Other abstract methods should be implemented, but they are not relevant for this test

    }

}

