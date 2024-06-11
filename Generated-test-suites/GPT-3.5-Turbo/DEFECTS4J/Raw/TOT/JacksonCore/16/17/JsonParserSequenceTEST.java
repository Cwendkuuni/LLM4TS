
package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);

        JsonParserSequence parserSequence = new JsonParserSequence(parsers);

        // Act
        parserSequence.close();

        // Assert
        // Verify that close() method is called for each parser in the sequence
        verify(parsers[0], times(1)).close();
        verify(parsers[1], times(1)).close();
    }

    @Test
    public void testNextToken_HappyPath() throws IOException, JsonParseException {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);

        // Mock the nextToken() method to return a valid token for the second parser
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonParserSequence parserSequence = new JsonParserSequence(parsers);

        // Act
        JsonToken token = parserSequence.nextToken();

        // Assert
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
    }

    @Test
    public void testNextToken_SwitchingParsers() throws IOException, JsonParseException {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);

        // Mock the nextToken() method to return null for both parsers initially,
        // but then return a valid token for the second parser after switching
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(null, JsonToken.VALUE_NUMBER_INT);

        JsonParserSequence parserSequence = new JsonParserSequence(parsers);

        // Act
        JsonToken token = parserSequence.nextToken();

        // Assert
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);

        // Verify that nextToken() method is called for the second parser after switching
        verify(parsers[1], times(2)).nextToken();
    }

    @Test
    public void testNextToken_NoValidToken() throws IOException, JsonParseException {
        // Arrange
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);

        // Mock the nextToken() method to return null for both parsers
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(null);

        JsonParserSequence parserSequence = new JsonParserSequence(parsers);

        // Act
        JsonToken token = parserSequence.nextToken();

        // Assert
        Assert.assertNull(token);

        // Verify that nextToken() method is called for both parsers
        verify(parsers[0], times(1)).nextToken();
        verify(parsers[1], times(1)).nextToken();
    }

    @Test
    public void testCreateFlattened() {
        // Arrange
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);

        // Act
        JsonParserSequence result = JsonParserSequence.createFlattened(first, second);

        // Assert
        // Verify that createFlattened() method returns a JsonParserSequence object
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof JsonParserSequence);
    }

    @Test
    public void testCreateFlattened_NoFlattening() {
        // Arrange
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);

        // Act
        JsonParserSequence.createFlattened(first, second);

        // Assert
        // Verify that addFlattenedActiveParsers() method is called for each parser
        verify(first, times(1)).addFlattenedActiveParsers(anyList());
        verify(second, times(1)).addFlattenedActiveParsers(anyList());
    }

    @Test
    public void testCreateFlattened_FlatteningSequences() {
        // Arrange
        JsonParser first = mock(JsonParserSequence.class);
        JsonParser second = mock(JsonParserSequence.class);

        // Act
        JsonParserSequence.createFlattened(first, second);

        // Assert
        // Verify that addFlattenedActiveParsers() method is called for each sequence
        verify(first, times(1)).addFlattenedActiveParsers(anyList());
        verify(second, times(1)).addFlattenedActiveParsers(anyList());
    }

    @Test
    public void testContainedParsersCount() {
        // Arrange
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);
        parsers[2] = mock(JsonParser.class);

        JsonParserSequence parserSequence = new JsonParserSequence(parsers);

        // Act
        int count = parserSequence.containedParsersCount();

        // Assert
        Assert.assertEquals(3, count);
    }
}

