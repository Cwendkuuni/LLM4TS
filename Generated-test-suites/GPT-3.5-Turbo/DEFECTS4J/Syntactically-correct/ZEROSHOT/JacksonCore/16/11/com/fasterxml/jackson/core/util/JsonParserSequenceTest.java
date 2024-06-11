package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParserSequence sequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        JsonParser firstParser = new JsonParser() {
            @Override
            public void close() {
            }

            @Override
            public JsonToken nextToken() throws JsonParseException {
                return null;
            }
        };
        JsonParser secondParser = new JsonParser() {
            @Override
            public void close() {
            }

            @Override
            public JsonToken nextToken() throws JsonParseException {
                return null;
            }
        };

        parsers = new JsonParser[] { firstParser, secondParser };
        sequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws Exception {
        sequence.close();
        for (JsonParser parser : parsers) {
            Assert.assertTrue(parser.isClosed());
        }
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken token = sequence.nextToken();
        Assert.assertNull(token);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        int count = sequence.containedParsersCount();
        Assert.assertEquals(parsers.length, count);
    }

}