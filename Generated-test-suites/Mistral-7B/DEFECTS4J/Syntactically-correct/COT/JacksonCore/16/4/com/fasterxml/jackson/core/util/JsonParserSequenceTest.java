package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

/**
 * Test class for JsonParserSequence
 */
public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence seq;

    @Before
    public void setUp() throws IOException {
        parser1 = new JsonFactory().createParser("[]");
        parser2 = new JsonFactory().createParser("{\"name\": \"John\"}");
        seq = JsonParserSequence.createFlattened(parser1, parser2);
    }

    

    /**
     * Test for default constructor
     */
    @Test
    public void testJsonParserSequenceConstructor() {
        JsonParser[] parsers = new JsonParser[0];
        JsonParserSequence seq = new JsonParserSequence(parsers);
        Assert.assertNull(seq.delegate);
        Assert.assertArrayEquals(parsers, seq._parsers);
        Assert.assertEquals(0, seq._nextParser);
    }

    /**
     * Test for constructor with valid parsers
     */
    @Test
    public void testJsonParserSequenceConstructorWithValidParsers() {
        Assert.assertNotNull(seq.delegate);
        Assert.assertArrayEquals(new JsonParser[]{parser1, parser2}, seq._parsers);
        Assert.assertEquals(1, seq._nextParser);
    }

    /**
     * Test nextToken() with one parser
     */
    @Test
    public void testNextTokenWithOneParser() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[]");
        JsonParserSequence seq = new JsonParserSequence(new JsonParser[]{parser});
        JsonToken token = seq.nextToken();
        Assert.assertNotNull(token);
        parser.close();
    }

    /**
     * Test nextToken() with multiple parsers
     */
    @Test
    public void testNextTokenWithMultipleParsers() throws IOException {
        JsonToken token;
        while ((token = seq.nextToken()) != null) {
            switch (token.getTypeId()) {
                case VALUE_STRING:
                    Assert.assertEquals("null", token.getValueAsString());
                    break;
                case FIELD_NAME:
                    Assert.assertEquals("name", token.getValueAsString());
                    break;
                case START_OBJECT:
                case END_OBJECT:
                    break;
                case START_ARRAY:
                    Assert.assertEquals(JsonToken.END_ARRAY, seq.nextToken().getTypeId());
                    break;
                case END_ARRAY:
                    Assert.assertEquals(JsonToken.START_OBJECT, seq.nextToken().getTypeId());
                    break;
                case END_OBJECT:
                    break;
                default:
                    Assert.fail("Unexpected token type: " + token.getTypeId());
            }
        }
        parser1.close();
        parser2.close();
    }

    /**
     * Test containedParsersCount()
     */
    @Test
    public void testContainedParsersCount() {
        Assert.assertEquals(2, seq.containedParsersCount());
    }

    /**
     * Test switchToNext() with valid sequence
     */
    @Test
    public void testSwitchToNextWithValidSequence() throws IOException {
        boolean success = seq.switchToNext();
        Assert.assertTrue(success);
        JsonToken token = seq.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token.getTypeId());
    }

    /**
     * Test switchToNext() with empty sequence
     */
    @Test(expected = IOException.class)
    public void testSwitchToNextWithEmptySequence() {
        JsonParserSequence seq = new JsonParserSequence(new JsonParser[0]);
        seq.switchToNext();
    }

    /**
     * Test switchToNext() with invalid index
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSwitchToNextWithInvalidIndex() throws IOException {
        JsonParser[] parsers = new JsonParser[3];
        JsonParserSequence seq = new JsonParserSequence(parsers);
        seq.switchToNext();
        seq.switchToNext();
        seq.switchToNext();
    }

    
}