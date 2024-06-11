
package com.fasterxml.jackson.core.util;

import java.io.IOException;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {
    private JsonParserSequence jsonParserSequence;
    
    @Before
    public void setup() {
        JsonParser[] parsers = new JsonParser[2];
        // Create mock parsers
        parsers[0] = new MockJsonParser();
        parsers[1] = new MockJsonParser();
        jsonParserSequence = new JsonParserSequence(parsers);
    }
    
    @Test
    public void testClose() throws IOException {
        // Perform close operation on the sequence
        jsonParserSequence.close();
        // Check if the close operation was performed on all underlying parsers in the sequence
        MockJsonParser parser1 = (MockJsonParser) jsonParserSequence._parsers[0];
        assertTrue(parser1.isCloseCalled());
        MockJsonParser parser2 = (MockJsonParser) jsonParserSequence._parsers[1];
        assertTrue(parser2.isCloseCalled());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Perform nextToken operation on the sequence
        JsonToken token = jsonParserSequence.nextToken();
        // Check if the nextToken operation was performed on the delegate parser in the sequence
        MockJsonParser parser1 = (MockJsonParser) jsonParserSequence._parsers[0];
        assertTrue(parser1.isNextTokenCalled());
        // If the delegate parser returns a non-null token, it should be returned by the sequence
        assertNotNull(token);
    }
    
    @Test
    public void testContainedParsersCount() {
        // Check the containedParsersCount method returns the correct count
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
}
