package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken_DefaultParser() throws IOException, JsonParseException {
        JsonParser firstParser = new JsonFactory().getParser("[]"); // Empty JSON array
        JsonParser secondParser = new JsonFactory().getParser("{}"); // Empty JSON object
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        JsonToken token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.END_ARRAY, token.getId());
    }

    // Add test cases for each method and edge cases here
}