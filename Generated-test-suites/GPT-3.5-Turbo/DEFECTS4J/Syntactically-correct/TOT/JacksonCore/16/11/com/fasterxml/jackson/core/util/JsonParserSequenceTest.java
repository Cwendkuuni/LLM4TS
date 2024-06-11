

package com.fasterxml.jackson.core.util;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testConstructor() throws IOException {
        // Arrange
        JsonParser mockParser1 = new MockJsonParser();
        JsonParser mockParser2 = new MockJsonParser();

        // Act
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);

        // Assert
        Assert.assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_SingleParser() throws IOException {
        // Arrange
        JsonParser mockParser1 = new MockJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { mockParser1 });

        // Assert
        Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
        Assert.assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testNextToken_MultipleParsers() throws IOException {
        // Arrange
        JsonParser mockParser1 = new MockJsonParser();
        JsonParser mockParser2 = new MockJsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);

        // Assert
        Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken());
        Assert.assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
        Assert.assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testClose() throws IOException {
        // Arrange
        JsonParser mockParser1 = new MockJsonParser();
        JsonParser mockParser2 = new MockJsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);

        // Act
        jsonParserSequence.close();

        // Assert
        Assert.assertTrue(((MockJsonParser) mockParser1).isCloseCalled());
        Assert.assertTrue(((MockJsonParser) mockParser2).isCloseCalled());
    }

    @Test
    public void testNextToken_EndOfSequence() throws IOException {
        // Arrange
        JsonParser mockParser1 = new MockJsonParser();
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { mockParser1 });

        // Act
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken();

        // Assert
        Assert.assertNull(jsonParserSequence.nextToken());
    }

}

class MockJsonParser extends JsonParser {

    private boolean isCloseCalled = false;

    @Override
    public void close() throws IOException {
        isCloseCalled = true;
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        if (_inputStream == null) {
            return null;
        }
        return super.nextToken();
    }

    public boolean isCloseCalled() {
        return isCloseCalled;
    }

    @Override
    public void setCodec(ObjectCodec c) {
        // Do nothing
    }

    @Override
    public ObjectCodec getCodec() {
        return null;
    }
}

