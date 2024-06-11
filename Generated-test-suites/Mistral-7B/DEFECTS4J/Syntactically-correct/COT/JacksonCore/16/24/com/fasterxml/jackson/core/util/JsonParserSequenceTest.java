package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {
    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser parser3;
    private JsonParser[] parsers;
    private JsonParserSequence parserSequence;

    @Before
    public void setup() throws IOException {
        parser1 = new TestJsonParser(new TestStream("[]"));
        parser2 = new TestJsonParser(new TestStream("{}"));
        parser3 = new TestJsonParser(new TestStream("[1, 2, 3]"));
        parsers = new JsonParser[] { parser1, parser2, parser3 };
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testConstructor() {
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] {});
        assertArrayEquals(new JsonParser[0], parserSequence._parsers);
        assertFalse(parserSequence.isActive());

        parserSequence = new JsonParserSequence(new JsonParser[] { parser1 });
        assertArrayEquals(new JsonParser[] { parser1 }, parserSequence._parsers);
        assertTrue(parserSequence.isActive());
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = new TestJsonParser(new TestStream("[]"));
        JsonParser parser2 = new TestJsonParser(new TestStream("{}"));
        JsonParser parser3 = new TestJsonParser(new TestStream("[1, 2, 3]"));

        JsonParser parserSequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
        JsonParser parserSequence2 = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        assertArrayEquals(parserSequence1._parsers, parserSequence2._parsers);
    }

    @Test
    public void testClose() throws IOException {
        parserSequence.close();
        for (JsonParser parser : parserSequence._parsers) {
            parser.close();
        }
    }

    @Test
    public void testContainsActiveParsersCount() {
        assertFalse(parserSequence.isActive());
        assertFalse(parserSequence.isActive());
        parserSequence.addFlattenedActiveParsers(Arrays.asList(parser1, parser2, parser3));
        assertTrue(parserSequence.isActive());
        assertEquals(3, parserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken token;

        // Test first parser
        token = parserSequence.nextToken();
        assertNull(token);

        token = parser1.nextToken();
        assertNull(token);
        assertEquals(JsonToken.START_ARRAY, parser1.nextToken().getTokenId());

        // Test second parser
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getTokenId());

        // Test third parser
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getTokenId());
        assertEquals(1, token.getIntValue());

        // Test fourth parser
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getTokenId());
        assertEquals(2, token.getIntValue());

        // Test fifth parser
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getTokenId());
        assertEquals(3, token.getIntValue());

        // Test sixth parser
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenException() throws IOException, JsonParseException {
        parserSequence.switchToNext();
        parserSequence.switchToNext();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken(); // should throw JsonParseException
    }

    /**
     * Test helper class for testing JsonParser.
     */
    static class TestJsonParser extends JsonParser {
        TestStream stream;

        public TestJsonParser(TestStream stream) {
            this.stream = stream;
        }

        @Override
        public void close() throws IOException {
            stream.close();
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return stream.nextToken();
        }
    }

    /**
     * Test helper class for testing JsonParser.nextToken() method.
     */
    static class TestStream implements Closeable {
        String content;
        int index;

        public TestStream(String content) {
            this.content = content;
        }

        @Override
        public void close() throws IOException {
        }

        public JsonToken nextToken() throws IOException {
            if (index >= content.length()) {
                return null;
            }

            char c = content.charAt(index);
            index++;

            if (c == '[') {
                return JsonToken.START_ARRAY;
            } else if (c == '{') {
                return JsonToken.START_OBJECT;
            } else if (Character.isDigit(c)) {
                int startIndex = index;
                while (Character.isDigit(content.charAt(index))) {
                    index++;
                }
                return new NumberToken(Integer.parseInt(content.substring(startIndex, index)));
            }

            throw new IOException("Unexpected character '" + c + "'");
        }
    }
}