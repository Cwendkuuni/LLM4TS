package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.util.MinimalJacksonContext;
import com.fasterxml.jackson.core.util.JsonParserSequence;

import java.io.IOException;

public class JsonParserSequenceTest {

    private JsonParser firstParser, secondParser, sequenceParser;
    private ParserMinimalBase context;

    @Before
    public void setup() throws IOException {
        context = new MinimalJacksonContext();
        firstParser = new ParserMinimalBase(context);
        secondParser = new ParserMinimalBase(context);
        sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testNextToken_BothParsersReturnNull_ReturnsNull() throws IOException {
        Assert.assertNull(sequenceParser.nextToken());
        Assert.assertNull(sequenceParser.nextToken());
        Assert.assertNull(sequenceParser.nextToken());
    }

    @Test
    public void testNextToken_FirstParserReturnsToken_ReturnsThatToken() throws IOException {
        firstParser.nextValue("{ \"foo\": 1 }");
        Assert.assertEquals(JsonToken.START_OBJECT, sequenceParser.nextToken());
    }

    @Test
    public void testNextToken_SecondParserReturnsToken_ReturnsThatToken() throws IOException {
        firstParser.nextValue("{ \"foo\": 1 }");
        firstParser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, sequenceParser.nextToken());
        Assert.assertEquals("\"foo\":", sequenceParser.getCurrentName());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_FirstParserThrowsException_ThrowsThatException() throws IOException {
        firstParser.nextValue("{ \"foo\": 1 }");
        firstParser.nextToken();
        firstParser.handleUnexpectedValue(null, null);
        sequenceParser.nextToken();
    }

    @Test
    public void testNextToken_BothParsersReturnSameToken_ReturnsThatToken() throws IOException {
        firstParser.nextValue("{ \"foo\": 1 }");
        firstParser.nextToken();
        secondParser.nextValue("{ \"foo\": 1 }");
        secondParser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, sequenceParser.nextToken());
        Assert.assertEquals(1, sequenceParser.getValueAsInt());
    }

    @Test
    public void testNextToken_BothParsersAtEnd_ReturnsNull() throws IOException {
        firstParser.close();
        secondParser.close();
        Assert.assertNull(sequenceParser.nextToken());
        Assert.assertNull(sequenceParser.nextToken());
    }

    @Test
    public void testContainsParsersCount() throws IOException {
        JsonParserSequence sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        Assert.assertEquals(2, sequence.containedParsersCount());
    }
}