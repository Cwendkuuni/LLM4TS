package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.test.BaseTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonParserSequenceTest extends BaseTest {

    private JsonParser[] parsers;
    private JsonParserSequence parserSequence;

    @Before
    public void setUp() throws IOException {
        String json1 = "{\"name\":\"John\", \"age\":30}";
        String json2 = "{\"name\":\"Jane\", \"age\":25}";

        JsonFactory factory = new JsonFactory();
        JsonParser p1 = factory.createParser(new ByteArrayInputStream(json1.getBytes()));
        JsonParser p2 = factory.createParser(new ByteArrayInputStream(json2.getBytes()));

        parsers = new JsonParser[] { p1, p2 };
        parserSequence = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
    }

    @Test
    public void testNextToken_NormalCase() throws IOException {
        JsonToken token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        token = parserSequence.nextToken();
        assertNull(token);

        token = parserSequence.nextToken();
        assertNull(token);

        assertEquals(parsers.length, parserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_EmptyParser() throws IOException {
        JsonParser emptyParser = new NullJsonParser();
        JsonParser[] parsersWithEmpty = new JsonParser[] { parsers[0], emptyParser };
        JsonParserSequence parserSequenceWithEmpty = JsonParserSequence.createFlattened(parsers[0], parsersWithEmpty);

        JsonToken token = parserSequenceWithEmpty.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequenceWithEmpty.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = parserSequenceWithEmpty.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().toString());

        token = parserSequenceWithEmpty.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = parserSequenceWithEmpty.nextToken();
        assertNull(token);

        token = parserSequenceWithEmpty.nextToken();
        assertNull(token);

        assertEquals(parsers.length + 1, parserSequenceWithEmpty.containedParsersCount());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_UnexpectedEndOfStream_Exception() throws IOException {
        JsonParser p1 = parsers[0];
        JsonParser p2 = parsers[1];

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { p1, p2 });
        parserSequence.delegate = p1;
        parserSequence.nextToken(); // consume first token in p1
        parserSequence.nextToken(); // consume second token in p1

        parserSequence.nextToken(); // should consume next token in p2

        parserSequence.nextToken(); // should throw exception because p2 is at EOS
    }

    @Test
    public void testNextToken_UnexpectedEndOfStream_ContinueToNextParser() throws IOException {
        JsonParser p1 = parsers[0];
        JsonParser p2 = parsers[1];

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { p1, p2 });
        parserSequence.delegate = p1;
        parserSequence.nextToken(); // consume first token in p1
        parserSequence.nextToken(); // consume second token in p1

        JsonToken token = parserSequence.nextToken(); // should consume next token in p2
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = parserSequence.nextToken(); // should consume next token in p2
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().toString());

        token = parserSequence.nextToken(); // should consume next token in p2
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("Jane", token.getText().toString());

        token = parserSequence.nextToken(); // should consume next token in p2
        assertNotNull(token);
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().toString());

        token = parserSequence.nextToken(); // should consume next token in p2
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(25, token.getIntValue());

        token = parserSequence.nextToken(); // should consume next token in p2
        assertNull(token);

        token = parserSequence.nextToken(); // should consume next token in p1, which is null

        assertEquals(parsers.length, parserSequence.containedParsersCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDelegate_IllegalStateException() throws IOException {
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[0]);
        parserSequence.delegate = parserSequence;
    }

    @Test
    public void testClose() throws IOException {
        parserSequence.close();

        for (JsonParser p : parsers) {
            p.close();
        }

        assertTrue(parserSequence.isClosed());
        for (JsonParser p : parsers) {
            assertTrue(p.isClosed());
        }
    }
}

class NullJsonParser extends JsonParser {
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return null;
    }
}