package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import java.io.IOException;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser1 = jsonFactory.createParser("{}");
        JsonParser parser2 = jsonFactory.createParser("{}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        sequence.close();
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser1 = jsonFactory.createParser("{}");
        JsonParser parser2 = jsonFactory.createParser("{}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser1 = jsonFactory.createParser("{}");
        JsonParser parser2 = jsonFactory.createParser("{}");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        int count = sequence.containedParsersCount();
        assertEquals(2, count);
    }
}