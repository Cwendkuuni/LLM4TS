package com.fasterxml.jackson.core.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.JsonParserDelegate;
import com.fasterxml.jackson.core.io.SerializedString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for {@link JsonParserSequence}
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstruction() {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattened() {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_sequentialParsing() throws IOException {
        JsonParser firstParser = createMockParserWithSequence("{\"a\":1}");
        JsonParser secondParser = createMockParserWithSequence("{\"b\":2}");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("a", token.getValueAsString());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, token.getIntValue());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("b", token.getValueAsString());

        token = parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(2, token.getIntValue());

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testNextToken_switchingParsers() throws IOException {
        JsonParser firstParser = createMockParserWithSequence("{\"a\":1}");
        JsonParser secondParser = createMockParserWithSequence("{\"b\":2}");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken().getValueAsString());
        assertEquals("a", parserSequence.nextToken().getValueAsString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(1, parserSequence.nextToken().getIntValue());

        parserSequence.switchToNext();

        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken().getValueAsString());
        assertEquals("b", parserSequence.nextToken().getValueAsString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(2, parserSequence.nextToken().getIntValue());

        assertNull(parserSequence.nextToken());
    }

    @Test(expected = IOException.class)
    public void testNextToken_exception_firstParser() throws IOException {
        JsonParser firstParser = createMockParserWithException();
        JsonParser secondParser = createMockParserWithSequence("{\"b\":2}");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken();
    }

    @Test(expected = IOException.class)
    public void testNextToken_exception_secondParser() throws IOException {
        JsonParser firstParser = createMockParserWithSequence("{\"a\":1}");
        JsonParser secondParser = createMockParserWithException();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();

        parserSequence.nextToken();
    }

    @Test
    public void testSwitchToNext_noMoreParsers() {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        parserSequence.switchToNext();
        assertFalse(parserSequence.switchToNext());
    }

    private JsonParser createMockParser() {
        return new JsonParserDelegateBaseMock() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }
        };
    }

    private JsonParser createMockParserWithSequence(String json) {
        return new JsonParserDelegateBaseMock() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return parseJsonToken(json);
            }
        };
    }

    private JsonParser createMockParserWithException() {
        return new JsonParserDelegateBaseMock() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                throw new IOException("Mock exception");
            }
        };
    }

    private JsonToken parseJsonToken(String json) throws IOException {
        StringResource jsonResource = new StringResource(json);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(jsonResource);
        JsonToken token = jsonParser.nextToken();
        jsonParser.close();
        return token;
    }

    static class StringResource extends SerializedString {
        public StringResource(String source) {
            super(source);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static class JsonParserDelegateBaseMock extends JsonParserDelegate {
        @Override
        public void close() throws IOException {
            // Empty implementation
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return null;
        }
    }
}