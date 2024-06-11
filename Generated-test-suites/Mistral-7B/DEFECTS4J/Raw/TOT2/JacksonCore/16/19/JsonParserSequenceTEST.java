package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest extends BaseTest {
    @Test
    public void testJsonParserSequence_BasicUsage() throws IOException, JsonProcessingException {
        JsonParser parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("/test1.json"));
        JsonParser parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("/test2.json"));
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Test nextToken() method with basic usage
        JsonToken token = null;
        while ((token = parserSequence.nextToken()) != null) {
            System.out.println("Current token: " + token.getClass().getSimpleName());
        }
        Assert.assertEquals(JsonToken.END_ARRAY, parserSequence.nextToken());
    }

    @Test
    public void testJsonParserSequence_EdgeCases() throws IOException, JsonProcessingException {
        JsonParser parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("/test1.json"));
        JsonParser parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("/test3.json")); // Empty JSON file
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Test nextToken() method with edge cases
        JsonToken token = null;
        while ((token = parserSequence.nextToken()) != null) {
            System.out.println("Current token: " + token.getClass().getSimpleName());
            if (token.isStructStart()) {
                ObjectCodec oc = parserSequence.getCodec();
                String fieldName = oc.treeAsTokens(parserSequence.nextValue()).nextValue().asText();
                Assert.assertEquals("testField", fieldName);
                parserSequence.nextFieldName();
                Assert.assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken().getClass());
                break; // Stop checking after finding the field
            }
        }
    }

    @Test(expected = JsonParseException.class)
    public void testJsonParserSequence_ErrorScenarios() throws IOException, JsonProcessingException {
        JsonParser parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("/test1.json"));
        JsonParser parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("/test4.json")); // Malformed JSON file
        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Test nextToken() method with error scenarios
        JsonToken token = null;
        while ((token = parserSequence.nextToken()) != null) {
            // This test case will throw JsonParseException, as the second parser encounters a malformed JSON file
        }
    }

    // 
}