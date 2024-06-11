package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonParserSequenceTest {
    // List all public methods and their signatures

    private JsonParser[] jsonParsers;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setup() {
        JsonParser firstParser = new SimpleJsonParser(new ByteArrayInputStream("[]".getBytes()));
        JsonParser secondParser = new SimpleJsonParser(new ByteArrayInputStream("{}".getBytes()));
        jsonParsers = new JsonParser[]{firstParser, secondParser};
        jsonParserSequence = new JsonParserSequence(jsonParsers);
    }

    @Test
    public void testJsonParserSequenceConstructor() {
        Assert.assertArrayEquals(jsonParsers, jsonParserSequence._parsers);
        Assert.assertEquals(jsonParsers.length, jsonParserSequence.containedParsersCount());
        Assert.assertEquals(jsonParsers[0], jsonParserSequence.delegate);
        Assert.assertEquals(1, jsonParserSequence._nextParser);
    }

    @Test
    public void testNextTokenBasic() throws IOException {
        JsonToken token = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token.getId());

        token = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NULL, token.getId());

        jsonParserSequence.nextToken(); // consume null

        token = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token.getId());

        jsonParserSequence.nextToken(); // consume {}

        token = jsonParserSequence.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token.getId());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        jsonParserSequence.nextToken(); // consume [
        jsonParserSequence.nextToken(); // consume null

        Assert.assertTrue(jsonParserSequence.switchToNext());

        JsonToken token = jsonParserSequence.delegate.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token.getId());

        jsonParserSequence.nextToken(); // consume {}

        Assert.assertTrue(jsonParserSequence.switchToNext());

        token = jsonParserSequence.delegate.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token.getId());

        Assert.assertFalse(jsonParserSequence.switchToNext());
    }

    @Test(expected = JsonParseException.class)
    public void testIOExceptionOnFirstParser() throws IOException {
        JsonParser firstParser = new ThrowingJsonParser();
        JsonParser secondParser = new SimpleJsonParser(new ByteArrayInputStream("{}".getBytes()));
        JsonParser[] jsonParsers = new JsonParser[]{firstParser, secondParser};
        JsonParserSequence jsonParserSequence = new JsonParserSequence(jsonParsers);

        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();

        jsonParserSequence.switchToNext();

        jsonParserSequence.nextToken(); // should throw exception here
    }

    @Test(expected = JsonParseException.class)
    public void testIOExceptionOnSecondParser() throws IOException {
        JsonParser firstParser = new SimpleJsonParser(new ByteArrayInputStream("[]".getBytes()));
        JsonParser secondParser = new ThrowingJsonParser();
        JsonParser[] jsonParsers = new JsonParser[]{firstParser, secondParser};
        JsonParserSequence jsonParserSequence = new JsonParserSequence(jsonParsers);

        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();

        jsonParserSequence.switchToNext();

        jsonParserSequence.nextToken(); // should throw exception here
    }
}

class SimpleJsonParser extends BaseJsonParser {
    public SimpleJsonParser(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return null;
    }
}

class ThrowingJsonParser extends BaseJsonParser {
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        throw new IOException("Test IOException");
    }
}


public class JsonParserSequenceTest {
    // test methods here
}

