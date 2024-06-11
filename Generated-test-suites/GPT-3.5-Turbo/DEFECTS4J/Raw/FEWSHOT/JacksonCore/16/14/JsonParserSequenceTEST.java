
package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonParserStub parser1 = new JsonParserStub();
        JsonParserStub parser2 = new JsonParserStub();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        sequence.close();

        assertEquals(1, parser1.closeInvocationCount);
        assertEquals(1, parser2.closeInvocationCount);
    }

    @Test
    public void testNextTokenWithOneParser() throws IOException, JsonParseException {
        JsonParserStub parser = new JsonParserStub();
        parser.nextToken = JsonToken.START_OBJECT;
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser);

        JsonToken token = sequence.nextToken();

        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals(1, parser.nextTokenInvocationCount);
    }

    @Test
    public void testNextTokenWithMultipleParsers() throws IOException, JsonParseException {
        JsonParserStub parser1 = new JsonParserStub();
        parser1.nextToken = null;
        JsonParserStub parser2 = new JsonParserStub();
        parser2.nextToken = JsonToken.START_OBJECT;
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = sequence.nextToken();

        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals(1, parser1.nextTokenInvocationCount);
        assertEquals(1, parser2.nextTokenInvocationCount);
    }

    @Test
    public void testNextTokenWithNoParsers() throws IOException, JsonParseException {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[0]);

        JsonToken token = sequence.nextToken();

        assertNull(token);
    }

    @Test
    public void testContainedParsersCount() {
        JsonParserStub parser1 = new JsonParserStub();
        JsonParserStub parser2 = new JsonParserStub();
        JsonParserStub parser3 = new JsonParserStub();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        int count = sequence.containedParsersCount();

        assertEquals(3, count);
    }

    private static class JsonParserStub extends JsonParser {

        int closeInvocationCount = 0;
        int nextTokenInvocationCount = 0;
        JsonToken nextToken;

        @Override
        public void close() throws IOException {
            closeInvocationCount++;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            nextTokenInvocationCount++;
            return nextToken;
        }
    }
}
