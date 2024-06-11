package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = createTestParser();
        JsonParser parser2 = createTestParser();
        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("value1", token.getText());

        sequence.close();
        parser1.close();
        parser2.close();
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = createTestParser();
        JsonParser parser2 = createTestParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(2, sequence.containedParsersCount());

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText());
        assertEquals(JsonToken.VALUE_NULL, sequence.nextToken().getId());

        sequence.switchToNext();

        token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER, token.getId());
        assertEquals(1.0, token.getDoubleValue(), 0.0);

        sequence.close();
        parser1.close();
        parser2.close();
    }

    @Test
    public void testNullParser() throws IOException {
        JsonParser parser1 = createTestParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, null });

        assertEquals(1, sequence.containedParsersCount());
        assertFalse(sequence.switchToNext());

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText());
        assertEquals(JsonToken.VALUE_NULL, sequence.nextToken().getId());

        sequence.close();
        parser1.close();
    }

    @Test
    public void testEmptyParsersArray() throws IOException {
        JsonParser sequence = new JsonParserSequence(new JsonParser[0]);

        assertNull(sequence.delegate);
        assertEquals(0, sequence.containedParsersCount());
    }

    @Test
    public void testSequenceOfSequences() throws IOException {
        JsonParser parser1 = createTestParser();
        JsonParser parser2 = createTestParser();
        JsonParser parser3 = createTestParser();
        JsonParser sequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParser sequence2 = new JsonParserSequence(new JsonParser[] { parser2, parser3 });
        JsonParser sequence3 = JsonParserSequence.createFlattened(sequence1, sequence2);

        assertEquals(3, sequence3.containedParsersCount());

        JsonToken token = sequence3.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        token = sequence3.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        token = sequence3.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key1", token.getText());
        assertEquals(JsonToken.VALUE_NULL, sequence3.nextToken().getId());
        token = sequence3.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("key2", token.getText());
        assertEquals(JsonToken.VALUE_NUMBER, sequence3.nextToken().getId());
        assertEquals(2.0, sequence3.nextToken().getDoubleValue(), 0.0);

        sequence3.close();
        parser1.close();
        parser2.close();
        parser3.close();
    }

    private JsonParser createTestParser() throws IOException {
        String json = "{\"key1\":null,\"key2\":1}";
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new StringReader(json));
    }
}