package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * Test file for JsonParserSequence class
 */
public class JsonParserSequenceTest {

    

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence jsonParserSequence;

    @BeforeClass
    public static void setupClass() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            parser1 = objectMapper.getFactory().createParser(new ByteArrayInputStream(
                    "{\"name\": \"John\", \"age\": 30}".getBytes()));
            parser2 = objectMapper.getFactory().createParser(new ByteArrayInputStream(
                    "{\"name\": \"Jane\", \"age\": 25}".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() {
        jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    @Test
    public void testConstructorWithTwoParsers() {
        assertEquals("Expected two parsers", 2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParserReturnsToken() throws IOException {
        JsonToken token = jsonParserSequence.nextToken();
        assertNotNull("Expected a token", token);
    }

    @Test
    public void testNextToken_FirstParserReturnsNull_SecondParserReturnsToken()
            throws IOException {
        JsonToken token = null;
        while ((token = jsonParserSequence.nextToken()) == null) {}
        JsonToken nextToken = jsonParserSequence.nextToken();
        assertNotNull("Expected a token", nextToken);
    }

    @Test(expected = JsonParseException.class)
    public void testSwitchToNext_NoMoreParsers() throws IOException {
        jsonParserSequence.switchToNext();
        jsonParserSequence.nextToken();
        jsonParserSequence.switchToNext();
        jsonParserSequence.nextToken();
        jsonParserSequence.switchToNext();
    }

    @Test
    public void testSwitchToNext_SwitchToFirstParser() throws IOException {
        jsonParserSequence.switchToNext();
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals("Expected token from parser 1", JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testSwitchToNext_SwitchToSecondParser() throws IOException {
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals("Expected token from parser 2", JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        parser1.close();
        parser2.close();
    }

    
}