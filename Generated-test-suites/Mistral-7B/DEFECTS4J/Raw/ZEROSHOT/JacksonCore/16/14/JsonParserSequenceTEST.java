package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test class for JsonParserSequence.
 */
public class JsonParserSequenceTest {

    /**
     * Test method for constructor.
     * @throws IOException
     */
    @Test
    public void testJsonParserSequence() throws IOException {
        // Test construction with single parser
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        assertEquals(parser, sequence.getDelegate());
        assertEquals(1, sequence.containedParsersCount());

        // Test construction with multiple parsers
        JsonParser parser1 = new ObjectMapper().getFactory().createParser("{}");
        JsonParser parser2 = new ObjectMapper().getFactory().createParser("[]");
        JsonParserSequence sequence2 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(parser1, sequence2.getDelegate());
        assertEquals(2, sequence2.containedParsersCount());

        // Test creationFlattened method
        JsonParser parser3 = new ObjectMapper().getFactory().createParser("{}");
        JsonParserSequence seqParser1 = new JsonParserSequence(parser3);
        JsonParser parser4 = new ObjectMapper().getFactory().createParser("[]");
        JsonParserSequence seqParser2 = new JsonParserSequence(parser4);
        JsonParserSequence sequence3 = JsonParserSequence.createFlattened(seqParser1, seqParser2);
        assertEquals(parser3, sequence3.getDelegate());
        assertEquals(2, sequence3.containedParsersCount());
    }

    /**
     * Test method for nextToken() with single parser.
     * @throws IOException
     * @throws JsonParseException
     */
    @Test
    public void testNextTokenWithSingleParser() throws IOException, JsonParseException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{ \"name\": \"John\" }");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().trim());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText().trim());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());
    }

    /**
     * Test method for nextToken() with multiple parsers.
     * @throws IOException
     * @throws JsonParseException
     */
    @Test
    public void testNextTokenWithMultipleParsers() throws IOException, JsonParseException {
        JsonParser parser1 = new ObjectMapper().getFactory().createParser("{ \"name\": \"John\" }");
        JsonParser parser2 = new ObjectMapper().getFactory().createParser("{ \"age\": 30 }");

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        assertEquals("John", sequence.nextValue().textValue());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText().trim());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getLongValue());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        // test that second parser is skipped
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken().getId());
    }

    /**
     * Test method for closing.
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{ \"name\": \"John\" }");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        sequence.close();
        parser.close();
    }

    /**
     * Test method for switchToNext() method.
     * @throws IOException
     * @throws JsonParseException
     */
    @Test
    public void testSwitchToNext() throws IOException, JsonParseException {
        JsonParser parser1 = new ObjectMapper().getFactory().createParser("{ \"name\": \"John\" }");
        JsonParser parser2 = new ObjectMapper().getFactory().createParser("{ \"age\": 30 }");

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // switchToNext should return false when all parsers have been tested
        assertFalse(sequence.switchToNext());

        // close the first parser
        parser1.close();

        // switchToNext should now return true and delegate to parser2
        assertTrue(sequence.switchToNext());
        JsonToken token2 = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token2.getId());
        assertEquals("age", sequence.nextFieldName());
        assertEquals(30, sequence.nextValue().asInt());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken().getId());
    }

    /**
     * Test method for nextValue().
     * @throws IOException
     * @throws JsonParseException
     */
    @Test
    public void testNextValue() throws IOException, JsonParseException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{ \"name\": \"John\" }");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        JsonNode value = sequence.nextValue();
        assertEquals("John", value.textValue());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText().trim());

        value = sequence.nextValue();
        assertEquals("John", value.textValue());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());
    }

}