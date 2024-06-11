package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        // create two json parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();

        // create flattened json parser sequence
        JsonParserSequence jsonParserSeq = JsonParserSequence.createFlattened(parser1, parser2);

        // assert that the active parsers count is 2
        assertEquals(2, jsonParserSeq.containedParsersCount());
    }

    @Test
    public void testNextToken() {
        // create two json parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();

        // create flattened json parser sequence
        JsonParserSequence jsonParserSeq = JsonParserSequence.createFlattened(parser1, parser2);

        try {
            // call nextToken method on json parser sequence and assert that it is not null
            assertNotNull(jsonParserSeq.nextToken());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClose() {
        // create two json parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();

        // create flattened json parser sequence
        JsonParserSequence jsonParserSeq = JsonParserSequence.createFlattened(parser1, parser2);

        try {
            // call close method on json parser sequence
            jsonParserSeq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // assert that the parsers are closed
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testContainedParsersCount() {
        // create two json parsers
        JsonParser parser1 = new JsonParser();
        JsonParser parser2 = new JsonParser();

        // create flattened json parser sequence
        JsonParserSequence jsonParserSeq = JsonParserSequence.createFlattened(parser1, parser2);

        // assert that the contained parsers count is 2
        assertEquals(2, jsonParserSeq.containedParsersCount());
    }
}