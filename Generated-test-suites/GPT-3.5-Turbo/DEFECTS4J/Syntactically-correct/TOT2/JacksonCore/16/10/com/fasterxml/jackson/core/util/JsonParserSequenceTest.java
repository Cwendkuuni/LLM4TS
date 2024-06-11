package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertNotNull(sequence);
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
    }

    @Test
    public void testSwitchToNext() {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonParser first = new JsonFactory().createParser("{}");
        JsonParser second = new JsonFactory().createParser("[]");

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        sequence.close();
    }
}