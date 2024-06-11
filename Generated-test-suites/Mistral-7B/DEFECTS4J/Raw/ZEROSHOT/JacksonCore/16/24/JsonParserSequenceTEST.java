package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;

/**
 * Test class for JsonParserSequence.
 */
public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() throws IOException {
        String json1 = "{\"name\": \"Jan\", \"age\": 30}";
        parser1 = new JsonFactory().createParser(new ByteArrayInputStream(json1.getBytes()));

        String json2 = "{\"city\": \"Berlin\", \"country\": \"Germany\"}";
        parser2 = new JsonFactory().createParser(new ByteArrayInputStream(json2.getBytes()));

        parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testContainedParsersCount() {
        int containedParsersCount = parserSequence.containedParsersCount();
        assertEquals(2, containedParsersCount);
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        JsonToken token;

        // Test the first parser
        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // Test the second parser
        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("Jan", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        // Test the first parser again
        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.END_OBJECT, token.getId());

        // Test the second parser
        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("city", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("Berlin", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("country", token.asString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("Germany", token.asString());

        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonProcessingException.class)
    public void testIllegalStateException() throws IOException {
        JsonToken token = parserSequence.nextToken();
        parserSequence.nextToken(); // Should throw an exception
        token = parserSequence.nextToken();
    }
}