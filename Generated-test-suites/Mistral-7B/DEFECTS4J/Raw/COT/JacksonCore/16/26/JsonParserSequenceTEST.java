package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser sequence;

    @Before
    public void setup() throws IOException {
        parser1 = new JsonTestParser();
        parser2 = new JsonTestParser();
        sequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParser() throws IOException {
        JsonToken token = sequence.nextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parser1.nextToken();
        Assert.assertSame(token, sequence.nextToken());
    }

    @Test
    public void testNextToken_SecondParser() throws IOException {
        JsonToken token = parser1.nextToken(); // Consume token from parser1

        token = sequence.nextToken();
        Assert.assertNotNull(token);
        Assert.assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parser2.nextToken();
        Assert.assertSame(token, sequence.nextToken());
    }

    @Test(expected = IOException.class)
    public void testNextToken_IOException_FirstParser() throws IOException {
        parser1.nextToken(); // Consume token from parser1
        parser1.nextToken(); // Throw IOException

        sequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseException_FirstParser() throws IOException {
        parser1.nextToken(); // Consume token from parser1
        parser1.nextToken(); // Throw JsonParseException

        sequence.nextToken();
    }

    @Test(expected = IOException.class)
    public void testNextToken_IOException_SecondParser() throws IOException {
        JsonToken token = parser1.nextToken(); // Consume token from parser1
        JsonToken tokenFromSecondParser = parser2.nextToken();

        parser2.nextToken(); // Throw IOException

        sequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseException_SecondParser() throws IOException {
        JsonToken token = parser1.nextToken(); // Consume token from parser1
        JsonToken tokenFromSecondParser = parser2.nextToken();

        parser2.nextToken(); // Throw JsonParseException

        sequence.nextToken();
    }

    @Test
    public void testClose() throws IOException {
        sequence.close();
        parser1.close();
        parser2.close();
    }

    private static class JsonTestParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return JsonToken.START_OBJECT;
        }

        @Override
        public void close() throws IOException {
            // Empty
        }
    }

    
    // Add more test cases as needed
    
}