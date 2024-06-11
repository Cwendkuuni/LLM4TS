package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        String json1 = "{\"name\":\"John\",\"age\":30}";
        String json2 = "{\"city\":\"New York\",\"country\":\"USA\"}";

        JsonParser parser1 = new JsonFactory().createParser(new ByteArrayInputStream(json1.getBytes(Base64Variants.UTF8)));
        JsonParser parser2 = new JsonFactory().createParser(new ByteArrayInputStream(json2.getBytes(Base64Variants.UTF8)));

        JsonParserSequence seq = JsonParserSequence.createFlattened(parser1, parser2);

        // Test nextToken() returns expected token from the first parser
        JsonToken token = seq.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // Test that first parser can parse given JSON data
        Map<String, Object> json1Map = new ObjectMapper().readValue(parser1, Map.class);
        assertEquals("John", json1Map.get("name"));
        assertEquals(30, json1Map.get("age"));

        // Test nextToken() returns expected token from the second parser
        token = seq.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // Test that second parser can parse given JSON data
        Map<String, Object> json2Map = new ObjectMapper().readValue(parser2, Map.class);
        assertEquals("New York", json2Map.get("city"));
        assertEquals("USA", json2Map.get("country"));

        // Test skipping tokens from the first parser
        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText());
        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText());
        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("age", token.getText());
        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
        assertEquals(30, token.getIntValue());

        // Test that nextToken() returns null when no more tokens are left
        token = seq.nextToken();
        assertNull(token);

        // Test that close() method calls close() on all underlying parsers
        seq.close();
        parser1.close();
        parser2.close();
    }

    @Test(expected = IOException.class)
    public void testIllegalArgumentException() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));
        JsonParser parser2 = null;

        JsonParserSequence seq = JsonParserSequence.createFlattened(parser1, parser2);

        seq.nextToken();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));
        JsonParser parser2 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[0]);
        seq.add(parser1);
        seq.add(parser2);

        seq.nextToken();
        seq.switchToNext();

        seq.nextToken();
        seq.switchToNext();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException2() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));
        JsonParser parser2 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[0]);
        seq.add(parser1);
        seq.add(parser2);

        seq.nextToken();
        seq.switchToNext();

        seq.nextToken();
        seq.switchToNext();

        seq.nextToken();
        seq.switchToNext();
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));
        JsonParser parser2 = new JsonFactory().createParser(new ByteArrayInputStream("{}".getBytes(Base64Variants.UTF8)));

        JsonParserSequence seq = JsonParserSequence.createFlattened(parser1, parser2);

        int containedParsersCount = seq.containedParsersCount();
        assertEquals(2, containedParsersCount);
    }
}