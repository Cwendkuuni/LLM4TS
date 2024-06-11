package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest extends BaseTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser parser3;
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() throws IOException {
        parser1 = createParser(new StringReader("{\"a\":1,\"b\":2}"));
        parser2 = createParser(new StringReader("{\"c\":3,\"d\":4}"));
        parser3 = createParser(new StringReader("{\"e\":5,\"f\":6}"));

        parserSequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
    }

    @After
    public void tearDown() throws IOException {
        parser1.close();
        parser2.close();
        parser3.close();
        parserSequence.close();
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser expected = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        JsonParser actual = parserSequence;

        assertSame(expected, actual);
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("a", parserSequence.getValueAsString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(1, parserSequence.getValueAsInt());

        // Test that the first parser is exhausted
        assertEquals(JsonToken.END_OBJECT, parser1.nextToken());
        assertFalse(parser1.nextToken() instanceof JsonToken);

        // Test that the next parser is active
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("b", parserSequence.getValueAsString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(2, parserSequence.getValueAsInt());

        // Test that the third parser is active
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals("c", parserSequence.getValueAsString());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parserSequence.nextToken());
        assertEquals(3, parserSequence.getValueAsInt());

        // Test that the sequence is exhausted
        assertNull(parserSequence.nextToken());
    }

    @Test(expected = IOException.class)
    public void testClosedParsers() throws IOException {
        JsonParser parser1 = createParser(new StringReader("{\"a\":1,\"b\":2}"));
        JsonParser parser2 = createParser(parser1);
        parser1.close();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser2 });
        parserSequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException {
        JsonParser parser1 = createParser(new StringReader("{\"a\":1"));
        JsonParser parser2 = createParser(new StringReader("\"b\":2}"));
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        parserSequence.nextToken();
    }

    @Test
    public void testContainedParsersCount() {
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        assertEquals(3, parserSequence.containedParsersCount());
    }
}