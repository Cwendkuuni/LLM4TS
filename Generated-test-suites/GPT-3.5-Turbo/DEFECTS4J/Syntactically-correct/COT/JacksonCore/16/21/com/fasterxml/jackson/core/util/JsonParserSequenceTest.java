
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        JsonParser[] parsers = {};
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        try {
            sequence.close();
        } catch (IOException e) {
            Assert.fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testNextToken() {
        JsonParser[] parsers = {};
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        try {
            JsonToken token = sequence.nextToken();
            Assert.assertNull(token);
        } catch (IOException | JsonParseException e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateFlattened() {
        JsonParser first = null;
        JsonParser second = null;

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        Assert.assertNotNull(sequence);
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser[] parsers = {};

        JsonParserSequence sequence = new JsonParserSequence(parsers);
        int count = sequence.containedParsersCount();

        Assert.assertEquals(0, count);
    }
}
