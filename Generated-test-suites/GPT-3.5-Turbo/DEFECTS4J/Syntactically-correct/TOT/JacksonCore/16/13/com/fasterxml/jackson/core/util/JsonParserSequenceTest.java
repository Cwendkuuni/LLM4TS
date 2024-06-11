
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        parsers = new JsonParser[2];
        // Mocking up two JsonParsers for testing
        parsers[0] = new JsonParser() {
            @Override
            public void close() throws IOException {
                // TODO Auto-generated method stub
            }

            @Override
            public int containedParsersCount() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int nextIntValue(int defaultValue) throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String nextTextValue() throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return null;
            }
        };
        
        parsers[1] = new JsonParser() {
            @Override
            public void close() throws IOException {
                // TODO Auto-generated method stub
            }

            @Override
            public int containedParsersCount() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int nextIntValue(int defaultValue) throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String nextTextValue() throws IOException, JsonParseException {
                // TODO Auto-generated method stub
                return null;
            }
        };
        
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws IOException {
        // TODO: Implement test
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // TODO: Implement test
    }

    @Test
    public void testContainedParsersCount() {
        int expected = 2;
        int actual = jsonParserSequence.containedParsersCount();
        assertEquals(expected, actual);
    }
}
