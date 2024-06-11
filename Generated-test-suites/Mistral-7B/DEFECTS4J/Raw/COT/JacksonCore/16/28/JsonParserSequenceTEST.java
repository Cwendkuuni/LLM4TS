package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

    

    @Test
    public void testConstructor() {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        Assert.assertNotNull(jsonParserSequence);
    }

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        Assert.assertNotNull(jsonParserSequence);
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        jsonParserSequence.close();
        parser1.close();
        parser2.close();
    }

    @Test
    public void testNextToken_NoToken() throws IOException, JsonParseException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = jsonParserSequence.nextToken();
        Assert.assertNull(token);
        parser1.nextToken();
        parser2.nextToken();
        token = jsonParserSequence.nextToken();
        Assert.assertNotNull(token);
    }

    @Test
    public void testNextToken_SameParser() throws IOException, JsonParseException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        parser1.nextToken();
        JsonToken token = jsonParserSequence.nextToken();
        Assert.assertSame(parser1.getCurrentToken(), token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_IOException() throws IOException, JsonParseException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        parser1.getCurrentStream().throwIOException();
        jsonParserSequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_UnexpectedChar() throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser1 = mapper.getFactory().createParser("{ \"foo\": 1 }");
        JsonParser parser2 = mapper.getFactory().createParser("[]");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        parser1.nextToken();
        jsonParserSequence.nextToken();
    }

    @Test
    public void testSwitchToNext() throws IOException, JsonParseException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        parser1.nextToken();
        Assert.assertTrue(jsonParserSequence.switchToNext());
        JsonToken token = jsonParserSequence.nextToken();
        Assert.assertSame(parser2.getCurrentToken(), token);
    }

    @Test
    public void testSwitchToNext_NoMoreParsers() throws IOException, JsonParseException {
        JsonParser parser1 = new BaseJsonParser();
        JsonParser parser2 = new BaseJsonParser();
        JsonParser parser3 = new BaseJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        Assert.assertFalse(jsonParserSequence.switchToNext());
    }

    
}