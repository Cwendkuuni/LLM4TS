
package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken() throws IOException {
        // Prepare
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser1 = jsonFactory.createParser("[1, 2, 3]");
        JsonParser jsonParser2 = jsonFactory.createParser("[4, 5, 6]");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);

        // Test
        JsonToken token1 = jsonParserSequence.nextToken();
        JsonToken token2 = jsonParserSequence.nextToken();
        JsonToken token3 = jsonParserSequence.nextToken();
        JsonToken token4 = jsonParserSequence.nextToken();
        JsonToken token5 = jsonParserSequence.nextToken();
        JsonToken token6 = jsonParserSequence.nextToken();
        JsonToken token7 = jsonParserSequence.nextToken();

        // Verify
        Assert.assertEquals(JsonToken.START_ARRAY, token1);
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token2);
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token3);
        Assert.assertEquals(JsonToken.END_ARRAY, token4);
        Assert.assertEquals(JsonToken.START_ARRAY, token5);
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token6);
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token7);
    }

    @Test
    public void testClose() throws IOException {
        // Prepare
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser1 = jsonFactory.createParser("{}");
        JsonParser jsonParser2 = jsonFactory.createParser("{}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);

        // Test
        jsonParserSequence.close();

        // Verify
        Assert.assertTrue(jsonParser1.isClosed());
        Assert.assertTrue(jsonParser2.isClosed());
    }

    @Test
    public void testContainedParsersCount() {
        // Prepare
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser1 = jsonFactory.createParser("{}");
        JsonParser jsonParser2 = jsonFactory.createParser("{}");
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(jsonParser1, jsonParser2);

        // Test
        int containedParsersCount = jsonParserSequence.containedParsersCount();

        // Verify
        Assert.assertEquals(2, containedParsersCount);
    }
}

