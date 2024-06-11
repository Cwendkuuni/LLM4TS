package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.util.JsonParserSequence;

import java.io.IOException;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser thirdParser;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() throws IOException {
        firstParser = new ParserMinimalBase() {};
        secondParser = new ParserMinimalBase() {};
        thirdParser = new ParserMinimalBase() {};
        jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
    }

    

    // Test basic functionality of JsonParserSequence
    @Test
    public void testBasicFunctionality() throws IOException {
        Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        // Add more assertions for other expected tokens
    }

    // Test containedParsersCount() method
    @Test
    public void testContainedParsersCount() {
        Assert.assertEquals(3, jsonParserSequence.containedParsersCount());
    }

    // Test nextToken() when no more parsers
    @Test(expected = IOException.class)
    public void testNextTokenNoMoreParsers() throws IOException {
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
    }

    // Test switchToNext() method with no more parsers
    @Test(expected = IOException.class)
    public void testSwitchToNextNoMoreParsers() throws IOException {
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
    }

    // Test exception handling when parsing with incorrect order of parsers
    @Test(expected = IllegalStateException.class)
    public void testIncorrectOrderParsers() throws IOException {
        JsonParserSequence incorrectOrderJsonParserSequence = JsonParserSequence.createFlattened(thirdParser, secondParser, firstParser);
        incorrectOrderJsonParserSequence.nextToken();
    }

    
}